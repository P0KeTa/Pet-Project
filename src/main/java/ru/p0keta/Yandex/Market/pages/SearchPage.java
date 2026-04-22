package ru.p0keta.Yandex.Market.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Getter
public class SearchPage {

    protected WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    protected static final By productLocatorName = By.xpath("//div[@class='cia-cs _1pFpJ']//span[@title]");
}
