import org.example.DriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
public class MtsPaymentTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private DriverManager driverManager;
    private final String BASE_URL = "https://mts.by";
    private final String EXPECTED_DETAILS_URL = "poryadok-oplaty-i-bezopasnost-internet-platezhey";
    //Локаторы
    private final By cookieBtnLocator = By.xpath("//button[contains(@class, 'cookie__btn') or contains(text(), 'Принять') or contains(text(), 'Согласен')]");
    private final By blockTitleLocator = By.xpath("//h2[normalize-space()='Онлайн пополнение без комиссии']");
    private final By logosContainerLocator = By.className("pay__partners");
    private final By logosImagesLocator = By.cssSelector(".pay__partners img");
    private final By detailsLinkLocator = By.xpath("//a[contains(text(), 'Подробнее о сервисе')]");
    private final By phoneInputLocator = By.id("connection-phone");
    private final By sumInputLocator = By.id("connection-sum");
    private final By submitButtonLocator = By.xpath("//button[normalize-space()='Продолжить']");
    @BeforeEach
    public void setUp() {
        driverManager = new DriverManager();
        driver = driverManager.initDriver();
        wait = driverManager.getWait();
        driver.get(BASE_URL);
        handleCookieBanner();
    }
    private void handleCookieBanner() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement cookieBtn = shortWait.until(ExpectedConditions.elementToBeClickable(cookieBtnLocator));
            cookieBtn.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cookie")));
        } catch (Exception e) {
            System.out.println("Cookie баннер отсутствует или уже закрыт.");
        }
    }
    @Test
    public void test1_CheckBlockTitle() {
        // 1- Проверить название указанного блока
        WebElement blockTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(blockTitleLocator));
        Assertions.assertTrue(blockTitle.isDisplayed(), "Блок оплаты не отображается на странице");
        String actualTitleText = blockTitle.getText().replace("\n", " ").trim();
        Assertions.assertEquals("Онлайн пополнение без комиссии", actualTitleText,
                "Название блока не совпадает с ожидаемым");
    }
    @Test
    public void test2_CheckPaymentLogosPresence() {
        //2 - Проверить наличие логотипов платёжных систем
        WebElement logosContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(logosContainerLocator));
        Assertions.assertTrue(logosContainer.isDisplayed(), "Контейнер платежных систем скрыт");
        List<WebElement> logos = driver.findElements(logosImagesLocator);
        Assertions.assertFalse(logos.isEmpty(), "Список логотипов платежных систем пуст");
    }
    @Test
    public void test3_CheckDetailsLinkWorking() {
        //3 - Проверить работу ссылки «Подробнее о сервисе»
        WebElement detailsLink = wait.until(ExpectedConditions.elementToBeClickable(detailsLinkLocator));
        detailsLink.click();
        wait.until(ExpectedConditions.urlContains(EXPECTED_DETAILS_URL));
        Assertions.assertTrue(driver.getCurrentUrl().contains(EXPECTED_DETAILS_URL),
                "Переход по ссылке 'Подробнее о сервисе' не выполнен");
    }
    @Test
    public void test4_CheckSubmitFormCommunicationServices() {
        //4 - Заполнить поля и проверить работу кнопки «Продолжить» (вариант «Услуги связи»)
        WebElement phoneInput = wait.until(ExpectedConditions.elementToBeClickable(phoneInputLocator));
        phoneInput.clear();
        phoneInput.sendKeys("297777777");
        WebElement sumInput = wait.until(ExpectedConditions.elementToBeClickable(sumInputLocator));
        sumInput.clear();
        sumInput.sendKeys("10");
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(submitButtonLocator));
        Assertions.assertTrue(submitButton.isDisplayed(), "Кнопка 'Продолжить' отсутствует на форме");
        submitButton.click();
        //Проверяем, что произошел уход с главной страницы на эквайринг оплаты
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(BASE_URL)));
        Assertions.assertNotEquals(BASE_URL, driver.getCurrentUrl(),
                "Форма не отправилась, перехода на страницу оплаты не произошло");
    }
    @AfterEach
    public void tearDown() {
        if (driverManager != null) {
            driverManager.quitDriver();
        }
    }
}