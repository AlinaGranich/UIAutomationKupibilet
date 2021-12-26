package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.example.kupibilet.KpbEventListener;
import org.example.kupibilet.MainPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppTest
{
    static WebDriver webDriver;
    static EventFiringWebDriver eventDriver;

    @BeforeAll
    static void setupChromeDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @AfterEach
    void exit() {
        webDriver.quit();
    }

    @BeforeEach
    void initMainPage() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("start-maximized");
        options.setPageLoadTimeout(Duration.ofSeconds(10));
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        webDriver.manage().window().maximize();

        eventDriver = new EventFiringWebDriver(webDriver);
        eventDriver.register(new KpbEventListener());

        Assertions.assertDoesNotThrow(
                () -> this.getWebDriver(true).navigate().to("https://www.kupibilet.ru/"),
                "Страница не доступна"
        );
    }

    @Test
    @Epic("Мультиязычность")
    @Description("Возможность смены языка с Русского на Испанский и наоборот")
    @Severity(SeverityLevel.NORMAL)
    public void shouldChangeLanguage() {
        MainPage mainPage = new MainPage(this.getWebDriver(true));

        Assertions.assertDoesNotThrow(mainPage::openCountrySelector);
        Assertions.assertDoesNotThrow(mainPage::getCountryDropdown, "Выбор языка не найден");

        Assertions.assertDoesNotThrow(mainPage::selectSpanish, "Кнопка выбора Испанского языка не найдена");
        Assertions.assertDoesNotThrow(() -> mainPage.airTicketsLinkByText("Vuelos"), "Не переключились на Испанский");

        Assertions.assertDoesNotThrow(mainPage::openCountrySelector, "Кнопка выбора языка не найдена");
        Assertions.assertDoesNotThrow(mainPage::selectRussian, "Кнопка выбора Русского языка не найдена");
        Assertions.assertDoesNotThrow(() -> mainPage.airTicketsLinkByText("Авиабилеты"), "Не переключились на Русский");
    }

    @Test
    @Epic("Мобильные приложения")
    @Description("Ссылки на мобильные приложения ведут на соотвествующие магазины")
    @Severity(SeverityLevel.NORMAL)
    public void shouldRedirectToAppStore() {
        MainPage mainPage = new MainPage(this.getWebDriver(false));

        Assertions.assertDoesNotThrow(mainPage::followAppStoreLink, "Ссылка на AppStore не найдена");

        List<String> windowHandles = new ArrayList(this.getWebDriver(false).getWindowHandles());
        Assertions.assertEquals(2, windowHandles.size(), "Не открылось новое окно");

        String secondTab = windowHandles.get(1);
        this.getWebDriver(false).switchTo().window(secondTab);
        Assertions.assertTrue(this.getWebDriver(false).getCurrentUrl().contains("apps.apple.com"), "Новое окно не перешло на apple");
    }

    @Test
    @Epic("Поиск билета")
    @Description("При заполнении формы поиска система ищет нужные билеты")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldSearchTickets() {
        MainPage mainPage = new MainPage(this.getWebDriver(true));

        Assertions.assertDoesNotThrow(() -> { mainPage.fillInFrom("Санкт-Петербург"); }, "Поле откуда не найдено");
        Assertions.assertDoesNotThrow(() -> { mainPage.fillInTo("Лиссабон"); }, "Поле куда не найдено");
        Assertions.assertDoesNotThrow(mainPage::fillInFromDate);
        Assertions.assertDoesNotThrow(mainPage::fillInNoTicketBack);
        Assertions.assertDoesNotThrow(mainPage::search, "Кнопки Найти не найдено");
        Assertions.assertTrue(this.getWebDriver(false).getCurrentUrl().contains("search"), "Не перешли на страницу поиска");
        Assertions.assertDoesNotThrow(() -> { this.getWebDriver(true).findElements(By.cssSelector(".styled__TicketContainer-bi8av4-0")); }, "Билеты не найдены");
    }

    @Test
    @Epic("Основные элементы управления")
    @Description("Логотип в хедере выступает ссылкой на главную страницу сайта")
    @Severity(SeverityLevel.MINOR)
    public void shouldRedirectToMainPageOnLogo() {
        MainPage mainPage = new MainPage(this.getWebDriver(false));
        Assertions.assertDoesNotThrow(mainPage::redirectSpainPage, "Ссылка на 'Авиабилеты в Испанию' не найдена");
        Assertions.assertDoesNotThrow((Executable) mainPage::getTitleSpain, "Не перешли на страницу Испании");

        Assertions.assertDoesNotThrow(mainPage::clickLogo, "Ссылка на лого не найдена");
        Assertions.assertEquals("https://www.kupibilet.ru/", this.getWebDriver(false).getCurrentUrl(), "Не перешли на главную страницу");
    }

    public WebDriver getWebDriver(boolean stale) {
        if (stale) {
            return webDriver;
        }

        return eventDriver;
    }
}
