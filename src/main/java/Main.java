
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("start-maximized");
        System.setProperty(
                "webdriver.chrome.driver", "src/main/resources/chromedriver"
        );
        WebDriver driver = new ChromeDriver();

        driver.navigate().to("https://www.kupibilet.ru/");
        Thread.sleep(5000);
        WebElement webElement = driver.findElement(By.cssSelector(".styled__StyledLink-sc-109fpwc-4"));
        webElement.click();

        webElement = driver.findElement(By.xpath("//div[@class=\"styled__StyledSuggestion-sc-109fpwc-1 fkxixj\"]/span[text()=\"ES\"]"));
        webElement.click();

        webElement = driver.findElement(By.cssSelector(".styled__StyledLink-sc-109fpwc-4"));
        webElement.click();

        webElement = driver.findElement(By.xpath("//div[@class=\"styled__StyledSuggestion-sc-109fpwc-1 fkxixj\"]/span[text()=\"RU\"]"));
        webElement.click();


        webElement = driver.findElement(By.cssSelector(".styled__StyledAppStoreIcon-e8ikej-9 > .AppLinks__LinkText-eytozw-3"));
        webElement.click();


        Thread.sleep(5000);
        driver.quit();
    }
}
