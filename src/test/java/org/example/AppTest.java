package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    static WebDriver webDriver;

    @AfterEach
    void exit() {
        webDriver.quit();
    }

    @BeforeEach
    void initMainPage() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("start-maximized");
        options.setPageLoadTimeout(Duration.ofSeconds(10));
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        webDriver.manage().window().maximize();

        Assertions.assertDoesNotThrow(
                () -> webDriver.navigate().to("https://www.kupibilet.ru/"),
                "Страница не доступна"
        );
    }

    @Test
    public void shouldChangeLanguage() {
        String countrySelector = "header .styled__StyledLink-sc-109fpwc-4";
        String countriesDropdownSelector = "header div.styled__DropdownContent-sc-5ks2ov-1";
        String spanishSelector = "//div[contains(@class, 'styled__StyledSuggestion-sc-109fpwc-1')]/span[text()='ES']";
        String russianSelector = "//div[contains(@class, 'styled__StyledSuggestion-sc-109fpwc-1')]/span[text()='RU']";

        Assertions.assertDoesNotThrow(() -> webDriver.findElement(By.cssSelector(countrySelector)).click(), "Кнопка выбора языка не найдена");
        Assertions.assertDoesNotThrow(() -> webDriver.findElement(By.cssSelector(countriesDropdownSelector)), "Выбор языка не найден");

        Assertions.assertDoesNotThrow(() -> webDriver.findElement(By.xpath(spanishSelector)).click(), "Кнопка выбора Испанского языка не найдена");
        Assertions.assertDoesNotThrow(() -> webDriver.findElement(By.xpath("//header//a[text()='Vuelos']")), "Кнопка выбора Испанского языка не найдена");

        Assertions.assertDoesNotThrow(() -> webDriver.findElement(By.cssSelector(countrySelector)).click(), "Кнопка выбора языка не найдена");
        Assertions.assertDoesNotThrow(() -> webDriver.findElement(By.xpath(russianSelector)).click(), "Кнопка выбора Русского языка не найдена");
        Assertions.assertDoesNotThrow(() -> webDriver.findElement(By.xpath("//header//a[text()='Авиабилеты']")), "Кнопка выбора Русского языка не найдена");
    }

    @Test
    public void shouldRedirectToAppStore() {
        String appStoreSelector = ".styled__StyledAppStoreIcon-e8ikej-9 > .AppLinks__LinkText-eytozw-3";

        Assertions.assertDoesNotThrow(() -> {
            Actions actions = new Actions(webDriver);
            WebElement webElement = webDriver.findElement(By.cssSelector(appStoreSelector));
            actions.moveToElement(webElement).build().perform();
            webElement.click();
        }, "Ссылка на AppStore не найдена");

        List<String> windowHandles = new ArrayList(webDriver.getWindowHandles());
        Assertions.assertEquals(2, windowHandles.size(), "Не открылось новое окно");

        String secondTab = windowHandles.get(1);
        webDriver.switchTo().window(secondTab);
        Assertions.assertTrue(webDriver.getCurrentUrl().contains("apps.apple.com"), "Новое окно не перешло на apple");
    }

    @Test
    public void shouldSearchTickets() {
        String inputWrapper = "//div[@data-test='search-form-wrapper']";
        String fromToWrapper = "//div[contains(@class, 'styled__SwapGroupContainer-sc-1ar33t8-2')]";
        String fromInput = "(" + inputWrapper + fromToWrapper +  "//input)[1]";
        String toInput = "(" + inputWrapper + fromToWrapper +  "//input)[2]";
        String datesWrapper = "//div[contains(@class, 'ResponsiveWrappers__FlexBoxColumnOnMobile-cz63ln-2')]";
        String fromDateWrapper = "(" + inputWrapper + datesWrapper +  "//input)[1]";
        String nextMonthButton = "(//div[contains(@class, 'styled__NavbarButtons-sc-11ct9zd-3')]//button)[2]";
        String fromDate = "(//div[contains(@class, 'DayPicker-Day')]//span[text()='18'])[1]";
        String toDate = ".styled__HeaderPaddingWrapper-kn0ij9-1 .styled__StyledButtonText-sc-1c07g7v-1";
        String searchButton = "//button[@data-test='search-ticket-button']";

        Assertions.assertDoesNotThrow(() -> {
            Actions actions = new Actions(webDriver);
            actions.click(webDriver.findElement(By.xpath(fromInput)))
                    .sendKeys("Санкт-Петербург")
                    .pause(Duration.ofSeconds(1))
                    .build()
                    .perform();
        }, "Поле откуда не найдено");

        Assertions.assertDoesNotThrow(() -> {
            Actions actions = new Actions(webDriver);
            actions.click(webDriver.findElement(By.xpath(toInput)))
                    .sendKeys("Лиссабон")
                    .pause(Duration.ofSeconds(1))
                    .build()
                    .perform();
        }, "Поле куда не найдено");


        Assertions.assertDoesNotThrow(() -> {
            webDriver.findElement(By.xpath(fromDateWrapper)).click();
            webDriver.findElement(By.xpath(nextMonthButton)).click();
            webDriver.findElement(By.xpath(fromDate)).click();
            webDriver.findElement(By.cssSelector(toDate)).click();
        }, "Поле даты не найдено");

        Assertions.assertDoesNotThrow(() -> {
            Actions actions = new Actions(webDriver);
            actions.click(webDriver.findElement(By.xpath(searchButton)))
                    .pause(Duration.ofSeconds(10))
                    .build()
                    .perform();
        }, "Кнопки Найти не найдено");

        Assertions.assertTrue(webDriver.getCurrentUrl().contains("search"), "Не перешли на страницу поиска");
        Assertions.assertDoesNotThrow(() -> { webDriver.findElements(By.cssSelector(".styled__TicketContainer-bi8av4-0")); }, "Билеты не найдены");
    }

    @Test
    public void shouldWork() throws InterruptedException {
        String footerSpain = "//div[@id='footer']//a[@href='/countries/spain']";
        String titleSpain = "//h1[text()='Авиабилеты в Испанию']";
        String logo = "header nav a.styled__LogoLink-sc-9ogj7m-2";

        Assertions.assertDoesNotThrow(() -> {
            Actions actions = new Actions(webDriver);
            actions.moveToElement(webDriver.findElement(By.xpath(footerSpain)))
                    .click(webDriver.findElement(By.xpath(footerSpain)))
                    .build()
                    .perform();
        }, "Ссылка на 'Авиабилеты в Испанию' не найдена");
        Assertions.assertDoesNotThrow(() -> { webDriver.findElement(By.xpath(titleSpain)); }, "Не перешли на страницу Испании");

        Assertions.assertDoesNotThrow(() -> { webDriver.findElement(By.cssSelector(logo)).click(); }, "Ссылка на лого не найдена");
        Assertions.assertEquals("https://www.kupibilet.ru/", webDriver.getCurrentUrl(), "Не перешли на главную страницу");
    }
}
