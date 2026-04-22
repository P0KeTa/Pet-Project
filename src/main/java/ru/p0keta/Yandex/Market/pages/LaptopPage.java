package ru.p0keta.Yandex.Market.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Getter
public class LaptopPage extends MainPage {

    public LaptopPage(WebDriver driver) {
        super(driver);
    }

    protected static final By laptopText = By.xpath("//h1[contains(text(),'Ноутбуки')]");
    protected static final By inputPriceFrom = By.xpath("//input[@id='range-filter-field-glprice_25563_min']");
    protected static final By buttonPriceFromClear = By.xpath("//div[@data-node-id='etzr_5i90_4']//button[@title='Очистить']");
    protected static final By inputPriceTo = By.xpath("//input[@id='range-filter-field-glprice_25563_max']");
    protected static final By inputPriceToClear = By.xpath("//div[@data-node-id='etzr_5i90_5']//button[@title='Очистить']");
    protected static final By btnGetAll = By.xpath("//button[.//span[contains(text(),'Показать всё')]]");
    protected static final By inputModel = By.xpath("//input[@placeholder='Найти']");
    protected static final By inputModelCancel = By.xpath("//button[@title='Очистить']");
    protected static final By productLocator = By.xpath("//div[@data-data-source='ss-product']");
    protected static final By productLocatorPrice = By.xpath("//div[@data-data-source='ss-product' and @data-auto-themename='list_full']//span[@data-auto='snippet-price-current']/span[1]");
    protected static final By productLocatorName = By.xpath("//div[@data-data-source='ss-product']//span[@title]");
}
