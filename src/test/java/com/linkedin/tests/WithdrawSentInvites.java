package com.linkedin.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WithdrawSentInvites {

    WebDriver driver;
    Properties prop;
    InputStream input;
    WebDriverWait wait;

    @Test
    public void WithdrawSentInvitesTest() throws IOException, InterruptedException {

        while (true) {
            WebDriverManager.chromedriver().setup();
            prop = new Properties();
            input = null;
            driver = new ChromeDriver();
            driver.get("https://in.linkedin.com/");
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            wait = new WebDriverWait(driver, 10);
            String filename = "src/test/java/com/linkedin/tests/Linkedin.properties";
            input = new FileInputStream(filename);
            prop.load(input);
            driver.findElement(By.id("login-email")).sendKeys(prop.getProperty("email"));
            driver.findElement(By.id("login-password")).sendKeys(prop.getProperty("password"));
            driver.findElement(By.id("login-submit")).click();
            while (true) {
                driver.get("https://www.linkedin.com/mynetwork/invitation-manager/sent/");
                driver.findElement(By.xpath("//label[@for='contact-select-checkbox']")).click();
                wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[@class='button-primary-medium']"))));
                driver.findElement(By.xpath("//button[@class='button-primary-medium']")).click();
            }
        }
    }
}