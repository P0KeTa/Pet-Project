package steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.p0keta.Yandex.Market.pages.SearchPage;

import java.util.List;

/**
 * Шаги для работы со страницей поиска на Яндекс.Маркете.
 * Содержит методы для проверки результатов поиска.
 */
public class SearchPageSteps extends SearchPage {

    public SearchPageSteps(WebDriver driver) {
        super(driver);
    }

    /**
     * Проверяет, что поиск соответствует искомого товара.
     *
     * @param nameFirstProduct имя искомого товара
     */
    @Step("Проверяем что в результатах поиска, на первой странице, есть искомый товар")
    public boolean checkSearchLaptopName(String nameFirstProduct) {
        List<WebElement> productsSearchPage = driver.findElements(productLocatorName);
        for (WebElement product : productsSearchPage) {
            if (product.getText().equals(nameFirstProduct)) {
                return true;
            }
        }
        return false;
    }
}
