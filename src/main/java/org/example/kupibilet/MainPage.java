package org.example.kupibilet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class MainPage extends AbstractPage {

    @FindBy(css = "header .styled__StyledLink-sc-109fpwc-4")
    private WebElement countrySelector;

    @FindBy(css = "header div.styled__DropdownContent-sc-5ks2ov-1")
    private WebElement countryDropdown;

    @FindBy(xpath = "//div[contains(@class, 'styled__StyledSuggestion-sc-109fpwc-1')]/span[text()='ES']")
    private WebElement spanishLanguage;

    @FindBy(xpath = "//div[contains(@class, 'styled__StyledSuggestion-sc-109fpwc-1')]/span[text()='RU']")
    private WebElement russianLanguage;

    @FindBy(css = ".styled__StyledAppStoreIcon-e8ikej-9 > .AppLinks__LinkText-eytozw-3")
    private WebElement appStoreLink;

    @FindBy(xpath = "(//div[@data-test='search-form-wrapper']//div[contains(@class, 'styled__SwapGroupContainer-sc-1ar33t8-2')]//input)[1]")
    private WebElement directionFromInput;

    @FindBy(xpath = "(//div[@data-test='search-form-wrapper']//div[contains(@class, 'styled__SwapGroupContainer-sc-1ar33t8-2')]//input)[2]")
    private WebElement directionToInput;

    @FindBy(xpath = "(//div[@data-test='search-form-wrapper']//div[contains(@class, 'ResponsiveWrappers__FlexBoxColumnOnMobile-cz63ln-2')]//input)[1]")
    private WebElement inputFromDate;

    @FindBy(xpath = "(//div[contains(@class, 'styled__NavbarButtons-sc-11ct9zd-3')]//button)[2]")
    private WebElement datesDropdownNextMonth;

    @FindBy(xpath = "(//div[contains(@class, 'DayPicker-Day')]//span[text()='18'])[1]")
    private WebElement fromDateButton;

    @FindBy(css = ".styled__HeaderPaddingWrapper-kn0ij9-1 .styled__StyledButtonText-sc-1c07g7v-1")
    private WebElement noTicketBackButton;

    @FindBy(xpath = "//button[@data-test='search-ticket-button']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@id='footer']//a[@href='/countries/spain']")
    private WebElement footerSpain;

    @FindBy(xpath = "//h1[text()='Авиабилеты в Испанию']")
    private WebElement titleSpain;

    @FindBy(css = "header nav a.styled__LogoLink-sc-9ogj7m-2")
    private WebElement logo;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void fillInFrom(String text) {
        Actions actions = new Actions(this.getDriver());
        actions.click(this.directionFromInput)
                .sendKeys(text)
                .pause(Duration.ofSeconds(1))
                .build()
                .perform();
    }

    public void fillInTo(String text) {
        Actions actions = new Actions(this.getDriver());
        actions.click(this.directionToInput)
                .sendKeys(text)
                .pause(Duration.ofSeconds(1))
                .build()
                .perform();
    }

    public void fillInDates() {
        this.inputFromDate.click();
        this.datesDropdownNextMonth.click();
        this.fromDateButton.click();
        this.noTicketBackButton.click();
    }

    public void search() {
        Actions actions = new Actions(this.getDriver());
        actions.click(this.searchButton)
                .pause(Duration.ofSeconds(10))
                .build()
                .perform();
    }

    public void followAppStoreLink() {
        Actions actions = new Actions(this.getDriver());
        actions.moveToElement(this.appStoreLink)
                .pause(Duration.ofSeconds(1))
                .click()
                .build()
                .perform();
    }

    public WebElement getTitleSpain() {
        return this.titleSpain;
    }

    public void clickLogo() {
        this.logo.click();
    }

    public void redirectSpainPage() {
        Actions actions = new Actions(this.getDriver());
        actions.moveToElement(this.footerSpain)
                .click(this.footerSpain)
                .build()
                .perform();
    }

    public WebElement airTicketsLinkByText(String text) {
        return this.getDriver().findElement(By.xpath("//header//a[text()='" + text + "']"));
    }

    public void selectSpanish() {
        this.spanishLanguage.click();
    }

    public void selectRussian() {
        this.russianLanguage.click();
    }

    public void openCountrySelector() {
        this.countrySelector.click();
    }

    public WebElement getCountryDropdown() {
        return this.countryDropdown;
    }
}
