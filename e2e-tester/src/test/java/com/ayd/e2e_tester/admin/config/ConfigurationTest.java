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
    void testChangeEntrepriseNameSuccess() {

        navigateToWindowToTest();
        // arrange
        WebElement txtName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("txtName")));
        WebElement btnCambiarNombre = driver.findElement(
                By.name("btnSend"));

        // act
        txtName.clear();
        txtName.sendKeys("Mi Empresa Test");
        btnCambiarNombre.click();

        // assert
        // busca en todos los elementos de la pagina con //* y evalua el texto
        WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.name("toastr")));

        assertTrue(toast.isDisplayed());
    }

    @Test
    void testChangeNitSuccess() {

        navigateToWindowToTest();
        // arrange
        WebElement txtNit = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("txtNit")));
        WebElement btnSendNit = driver.findElement(
                By.name("btnSendNit"));

        // act
        txtNit.clear();
        txtNit.sendKeys("12345");
        btnSendNit.click();

        // assert
        // busca en todos los elementos de la pagina con //* y evalua el texto
        WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.name("toastr")));

        assertTrue(toast.isDisplayed());
    }

    @Test
    void testChangeNitRegime() {

        navigateToWindowToTest();

        // arrange
        WebElement opcionPequeno = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("txtRegime")));

        opcionPequeno.click();

        WebElement listaOpciones = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.p-dropdown-items")));

        WebElement opcion = listaOpciones.findElements(By.tagName("li")).get(1);
        opcion.click();

        WebElement btnSendRegime = driver.findElement(
                By.name("btnSendRegime"));

        // act
        btnSendRegime.click();

        // assert
        // busca en todos los elementos de la pagina con //* y evalua el texto
        WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.name("toastr")));

        assertTrue(toast.isDisplayed());
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
