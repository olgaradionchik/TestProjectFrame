package org.example;
import io.qameta.allure.Step;
import org.example.pages.PaymentPage;
import org.junit.jupiter.api.*;
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
    //Константы тестовых данных
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
    @Order(1)
    public void test1_CheckBlockTitle() {
        WebElement blockTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.BLOCK_TITLE));
        Assertions.assertTrue(blockTitle.isDisplayed(), "Блок оплаты не отображается на странице");

        String actualTitleText = blockTitle.getText().replace("\n", " ").trim();
        Assertions.assertEquals("Онлайн пополнение без комиссии", actualTitleText, "Название блока не совпадает с ожидаемым");
    }
    @Test
    @Order(2)
    public void test2_CheckPaymentLogosPresence() {
        WebElement logosContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.LOGOS_CONTAINER));
        Assertions.assertTrue(logosContainer.isDisplayed(), "Контейнер платежных систем скрыт");
        List<WebElement> logos = driver.findElements(PaymentPage.LOGOS_IMAGES);
        Assertions.assertFalse(logos.isEmpty(), "Список логотипов платежных систем пуст");
    }
    @Test
    @Order(3)
    public void test3_CheckDetailsLinkWorking() {
        paymentPage.clickDetailsLink();
        wait.until(ExpectedConditions.urlContains(EXPECTED_DETAILS_URL));
        Assertions.assertTrue(driver.getCurrentUrl().contains(EXPECTED_DETAILS_URL), "Переход по ссылке 'Подробнее о сервисе' не выполнен");
    }
    @Test
    @Order(4)
    public void test4_CheckSubmitFormCommunicationServices() {
        paymentPage.fillForm(TEST_PHONE, TEST_SUM);
        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.SUBMIT_BUTTON));
        Assertions.assertTrue(submitButton.isDisplayed(), "Кнопка 'Продолжить' отсутствует на форме");
        paymentPage.clickContinue();
        // Проверяем, что произошел уход с главной страницы на эквайринг оплаты
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(BASE_URL)));
        Assertions.assertNotEquals(BASE_URL, driver.getCurrentUrl(), "Форма не отправилась, перехода на страницу оплаты не произошло");
    }
    @Test
    @Order(5)
    public void test5_VerifyPlaceholdersInAllTabs() {
        //Проверка вкладки «Услуги связи»
        paymentPage.selectTab(PaymentPage.TAB_CONNECTION);
        String phoneConn = paymentPage.getPlaceholder(PaymentPage.INPUT_PHONE_CONNECTION);
        String sumConn = paymentPage.getPlaceholder(PaymentPage.INPUT_SUM_CONNECTION);
        Assertions.assertEquals("Номер телефона", phoneConn, "Плейсхолдер телефона 'Услуги связи' не совпадает");
        Assertions.assertEquals("Сумма", sumConn, "Плейсхолдер суммы 'Услуги связи' не совпадает");
        //Проверка вкладки «Домашний интернет»
        paymentPage.selectTab(PaymentPage.TAB_INTERNET);
        String phoneInt = paymentPage.getPlaceholder(PaymentPage.INPUT_PHONE_INTERNET);
        String sumInt = paymentPage.getPlaceholder(PaymentPage.INPUT_SUM_INTERNET);
        Assertions.assertEquals("Номер абонента", phoneInt, "Плейсхолдер телефона 'Домашний интернет' не совпадает");
        Assertions.assertEquals("Сумма", sumInt, "Плейсхолдер суммы 'Домашний интернет' не совпадает");
        //Проверка вкладки «Рассрочка»
        paymentPage.selectTab(PaymentPage.TAB_INSTALLMENT);
        String scoreInst = paymentPage.getPlaceholder(PaymentPage.INPUT_SCORE_INSTALLMENT);
        String sumInst = paymentPage.getPlaceholder(PaymentPage.INPUT_SUM_INSTALLMENT);
        Assertions.assertEquals("Номер счета на 44", scoreInst, "Плейсхолдер счета 'Рассрочка' не совпадает");
        Assertions.assertEquals("Сумма", sumInst, "Плейсхолдер суммы 'Рассрочка' не совпадает");
        //Проверка вкладки «Задолженность»
        paymentPage.selectTab(PaymentPage.TAB_DEBT);
        String scoreDebt = paymentPage.getPlaceholder(PaymentPage.INPUT_SCORE_DEBT);
        String sumDebt = paymentPage.getPlaceholder(PaymentPage.INPUT_SUM_DEBT);
        Assertions.assertEquals("Номер счета на 2073", scoreDebt, "Плейсхолдер счета 'Задолженность' не совпадает");
        Assertions.assertEquals("Сумма", sumDebt, "Плейсхолдер суммы 'Задолженность' не совпадает");
    }
    @Test
    @Order(6)
    public void test6_VerifyInvoiceAndCardFormDetailsInsideIframe() {
        //Выбираем вкладку «Услуги связи» и заполняем форму из пререквизитов теста 4
        paymentPage.selectTab(PaymentPage.TAB_CONNECTION);
        paymentPage.fillForm(TEST_PHONE, TEST_SUM); // Использует константы TEST_PHONE ("297777777") и TEST_SUM ("10")
        paymentPage.clickContinue();
        //Переключаем контекст Selenium внутрь подгружаемого платежного iframe bePaid
        paymentPage.switchToPaymentIframe();
        //Проверяем корректность отображения суммы и номера телефона в появившемся окне счета
        WebElement amountInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.ORDER_AMOUNT_TEXT));
        WebElement phoneInfo = driver.findElement(PaymentPage.ORDER_PHONE_TEXT);
        //Очищаем фактический текст от пробелов, скобок и тире для точного сравнения номера телефона
        String cleanPhoneText = phoneInfo.getText().replaceAll("[^0-9]", "");
        Assertions.assertTrue(amountInfo.getText().contains(TEST_SUM), "Сумма в счете шлюза не совпадает с введенной");
        Assertions.assertTrue(cleanPhoneText.contains(TEST_PHONE), "Номер телефона в счете шлюза не совпадает с введенным");
        //Ожидаем появление кнопки оплаты на экране шлюза bePaid
        WebElement payBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(PaymentPage.PAY_BUTTON));
        String buttonText = payBtn.getText().trim();
        System.out.println("Текст найденной кнопки оплаты шлюза: '" + buttonText + "'");
        //Очищаем текст от букв, оставляя только цифры
        String cleanPayBtnText = buttonText.replaceAll("[^0-9]", "");
        //Интеллектуальный ассерт: если на кнопке есть цифры — проверяем сумму, если только текст ("Оплатить") — проверяем её видимость
        if (!cleanPayBtnText.isEmpty()) {
            Assertions.assertTrue(cleanPayBtnText.contains(TEST_SUM),
                    "Сумма на кнопке оплаты '" + buttonText + "' не содержит тестовую сумму '" + TEST_SUM + "'");
        } else {
            Assertions.assertTrue(payBtn.isDisplayed(), "Кнопка совершения платежа отсутствует или скрыта на форме");
        }
        //Проверяем наличие надписей в незаполненных полях для ввода реквизитов карты
        Assertions.assertTrue(driver.findElement(PaymentPage.CARD_NUMBER_LABEL).isDisplayed(), "Подпись поля номера карты скрыта или отсутствует");
        Assertions.assertTrue(driver.findElement(PaymentPage.CARD_EXPIRY_LABEL).isDisplayed(), "Подпись поля срока действия скрыта или отсутствует");
        Assertions.assertTrue(driver.findElement(PaymentPage.CARD_CVC_LABEL).isDisplayed(), "Подпись поля CVC скрыта или отсутствует");
        Assertions.assertTrue(driver.findElement(PaymentPage.CARD_HOLDER_LABEL).isDisplayed(), "Подпись поля имени держателя скрыта или отсутствует");
        //Проверяем наличие иконок платёжных систем в окне ввода карты
        List<WebElement> systemLogos = driver.findElements(PaymentPage.PAYMENT_SYSTEMS_LOGOS);
        Assertions.assertFalse(systemLogos.isEmpty(), "Иконки платежных систем банка-эквайера отсутствуют в iframe");
    }
    @AfterEach
    public void tearDown() {
        if (driverManager != null) {
            driverManager.quitDriver();
        }
    }
}