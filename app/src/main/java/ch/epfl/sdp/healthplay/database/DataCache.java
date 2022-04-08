package ch.epfl.sdp.healthplay.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DataCache {
    private Map<String, Map<String, Number>> current_map;
    private Context context;
    private Database db = new Database();
    private FirebaseAuth fa = FirebaseAuth.getInstance();

    public DataCache(Context context, DatabaseReference dbr, FirebaseAuth fa){
        db = new Database(dbr);
        this.fa = fa;
        init(context);
    }

    public DataCache(Context context){
        init(context);
    }
    public void init(Context context){
        this.context = context;
        fa.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = fa.getCurrentUser();
                if(user==null)
                    read();
                else {
                    db.mDatabase.child("users").child(user.getUid()).child("stats").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            store(user.getUid());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    store(user.getUid());
                }
            }
        });
    }

    private String readFile() throws IOException{
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(new File(context.getCacheDir(),"")+"cacheFile.txt")));
        InputStreamReader reader = new InputStreamReader(in);
        int character = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while((character = in.read()) != -1){
            stringBuilder.append((char)character);
        }
        Log.d("Debug lecture du fichier : ", stringBuilder.toString());
        in.close();
        return stringBuilder.toString();
    }

    private void read(){
        try {
            String jsonInput = readFile();
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, HashMap<String, Number>>> typeRef = new TypeReference<HashMap<String, HashMap<String, Number>>>(){};
            Map<String, Map<String, Number>> map = mapper.readValue(jsonInput, typeRef);
            current_map = map;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void store(String id){
        db.getStats(id, task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                Map<String, Map<String, Number>> map = (Map<String, Map<String, Number>>) task.getResult().getValue();
                current_map = map;
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<HashMap<String, HashMap<String, Number>>> typeRef = new TypeReference<HashMap<String, HashMap<String, Number>>>(){};
                ObjectOutputStream out = null;
                try {
                    String json = mapper.writerFor(typeRef).writeValueAsString(map);
                    Log.d("Ecriture", json);
                    out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"")+"cacheFile.txt"));
                    out.writeBytes(json);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public Map<String, Map<String, Number>> getDataMap(){
        return current_map;
    }
}
