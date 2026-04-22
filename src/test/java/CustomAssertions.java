
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс для проверок.
 * Содержит методы для проверок ОР и ФР
 */
public class CustomAssertions {

    public static void assertTrueCondition(boolean condition, String message) {
        assertAll(
                () -> assertTrue(condition, message)
                );
    }

    public static void assertProductCountMoreThan(int actual, int expected) {
        assertAll(
                () -> assertTrue(
                actual > expected,
                "Ожидалось больше " + expected + " товаров, но найдено: " + actual)
        );
    }

    public static void assertNoInvalidBrands(List<String> invalidProducts) {
        assertAll(
                () -> assertTrue(
                invalidProducts.isEmpty(),
                "Найдены товары с неверным брендом: " + invalidProducts)
        );
    }

    public static void assertNoInvalidPrices(List<String> invalidPrices) {
        assertAll(
                () -> assertTrue(
                invalidPrices.isEmpty(),
                "Найдены товары с неверной ценой: " + invalidPrices)
        );
    }

    public static void assertProductFound(boolean isFound, String productName) {
        assertAll(
                () -> assertTrue(
                isFound,
                "Товар не найден в поиске: " + productName)
        );
    }
}
