import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PetTest extends BaseTest {

    @DisplayName("Pet-проект")
    @ParameterizedTest
    @CsvFileSource(resources = "testData/laptops.csv", numLinesToSkip = 1)
    public void testBellIntegrator(String priceFrom, String priceTo, String brands, String countElements) {
        goPage(ConfigReader.get("base.url"));
        waitLoadPage();
        mainPageSteps.clickBtnApplyCookies();
        mainPageSteps.moveToAndClickBtnCatalog();
        mainPageSteps.moveToElectronicSection();
        mainPageSteps.moveToAndClickBtnLaptop();
        waitLoadPage();
        assertTrue(laptopPageSteps.checkLaptopPage(), "Страница ноутбуков не открылась");

        laptopPageSteps.setPrice(priceFrom, priceTo);
        boolean brandFound = laptopPageSteps.setModel(brands);
        laptopPageSteps.loadAllProducts();
        Assertions.assertAll(
                () -> assertTrue(laptopPageSteps.checkCountElementsOnPage() > Integer.parseInt(countElements),
                        "Количество товаров на первой странице меньше " + countElements),
                () -> assertTrue(laptopPageSteps.checkPrice(priceFrom, priceTo),
                        "Цена товаров не соответствует фильтру: " + priceFrom + "–" + priceTo)
        );

        if (brandFound) {
            List<String> resultModel = laptopPageSteps.checkModel(brands);
            Assertions.assertTrue(resultModel.isEmpty(),
                    "Товары не соответствует фильтру Модель: " + brands + "\nНе соответствующие товары:" + resultModel);
        }
        String nameFirstProduct = laptopPageSteps.enterNameToInputSearch();
        waitLoadPage();
        Assertions.assertTrue(searchPageSteps.checkSearchLaptopName(nameFirstProduct), "В результатах поиска нет искомого товара");

    }
}