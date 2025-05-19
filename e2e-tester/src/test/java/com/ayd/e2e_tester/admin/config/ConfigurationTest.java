package com.ayd.e2e_tester.admin.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;

import com.ayd.e2e_tester.BaseE2ETest;

@SpringBootTest
public class ConfigurationTest extends BaseE2ETest {

    @Test
    void testCambiarNombreEmpresa() {

        navigateToWindowToTest();
        // arrange
        WebElement txtName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("txtName")));
        WebElement btnCambiarNombre = driver.findElement(
                By.name("btnSend"));

        // act
        txtName.sendKeys("Mi Empresa Test");
        btnCambiarNombre.click();

        // // assert
        // // busca en todos los elementos de la pagina con //* y evalua el texto
        // WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(
        //         By.xpath("//*[contains(text(),'actualizado') or contains(text(),'Se han actualizado')]")));

        // assertTrue(toast.isDisplayed());
    }

    @Override
    public void navigateToWindowToTest() {
        WebElement adminBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/admin')]")));
        adminBtn.click();

        WebElement configBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/configuracion')]")));
        configBtn.click();

    }

}
