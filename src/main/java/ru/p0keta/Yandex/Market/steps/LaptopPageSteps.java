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

    @Step("Установка параметра Цена")
    public void setPrice(String priceFrom, String priceTo) {
        driver.findElement(inputPriceFrom).sendKeys(priceFrom);
        driver.findElement(inputPriceTo).sendKeys(priceTo);
    }

    @Step("Установка параметра Производитель")
    public void setModel(@NonNull String models) {
        String[] modelArr = models.split(",");

        if (!driver.findElements(btnGetAll).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(btnGetAll)).click();
        }

        for (String model : modelArr) {
            model = model.trim();
            if (!driver.findElements(inputModel).isEmpty()) {
                element = wait.until(ExpectedConditions.elementToBeClickable(inputModel));
                element.sendKeys(model);
            }

            By checkboxModel = By.xpath("//div//span[text()='" + model + "']");

            if (!driver.findElements(checkboxModel).isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(checkboxModel)).click();
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
            }
            if (!driver.findElements(inputModel).isEmpty()) {
                driver.findElement(inputModelCancel).click();
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
            }
        }
    }

    @Step("Проверяем кол-во элементов на первой странице")
    public int checkCountElementsOnPage() {
        List<WebElement> products = driver.findElements(productLocator);
        return products.size();
    }

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
        long endTime = System.currentTimeMillis() + 500L;
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

    @Step("Проверяем соответствие фильтра Модель")
    public boolean checkModel(@NonNull String models) {
        if (models.isEmpty()) {
            return false;
        } else {
            List<WebElement> products = driver.findElements(setLocator(models));
            List<String> result = new ArrayList<>();
            String[] modelsArr = models.split(",");

            for (WebElement product : products) {
                for (String s : modelsArr) {
                    String m = s.trim();
                    if (!(product.getText().equals(m))) {
                        result.add(s);
                    }
                }
            }
            return result.isEmpty();
        }
    }

    private @NonNull By setLocator(@NonNull String model) {
        String[] models = model.split(",");
        StringBuilder xpath = new StringBuilder("//div[@data-auto-themename='list_full']//span[");
        for (int i = 0; i < models.length; i++) {
            String m = models[i].trim();
            xpath.append("contains(@title,'").append(m).append("')");
            if (i < models.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]");
        return By.xpath(xpath.toString());
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
