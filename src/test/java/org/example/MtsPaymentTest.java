package org.example;

import org.example.pages.PaymentPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MtsPaymentTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private DriverManager driverManager;
    private PaymentPage paymentPage;

    // Константы тестовых данных (CAPS_LOCK)
    private static final String BASE_URL = "https://mts.by";
    private static final String EXPECTED_DETAILS_URL = "poryadok-oplaty-i-bezopasnost-internet-platezhey";
    private static final String TEST_PHONE = "297777777";
    private static final String TEST_SUM = "10";

    @BeforeEach
    public void setUp() {
        driverManager = new DriverManager();
        driver = driverManager.initDriver();
        wait = driverManager.getWait();
        paymentPage = new PaymentPage(driver);

        driver.get(BASE_URL);
        paymentPage.handleCookieBanner();
    }

    @Test
    public void test1_CheckBlockTitle() {
        WebElement blockTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.BLOCK_TITLE));
        Assertions.assertTrue(blockTitle.isDisplayed(), "Блок оплаты не отображается на странице");

        String actualTitleText = blockTitle.getText().replace("\n", " ").trim();
        Assertions.assertEquals("Онлайн пополнение без комиссии", actualTitleText, "Название блока не совпадает с ожидаемым");
    }

    @Test
    public void test2_CheckPaymentLogosPresence() {
        WebElement logosContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.LOGOS_CONTAINER));
        Assertions.assertTrue(logosContainer.isDisplayed(), "Контейнер платежных систем скрыт");

        List<WebElement> logos = driver.findElements(PaymentPage.LOGOS_IMAGES);
        Assertions.assertFalse(logos.isEmpty(), "Список логотипов платежных систем пуст");
    }

    @Test
    public void test3_CheckDetailsLinkWorking() {
        paymentPage.clickDetailsLink();

        wait.until(ExpectedConditions.urlContains(EXPECTED_DETAILS_URL));
        Assertions.assertTrue(driver.getCurrentUrl().contains(EXPECTED_DETAILS_URL), "Переход по ссылке 'Подробнее о сервисе' не выполнен");
    }

    @Test
    public void test4_CheckSubmitFormCommunicationServices() {
        paymentPage.fillForm(TEST_PHONE, TEST_SUM);

        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.SUBMIT_BUTTON));
        Assertions.assertTrue(submitButton.isDisplayed(), "Кнопка 'Продолжить' отсутствует на форме");

        paymentPage.clickContinue();

        // Проверяем, что произошел уход с главной страницы на эквайринг оплаты
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(BASE_URL)));
        Assertions.assertNotEquals(BASE_URL, driver.getCurrentUrl(), "Форма не отправилась, перехода на страницу оплаты не произошло");
    }

    @AfterEach
    public void tearDown() {
        if (driverManager != null) {
            driverManager.quitDriver();
        }
    }
}