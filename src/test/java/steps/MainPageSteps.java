package steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.p0keta.Yandex.Market.ConfigReader;
import ru.p0keta.Yandex.Market.pages.MainPage;

import java.time.Duration;
import java.util.List;

/**
 * Шаги для работы с главной страницей на Яндекс.Маркете.
 * Содержит методы для перехода на нужные разделы на странице и клика по кнопке согласия Куки.
 */
public class MainPageSteps extends MainPage {
    WebElement element;
    Actions action;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("timeout")));

    public MainPageSteps(WebDriver driver) {
        super(driver);
    }

    /**
     * Нажимает на кнопку принятия куки (если она есть)
     */
    @Step("Клик по кнопке согласия Куки")
    public void clickBtnApplyCookies() {
        List<WebElement> cookies = driver.findElements(btnApplyCookies);

        if (!cookies.isEmpty()) {
            element = cookies.get(0);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        }
    }

    /**
     * Клик по кнопке Каталог
     */
    @Step("Переходим в раздел Каталог")
    public void moveToAndClickBtnCatalog() {
        driver.findElement(btnCatalog).click();
    }

    /**
     * Наводит курсор на раздел Электроника
     */
    @Step("Наводим курсор на раздел Электроника")
    public void moveToElectronicSection() {
        element = wait.until(ExpectedConditions.elementToBeClickable(electronicalSection));
        action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    /**
     * Клик по разделу Ноутбуки
     */
    @Step("Переходим в раздел Ноутбуки")
    public void moveToAndClickBtnLaptop() {
        element = wait.until(ExpectedConditions.elementToBeClickable(btnLaptop));
        element.click();
    }
}
