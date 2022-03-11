package ch.epfl.sdp.healthplay.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;

public class ProductTest {
    private final static String CORRECT_JSON = "{\n" +
            "  \"code\":\"0737628064502\",\n" +
            "  \"product\":{\n" +
            "  \"generic_name\":\"Rice Noodles\",\n" +
            "  \"product_name\":\"Thai peanut noodle kit includes stir-fry rice noodles & thai peanut seasoning\",\n" +
            "  \"nutriments\":{\n" +
            "  \"energy-kcal\":385,\n" +
            "  }}}";
    private final static String EMPTY_JSON = "{\n" +
            "  \"code\":\"0737628064502\",\n" +
            "  \"product\":{\n" +
            "  \"generic_name\": null,\n" +
            "  \"product_name\":null,\n" +
            "  \"nutriments\":{\n" +
            "  \"energy-kcal\":null,\n" +
            "  }}}";

    @Test
    public void testNotCorrectlyFormattedJSONTIsNotEmpty() {
        assertFalse(Product.of("unformatted: null").isPresent());
    }

    @Test
    public void testCorrectName() {
        Optional<Product> optionalProduct = Product.of(CORRECT_JSON);
        assertTrue(optionalProduct.isPresent());
        Product p = optionalProduct.get();
        assertEquals(
                "Thai peanut noodle kit includes stir-fry rice noodles & thai peanut seasoning",
                p.getName());
    }

    @Test
    public void testEmptyName() {
        Optional<Product> optionalProduct = Product.of(EMPTY_JSON);
        assertTrue(optionalProduct.isPresent());
        Product p = optionalProduct.get();
        assertEquals(Product.UNKNOWN_NAME, p.getName());
    }

    @Test
    public void testCorrectKCal() {
        Optional<Product> optionalProduct = Product.of(CORRECT_JSON);
        assertTrue(optionalProduct.isPresent());
        Product p = optionalProduct.get();
        assertEquals("Rice Noodles", p.getGenericName());
    }

    @Test
    public void testEmptyKCal() {
        Optional<Product> optionalProduct = Product.of(EMPTY_JSON);
        assertTrue(optionalProduct.isPresent());
        Product p = optionalProduct.get();
        assertEquals(Product.UNKNOWN_NAME, p.getGenericName());
    }

    @Test
    public void testCorrectGenericName() {
        Optional<Product> optionalProduct = Product.of(CORRECT_JSON);
        assertTrue(optionalProduct.isPresent());
        Product p = optionalProduct.get();
        assertEquals(385, p.getKCalEnergy());
    }

    @Test
    public void testEmptyGenericName() {
        Optional<Product> optionalProduct = Product.of(EMPTY_JSON);
        assertTrue(optionalProduct.isPresent());
        Product p = optionalProduct.get();
        assertEquals(Product.UNKNOWN_VALUE, p.getKCalEnergy());
    }
}
