package com.ayd.e2e_tester;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.ayd.shared.security.AppProperties;

public abstract class BaseE2ETest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public static final Long TIME_TO_WAIT = 10l;

    @Autowired
    protected AppProperties appProperties;


    public abstract void navigateToWindowToTest();

    @BeforeEach
    void setUpBase() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIME_TO_WAIT));

        login();
    }

    @AfterEach
    void tearDownBase() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void login() {
        driver.get(appProperties.getFrontURL() + "/app/login");

        WebElement txtUsername = driver.findElement(By.name("username"));
        WebElement txtPassword = driver.findElement(By.name("password"));
        WebElement btnLogin = driver.findElement(By.name("btnSend"));

        txtUsername.sendKeys("admin");
        txtPassword.sendKeys("admin");
        btnLogin.click();

        wait.until(ExpectedConditions.urlContains("/app/"));
    }

}
