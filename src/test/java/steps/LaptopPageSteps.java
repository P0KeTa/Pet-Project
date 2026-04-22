package steps;

import io.qameta.allure.Step;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.p0keta.Yandex.Market.ConfigReader;
import ru.p0keta.Yandex.Market.pages.LaptopPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Шаги для работы со страницей ноутбуков на Яндекс.Маркете.
 * Содержит методы для установки фильтров и проверки результатов.
 */
public class LaptopPageSteps extends LaptopPage {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ConfigReader.get("timeout"))));
    WebElement element;

    public LaptopPageSteps(WebDriver driver) {
        super(driver);
    }

    /**
     * Проверяет, что перешли нужный раздел (Ноутбуки).
     */
    @Step("Убедимся что перешли в раздел Ноутбуки")
    public boolean checkLaptopPage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(laptopText)).isDisplayed();
    }

    /**
     * Устанавливает диапазон цены.
     *
     * @param priceFrom нижняя граница цены
     * @param priceTo   верхняя граница цены
     */
    @Step("Установка параметра Цена")
    public void setPrice(String priceFrom, String priceTo) {
        if (driver.findElements(buttonPriceFromClear).isEmpty()) {
            driver.findElement(inputPriceFrom).sendKeys(priceFrom);
        } else {
            driver.findElement(buttonPriceFromClear).click();
            driver.findElement(inputPriceFrom).sendKeys(priceFrom);
        }

        if (driver.findElements(inputPriceToClear).isEmpty()) {
            driver.findElement(inputPriceTo).sendKeys(priceTo);
        } else {
            driver.findElement(inputPriceToClear).click();
            driver.findElement(inputPriceTo).sendKeys(priceTo);
        }
    }

    /**
     * Устанавливает необходимые бренды.
     *
     * @param brands строка брендов через запятую, например "HP, Lenovo"
     */
    @Step("Установка параметра Производитель")
    public boolean setModel(@NonNull String brands) {
        String[] brandArr = brands.split(",");

        boolean brandFound = true;

        if (!driver.findElements(btnGetAll).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(btnGetAll)).click();
        }

        for (String brand : brandArr) {
            brand = brand.trim();
            if (!driver.findElements(inputModel).isEmpty()) {
                element = wait.until(ExpectedConditions.elementToBeClickable(inputModel));
                element.sendKeys(brand);
            }

            By checkboxModel = By.xpath("//div//span[text()='" + brand + "']");

            if (!driver.findElements(checkboxModel).isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(checkboxModel)).click();
            } else {
                brandFound = false;
                continue;
            }
            if (!driver.findElements(inputModel).isEmpty()) {
                driver.findElement(inputModelCancel).click();
            }
        }

        return brandFound;
    }

    /**
     * Проверяет, кол-во элементов товара на первой странице
     *
     * @return кол-во товаров
     */
    @Step("Проверяем кол-во элементов на первой странице")
    public int checkCountElementsOnPage() {
        List<WebElement> products = driver.findElements(productLocator);
        return products.size();
    }

    /**
     * Медленный скролл до конца страницы и возврат в начало страницы.
     * Каждые 0.5 сек пролистывает страницу на 500 пикселей
     */
    @Step("Плавно скроллим и ждём загрузку всех товаров")
    public void loadAllProducts() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int lastCount = 0;
        int noChanges = 0;
        while (true) {
            int currentCount = driver.findElements(productLocator).size();
            if (currentCount > lastCount) {
                lastCount = currentCount;
                noChanges = 0;
            } else {
                noChanges++;
            }
            if (noChanges >= 3 && isAtBottom()) {
                break;
            }
            js.executeScript("window.scrollBy(0, 600)");
            waitForMoreProducts(lastCount);
        }
        //Скролл в начало страницы
        js.executeScript("window.scrollTo(0, 0);");
    }

    private void waitForMoreProducts(int previousCount) {
        long endTime = System.currentTimeMillis() + 800L;
        while (System.currentTimeMillis() < endTime) {
            int currentCount = driver.findElements(productLocator).size();
            if (currentCount > previousCount) {
                return;
            }
        }
    }

    private boolean isAtBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object result = js.executeScript(
                "return window.innerHeight + window.pageYOffset >= document.body.scrollHeight - 5");
        return Boolean.TRUE.equals(result);
    }

    /**
     * Проверяет, что цены всех товаров находятся в заданном диапазоне.
     *
     * @param priceFrom нижняя граница цены
     * @param priceTo   верхняя граница цены
     * @return список некорректных значений цен
     */
    @Step("Проверяем соответствие фильтра Цена")
    public List<String> checkPrice(String priceFrom, String priceTo) {
        List<WebElement> products = driver.findElements(productLocatorPrice);
        List<String> result = new ArrayList<>();

        int min = Integer.parseInt(priceFrom);
        int max = Integer.parseInt(priceTo);

        for (WebElement product : products) {
            String rawText = product.getText().trim();
            String digitsOnly = rawText.replaceAll("[^0-9]", "");
            if (digitsOnly.isEmpty()) {
                continue;
            }
            int price = Integer.parseInt(digitsOnly);
            if (price <= min || price >= max) {
                result.add(String.valueOf(price));
            }
        }
        return result;
    }

    /**
     * Проверяет, что все товары на странице соответствуют переданным брендам.
     *
     * @param brands строка брендов через запятую, например "HP, Lenovo"
     * @return список товаров, которые не соответствуют фильтру
     */
    @Step("Проверяем соответствие фильтра Модель")
    public List<String> checkBrands(@NonNull String brands) {
        List<WebElement> products = driver.findElements(productLocatorName);
        List<String> result = new ArrayList<>();
        String[] brandsArr = brands.split(",");
        if (brandsArr.length > 0) {
            for (WebElement product : products) {
                String title = product.getText().trim().toLowerCase();
                boolean matches = false;
                for (String b : brandsArr) {
                    if (title.contains(b.trim().toLowerCase())) {
                        matches = true;
                        break;
                    }
                }
                if (!matches) {
                    result.add(title);
                }
            }
        }
        return result;
    }

    /**
     * Берёт название первого элемента на странице (карточки товара),
     * вводит его в поле поиска и кликает по кнопке Найти.
     */
    @Step("Вводим в поисковую строку наименование ноутбука из первой карточки и кликаем по кнопке Найти")
    public String enterNameToInputSearch() {
        List<WebElement> products = driver.findElements(productLocatorName);
        String nameFirstProduct = products.get(0).getText();
        driver.findElement(inputSearch).sendKeys(nameFirstProduct);
        driver.findElement(btnSearch).click();
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(ConfigReader.get("timeout"))));
        return nameFirstProduct;
    }
}
