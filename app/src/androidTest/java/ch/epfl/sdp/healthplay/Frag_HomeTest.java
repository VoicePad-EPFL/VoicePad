package ch.epfl.sdp.healthplay;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Frag_HomeTest {
    @Rule
    public ActivityScenarioRule<HomeScreenActivity> testRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    /*@Test
    public void getDateTest() {
        ViewInteraction sus = onView( Matchers.allOf(withId(R.id.calendar),hasChildCount(3)));
        sus.perform(ViewActions.click());
        onView(withId(R.id.my_date)).check(
                matches(
                        withText("2022-03-09: \nStats :\n calorie_counter=1001\n last_current_weight=179\n health_point=9")
                )
        );
    }*/

    @Test
    public void getWelcomeMessage() {
        onView(withId(R.id.my_date)).check(
                matches(
                        withText("Welcome to the home page! \nChoose a date to check what you've consumed.")));
    }
}