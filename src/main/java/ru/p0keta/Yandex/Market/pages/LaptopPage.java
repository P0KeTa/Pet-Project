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
    protected static final By inputPriceTo = By.xpath("//input[@id='range-filter-field-glprice_25563_max']");
    protected static final By btnGetAll = By.xpath("//button[.//span[contains(text(),'Показать всё')]]");
    protected static final By inputModel = By.xpath("//input[@id='textfield9248505620']");
    protected static final By inputModelCancel = By.xpath("//div[@class='_3vybc']//div[@class='_2xL77 _3Y8VG']");
    protected static final By productLocator = By.xpath("//div[@data-auto-themename='list_full']//div[@class='cia-cs Gqfzd']");
    protected static final By productLocatorPrice = By.xpath("//div[@data-auto-themename='list_full']//span[@class='ds-text ds-text_group_core ds-text_weight_bold ds-text_color_price-term ds-text_typography_headline-5 ds-text_headline-5_tight ds-text_headline-5_bold']");
    protected static final By productLocatorName = By.xpath("//div[@class='cia-cs _1pFpJ']//span[@title]");
}
