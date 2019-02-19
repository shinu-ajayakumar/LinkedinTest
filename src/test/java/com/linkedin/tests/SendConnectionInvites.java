package com.linkedin.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SendConnectionInvites {
    WebDriver driver;
    Properties prop;
    InputStream input;
    WebDriverWait wait;
    Actions actions;
    JavascriptExecutor jse;
    private String searchUrl = "https://www.linkedin.com/search/results/people/?facetGeoRegion=%5B%22es%3A0%22%2C%22nl%3A0%22%2C%22ie%3A0%22%2C%22gb%3A0%22%2C%22pl%3A0%22%2C%22de%3A0%22%2C%22hu%3A0%22%2C%22ro%3A0%22%2C%22nz%3A0%22%5D&facetNetwork=%5B%22S%22%2C%22O%22%5D&keywords=appium&origin=FACETED_SEARCH";

    @Test
    public void SendConnectionInvitesTest() throws IOException, InterruptedException {
        WebDriverManager.chromedriver().setup();
        prop = new Properties();
        input = null;
        driver = new ChromeDriver();
        driver.get("https://in.linkedin.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 5);
        actions = new Actions(driver);
        jse = (JavascriptExecutor) driver;
        String filename = "src/test/java/com/linkedin/tests/Linkedin.properties";
        input = new FileInputStream(filename);
        prop.load(input);
        driver.findElement(By.id("login-email")).sendKeys(prop.getProperty("email"));
        driver.findElement(By.id("login-password")).sendKeys(prop.getProperty("password"));
        driver.findElement(By.id("login-submit")).click();
        driver.get(searchUrl);
        Thread.sleep(2000);
        while (true) {
            pressButton(driver.findElement(By.xpath("(//input[@placeholder='Search'])[1]")), Keys.PAGE_DOWN, 3);
            pressButton(driver.findElement(By.xpath("(//input[@placeholder='Search'])[1]")), Keys.PAGE_UP, 2);
            List<WebElement> connectButtons = driver.findElements(By.xpath("//button[text()='Connect']"));
            if (connectButtons.size() == 0) {
                jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                jse.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[text()='Next']")));
                Thread.sleep(2000);
                pressButton(driver.findElement(By.xpath("(//input[@placeholder='Search'])[1]")), Keys.PAGE_DOWN, 3);
                pressButton(driver.findElement(By.xpath("(//input[@placeholder='Search'])[1]")), Keys.PAGE_UP, 2);
                connectButtons = driver.findElements(By.xpath("//button[text()='Connect']"));
            }
            for (WebElement connectButton : connectButtons) {
                String userName = connectButton.findElement(By.xpath("ancestor::*[2]/preceding-sibling::div/a/h3/span/span/span")).getText();
                String currentUrl = driver.getCurrentUrl();
                jse.executeScript("arguments[0].scrollIntoView(true);", connectButton);
                jse.executeScript("arguments[0].click();", connectButton);
                Thread.sleep(1000);
                try {
                    driver.findElement(By.xpath("//input[@class='mb3 ember-text-field ember-view']")).isDisplayed();
                    driver.findElement(By.xpath("//span[text()='Close']/following-sibling::li-icon/child::*")).click();
                } catch (Exception e) {
                    try {
                        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[text()='Send now']"))));
                        Thread.sleep(1000);
                        driver.findElement(By.xpath("//button[text()='Send now']")).click();
                        Thread.sleep(1000);
                        System.out.print(userName + " : Invitation Sent");
                    } catch (Exception e2) {
                        driver.get(currentUrl);
                        pressButton(driver.findElement(By.xpath("(//input[@placeholder='Search'])[1]")), Keys.PAGE_DOWN, 3);
                        pressButton(driver.findElement(By.xpath("(//input[@placeholder='Search'])[1]")), Keys.PAGE_UP, 2);
                    }
                }
            }
            Thread.sleep(1000);
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            jse.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[text()='Next']")));
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[text()='Next']"))));
        }
    }

    private void pressButton(WebElement element, Keys keyName, int i) throws InterruptedException {
        for (int j = 0; j <= i; j++) {
            element.sendKeys(keyName);
            element.sendKeys(Keys.TAB);
            Thread.sleep(500);
        }
    }
}