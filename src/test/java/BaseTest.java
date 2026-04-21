import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.p0keta.Yandex.Market.steps.LaptopPageSteps;
import ru.p0keta.Yandex.Market.steps.MainPageSteps;
import ru.p0keta.Yandex.Market.steps.SearchPageSteps;

import java.time.Duration;
import java.util.Objects;
/**
 * Базовый класс всех тестов
 * Содержит методы для инициализации WebDriver/классов, открытие браузера, ожидания загрузки, открытия страницы и закрытия браузера
 */
public class BaseTest {

    WebDriver driver;
    MainPageSteps mainPageSteps;
    LaptopPageSteps laptopPageSteps;
    SearchPageSteps searchPageSteps;

    @Step("Открытие браузера и инициализация WebDriver и классов")
    @BeforeEach
    public void initBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        mainPageSteps = new MainPageSteps(driver);
        laptopPageSteps =  new LaptopPageSteps(driver);
        searchPageSteps = new SearchPageSteps(driver);
    }

    @Step("Открытие главной страницы")
    public void goPage(String urlPage) {
        driver.manage().window().maximize();
        driver.get(urlPage);
    }

    @Step("Ожидание загрузни страницы")
    public void waitLoadPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ConfigReader.get("timeout"))));
        wait.until(webDriver -> Objects.equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"), "complete"));
    }

    @Step("Закрытие браузера")
    @AfterEach
    public void CloseBrowser() {
        driver.quit();
    }
}