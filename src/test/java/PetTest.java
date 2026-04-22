import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.p0keta.Yandex.Market.ConfigReader;

import java.util.List;

public class PetTest extends BaseTest {

    @DisplayName("Pet-проект")
    @ParameterizedTest
    @CsvFileSource(resources = "testData/laptops.csv", numLinesToSkip = 1)
    public void testBellIntegrator(String priceFrom, String priceTo, String brands, String countElements) {
        goPage(ConfigReader.getString("base.url"));
        waitLoadPage();
        mainPageSteps.clickBtnApplyCookies();
        mainPageSteps.moveToAndClickBtnCatalog();
        mainPageSteps.moveToElectronicSection();
        mainPageSteps.moveToAndClickBtnLaptop();
        waitLoadPage();
        CustomAssertions.assertTrueCondition(
                laptopPageSteps.checkLaptopPage(),
                "Страница ноутбуков не открылась"
        );

        laptopPageSteps.setPrice(priceFrom, priceTo);
        boolean brandFound = laptopPageSteps.setModel(brands);
        laptopPageSteps.loadAllProducts();
        waitLoadPage();
        CustomAssertions.assertProductCountMoreThan(
                laptopPageSteps.checkCountElementsOnPage(),
                Integer.parseInt(countElements)
        );
        CustomAssertions.assertNoInvalidPrices(
                laptopPageSteps.checkPrice(priceFrom, priceTo)
        );

        if (brandFound) {
            List<String> resultModel = laptopPageSteps.checkBrands(brands);
            CustomAssertions.assertNoInvalidBrands(resultModel);
        }

        String nameFirstProduct = laptopPageSteps.enterNameToInputSearch();
        waitLoadPage();
        CustomAssertions.assertProductFound(
                searchPageSteps.checkSearchLaptopName(nameFirstProduct),
                nameFirstProduct
        );

    }
}