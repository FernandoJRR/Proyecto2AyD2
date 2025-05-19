package com.ayd.e2e_tester.admin.empleados;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;

import com.ayd.e2e_tester.BaseE2ETest;

@SpringBootTest
public class EmployeesPageTest extends BaseE2ETest {

    @Test
    void testGetEmployees() {
        navigateToWindowToTest();

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("tableEmployees")));

        List<WebElement> rows = table.findElements(By.tagName("tr"));
        assertTrue(rows.size() > 0);
    }

    @Override
    public void navigateToWindowToTest() {
        WebElement adminBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/admin')]")));
        adminBtn.click();

        WebElement configBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/personal')]")));
        configBtn.click();

    }

}
