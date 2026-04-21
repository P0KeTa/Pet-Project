package ru.p0keta.Yandex.Market.steps;

import io.qameta.allure.Step;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.p0keta.Yandex.Market.pages.LaptopPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static ru.p0keta.Yandex.Market.pages.SearchPage.inputSearch;

/**
 * Шаги для работы со страницей ноутбуков на Яндекс.Маркете.
 * Содержит методы для установки фильтров и проверки результатов.
 */
public class LaptopPageSteps extends LaptopPage {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    WebElement element;

    public LaptopPageSteps(WebDriver driver) {
        super(driver);
    }

    @Step("Убедимся что перешли в раздел Ноутбуки")
    public boolean checkLaptopPage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(laptopText)).isDisplayed();
    }

    /**
     * Устанавливает диапазон цены.
     *
     * @param priceFrom нижняя граница цены
     * @param priceTo верхняя граница цены
     */
    @Step("Установка параметра Цена")
    public void setPrice(String priceFrom, String priceTo) {
        driver.findElement(inputPriceFrom).sendKeys(priceFrom);
        driver.findElement(inputPriceTo).sendKeys(priceTo);
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
            js.executeScript("window.scrollBy(0, 500)");
            waitForMoreProducts(lastCount);
        }
        //Скролл в начало страницы
        js.executeScript("window.scrollTo(0, 0);");
    }
    private void waitForMoreProducts(int previousCount) {
        long endTime = System.currentTimeMillis() + 700L;
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
     * @param priceTo верхняя граница цены
     * @return список некорректных значений цен
     */
    @Step("Проверяем соответствие фильтра Цена")
    public boolean checkPrice(String priceFrom, String priceTo) {
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
        return result.isEmpty();
    }

    /**
     * Проверяет, что все товары на странице соответствуют переданным брендам.
     *
     * @param brands строка брендов через запятую, например "HP, Lenovo"
     * @return список товаров, которые не соответствуют фильтру
     */
    @Step("Проверяем соответствие фильтра Модель")
    public List<String> checkModel(@NonNull String brands) {
        List<WebElement> products = driver.findElements(productLocatorName);
        List<String> result = new ArrayList<>();
        String[] modelsArr = brands.split(",");

        for (WebElement product : products) {
            String title = product.getText().trim().toLowerCase();
            boolean matches = false;
            for (String s : modelsArr) {
                if (title.contains(s.trim().toLowerCase())) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                result.add(title);
            }
        }
        return result;
    }

    @Step("Вводим в поисковую строку наименование ноутбука из первой карточки и кликаем по кнопке Найти")
    public String enterNameToInputSearch() {
        List<WebElement> products = driver.findElements(productLocatorName);
        String nameFirstProduct = products.get(0).getText();
        driver.findElement(inputSearch).sendKeys(nameFirstProduct + Keys.ENTER);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        return nameFirstProduct;
    }
}
