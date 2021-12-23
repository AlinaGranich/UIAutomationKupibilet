package org.example;

import org.example.kupibilet.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
        MainPage mainPage = new MainPage(webDriver);

        Assertions.assertDoesNotThrow(mainPage::openCountrySelector, "Кнопка выбора языка не найдена");
        Assertions.assertDoesNotThrow(mainPage::getCountryDropdown, "Выбор языка не найден");

        Assertions.assertDoesNotThrow(mainPage::selectSpanish, "Кнопка выбора Испанского языка не найдена");
        Assertions.assertDoesNotThrow(() -> mainPage.airTicketsLinkByText("Vuelos"), "Не переключились на Испанский");

        Assertions.assertDoesNotThrow(mainPage::openCountrySelector, "Кнопка выбора языка не найдена");
        Assertions.assertDoesNotThrow(mainPage::selectRussian, "Кнопка выбора Русского языка не найдена");
        Assertions.assertDoesNotThrow(() -> mainPage.airTicketsLinkByText("Авиабилеты"), "Не переключились на Русский");
    }

    @Test
    public void shouldRedirectToAppStore() {
        MainPage mainPage = new MainPage(webDriver);

        Assertions.assertDoesNotThrow(mainPage::followAppStoreLink, "Ссылка на AppStore не найдена");

        List<String> windowHandles = new ArrayList(webDriver.getWindowHandles());
        Assertions.assertEquals(2, windowHandles.size(), "Не открылось новое окно");

        String secondTab = windowHandles.get(1);
        webDriver.switchTo().window(secondTab);
        Assertions.assertTrue(webDriver.getCurrentUrl().contains("apps.apple.com"), "Новое окно не перешло на apple");
    }

    @Test
    public void shouldSearchTickets() {
        MainPage mainPage = new MainPage(webDriver);

        Assertions.assertDoesNotThrow(() -> { mainPage.fillInFrom("Санкт-Петербург"); }, "Поле откуда не найдено");
        Assertions.assertDoesNotThrow(() -> { mainPage.fillInTo("Лиссабон"); }, "Поле куда не найдено");
        Assertions.assertDoesNotThrow(mainPage::fillInDates, "Поле даты не найдено");
        Assertions.assertDoesNotThrow(mainPage::search, "Кнопки Найти не найдено");
        Assertions.assertTrue(webDriver.getCurrentUrl().contains("search"), "Не перешли на страницу поиска");
        Assertions.assertDoesNotThrow(() -> { webDriver.findElements(By.cssSelector(".styled__TicketContainer-bi8av4-0")); }, "Билеты не найдены");
    }

    @Test
    public void shouldWork() throws InterruptedException {
        MainPage mainPage = new MainPage(webDriver);
        Assertions.assertDoesNotThrow(mainPage::redirectSpainPage, "Ссылка на 'Авиабилеты в Испанию' не найдена");
        Assertions.assertDoesNotThrow((Executable) mainPage::getTitleSpain, "Не перешли на страницу Испании");

        Assertions.assertDoesNotThrow(mainPage::clickLogo, "Ссылка на лого не найдена");
        Assertions.assertEquals("https://www.kupibilet.ru/", webDriver.getCurrentUrl(), "Не перешли на главную страницу");
    }
}
