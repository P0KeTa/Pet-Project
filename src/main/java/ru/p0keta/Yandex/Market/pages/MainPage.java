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

    protected static final By btnCatalog = By.xpath("//div[@data-zone-name='catalog']");
    protected static final By electronicalSection = By.xpath("//div[@data-auto='catalog-content']//span[text()='Электроника']");
    protected static final By btnLaptop = By.xpath("//a[text()='Ноутбуки']");


}
