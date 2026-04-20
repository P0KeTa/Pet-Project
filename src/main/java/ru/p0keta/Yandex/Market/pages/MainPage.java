package ru.p0keta.Yandex.Market.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Getter
public class MainPage {
    protected WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    protected static final By btnApplyCookies = By.xpath("//div[text()='Allow all']");

    protected static final By btnCatalog = By.xpath("//button[@class='ds-button ds-button_variant_text ds-button_type_primary ds-button_size_m ds-button_brand_market _WsTn']");
    protected static final By electronicalSection = By.xpath("//span[@class='_3W4t0' and text()='Электроника']");
    protected static final By btnLaptop = By.xpath("//a[text()='Ноутбуки']");


}
