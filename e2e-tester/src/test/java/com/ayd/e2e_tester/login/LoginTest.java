package com.ayd.e2e_tester.login;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ayd.shared.security.AppProperties;

@SpringBootTest
public class LoginTest {

    @Autowired
    private AppProperties appProperties;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        driver.get(appProperties.getFrontURL() + "/app/login");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLoginSuccess() {

        // arrange

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement txtUsername = driver.findElement(By.name("username"));
        WebElement txtPassword = driver.findElement(By.name("password"));
        WebElement btnSend = driver.findElement(By.name("btnSend"));

        // act
        txtUsername.sendKeys("admin");
        txtPassword.sendKeys("admin");
        btnSend.click();

        // assert
        wait.until(ExpectedConditions.urlContains("/app/"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/app/"));

    }

    @Test
    void testLoginFail() {

        // arrange
        driver.get(appProperties.getFrontURL() + "/app/login");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement txtUsername = driver.findElement(By.name("username"));
        WebElement txtPassword = driver.findElement(By.name("password"));
        WebElement btnSend = driver.findElement(By.name("btnSend"));

        // act
        txtUsername.sendKeys("falso");
        txtPassword.sendKeys("falso");
        btnSend.click();

        // assert
        wait.until(ExpectedConditions.urlContains("login"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login"));

    }

}
