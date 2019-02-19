package com.linkedin.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RemoveAndBlockConnections {
    WebDriver driver;
    Properties prop;
    InputStream input;
    WebDriverWait wait;

    @Test
    public void RemoveAndBlockConnectionsTest() throws IOException, InterruptedException {
            while (true) {
             Thread.sleep(10000);
                WebDriverManager.chromedriver().setup();
                prop = new Properties();
                input = null;
                driver = new ChromeDriver();
                driver.get("https://in.linkedin.com/");
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                wait = new WebDriverWait(driver, 20);
                String filename = "src/test/java/com/linkedin/tests/Linkedin.properties";
                input = new FileInputStream(filename);
                prop.load(input);
                driver.findElement(By.id("login-email")).sendKeys(prop.getProperty("email"));
                driver.findElement(By.id("login-password")).sendKeys(prop.getProperty("password"));
                driver.findElement(By.id("login-submit")).click();
            driver.get("https://www.linkedin.com/search/results/people/?facetGeoRegion=%5B%22in%3A6831%22%2C%22in%3A6477%22%2C%22in%3A6560%22%5D&facetNetwork=%5B%22F%22%5D&origin=FACETED_SEARCH");
            driver.findElement(By.xpath("//*[@data-control-name='search_srp_result']")).sendKeys(Keys.PAGE_DOWN);
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@data-control-name='search_srp_result']")).sendKeys(Keys.PAGE_DOWN);
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@data-control-name='search_srp_result']")).sendKeys(Keys.PAGE_DOWN);
            Thread.sleep(1000);
            List<WebElement> elements = driver.findElements(By.xpath("//*[@data-control-name='search_srp_result']"));
            String[] profileUrls = new String[elements.size()*2];
            int i = 0;
            for (WebElement element : elements) {
                profileUrls[i] = element.getAttribute("href");
                i++;
            }
           /* List<String> list = Arrays.asList(profileUrls);
            Set<String> set = new HashSet<String>(list);
            set.toArray(profileUrls);*/
            String curUrl = "test";
            for (String profileUrl : profileUrls) {
                try {
                    if (!profileUrl.equals(curUrl)) {
                        curUrl = profileUrl;
                        if (!profileUrl.contains("shinu")) {
                            driver.get(profileUrl);
                            Thread.sleep(5000);
                            driver.findElement(By.xpath("//*[text()='More…']")).click();
                            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Report / Block']"))));
                            driver.findElement(By.xpath("//*[text()='Report / Block']")).click();
                            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@class='ember-semaphore__menu']/li/div/div/button"))));
                            driver.findElement(By.xpath("//*[@class='ember-semaphore__menu']/li/div/div/button")).click();
                            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[@role='submitRadio']"))));
                            driver.findElement(By.xpath("//button[@role='submitRadio']")).click();
                            wait.until(ExpectedConditions.urlToBe("https://www.linkedin.com/feed/"));
                        }
                    }
                }
                catch(Exception e){
                    break;
                }
            }
            driver.quit();
        }
    }
}