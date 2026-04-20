package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.p0keta.Yandex.Market.data.TestData.URL_PAGE;

public class PetTest extends BaseTest {

    @DisplayName("Pet-проект")
    @ParameterizedTest
    @CsvSource({
            "10000, 20000, 'HP,Lenovo', 7"
    })
    public void testBellIntegrator(String priceFrom, String priceTo, String models, String countElements) {
        goPage(URL_PAGE);
        mainPageSteps.clickBtnApplyCookies();
        mainPageSteps.moveToAndClickBtnCatalog();
        mainPageSteps.moveToElectronicSection();
        mainPageSteps.moveToAndClickBtnLaptop();
        waitLoadPage();
        assertTrue(laptopPageSteps.checkLaptopPage(), "Страница ноутбуков не открылась");

        laptopPageSteps.setPrice(priceFrom, priceTo);
        laptopPageSteps.setModel(models);
        laptopPageSteps.loadAllProducts();
        Assertions.assertAll(
                () -> assertTrue(laptopPageSteps.checkCountElementsOnPage() > Integer.parseInt(countElements),
                        "Количество товаров на первой странице меньше " + countElements),
                () -> assertTrue(laptopPageSteps.checkPrice(priceFrom, priceTo),
                        "Цена товаров не соответствует фильтру: " + priceFrom + "–" + priceTo),
                () -> assertTrue(laptopPageSteps.checkModel(models), "Товары не соответствует фильтру Модель: " + models)
        );
        String nameFirstProduct = laptopPageSteps.enterNameToInputSearch();
        waitLoadPage();
        Assertions.assertTrue(searchPageSteps.checkSearchLaptopName(nameFirstProduct), "В результатах поиска нет искомого товара");

    }
}