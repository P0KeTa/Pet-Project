import org.junit.jupiter.api.Assertions;

import java.util.List;

public class CustomAssertions {

    public static void assertTrueCondition(boolean condition, String message) {
        Assertions.assertTrue(condition, message);
    }

    public static void assertProductCountMoreThan(int actual, int expected) {
        Assertions.assertTrue(
                actual > expected,
                "Ожидалось больше " + expected + " товаров, но найдено: " + actual
        );
    }

    public static void assertNoInvalidBrands(List<String> invalidProducts) {
        Assertions.assertTrue(
                invalidProducts.isEmpty(),
                "Найдены товары с неверным брендом: " + invalidProducts
        );
    }

    public static void assertNoInvalidPrices(List<String> invalidPrices) {
        Assertions.assertTrue(
                invalidPrices.isEmpty(),
                "Найдены товары с неверной ценой: " + invalidPrices
        );
    }

    public static void assertProductFound(boolean isFound, String productName) {
        Assertions.assertTrue(
                isFound,
                "Товар не найден в поиске: " + productName
        );
    }
}
