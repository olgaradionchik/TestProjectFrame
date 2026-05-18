package org.example.pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

@TestMethodOrder(MethodOrderer.MethodName.class)

public class PaymentPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Константы локаторов (CAPS_LOCK)
    public static final By COOKIE_BTN = By.xpath("//button[contains(@class, 'cookie__btn') or contains(text(), 'Принять') or contains(text(), 'Согласен')]");
    public static final By BLOCK_TITLE = By.xpath("//h2[normalize-space()='Онлайн пополнение без комиссии']");
    public static final By LOGOS_CONTAINER = By.className("pay__partners");
    public static final By LOGOS_IMAGES = By.cssSelector(".pay__partners img");
    public static final By DETAILS_LINK = By.xpath("//a[contains(text(), 'Подробнее о сервисе')]");
    public static final By PHONE_INPUT = By.id("connection-phone");
    public static final By SUM_INPUT = By.id("connection-sum");
    public static final By SUBMIT_BUTTON = By.xpath("//button[normalize-space()='Продолжить']");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    @Step("Проверить и закрыть баннер cookie")
    public void handleCookieBanner() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement cookieBtn = shortWait.until(ExpectedConditions.elementToBeClickable(COOKIE_BTN));
            cookieBtn.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cookie")));
        } catch (Exception e) {
            System.out.println("Cookie баннер отсутствует или уже закрыт.");
        }
    }
    @Step("Нажать на ссылку 'Подробнее о сервисе'")
    public void clickDetailsLink() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(DETAILS_LINK));
        link.click();
    }
    @Step("Заполнить форму пополнения: номер телефона '{phone}', сумма пополнения '{sum}'")
    public void fillForm(String phone, String sum) {
        WebElement phoneField = wait.until(ExpectedConditions.elementToBeClickable(PHONE_INPUT));
        phoneField.clear();
        phoneField.sendKeys(phone);

        WebElement sumField = wait.until(ExpectedConditions.elementToBeClickable(SUM_INPUT));
        sumField.clear();
        sumField.sendKeys(sum);
    }
    @Step("Нажать кнопку 'Продолжить' для отправки формы")
    public void clickContinue() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(SUBMIT_BUTTON));
        // Используем JS-клик для 100% защиты от случайных перекрытий анимациями сайта
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }
    //5-тый тест
    // Локаторы кнопок-вкладок (поиск по тексту параграфов)
    public static final By TAB_CONNECTION = By.xpath("//p[contains(text(),'Услуги связи')]");
    public static final By TAB_INTERNET = By.xpath("//p[contains(text(),'Домашний интернет')]");
    public static final By TAB_INSTALLMENT = By.xpath("//p[contains(text(),'Рассрочка')]");
    public static final By TAB_DEBT = By.xpath("//p[contains(text(),'Задолженность')]");

    // Железобетонные локаторы полей ввода для каждой из 4-х вкладок
    public static final By INPUT_PHONE_CONNECTION = By.id("connection-phone");
    public static final By INPUT_SUM_CONNECTION = By.id("connection-sum");

    public static final By INPUT_PHONE_INTERNET = By.id("internet-phone");
    public static final By INPUT_SUM_INTERNET = By.id("internet-sum");

    public static final By INPUT_SCORE_INSTALLMENT = By.id("score-instalment");
    public static final By INPUT_SUM_INSTALLMENT = By.id("instalment-sum");

    public static final By INPUT_SCORE_DEBT = By.id("score-arrears");
    public static final By INPUT_SUM_DEBT = By.id("arrears-sum");

    @Step("Переключиться на вкладку")
    public void selectTab(By tabLocator) {
        WebElement tab = wait.until(ExpectedConditions.presenceOfElementLocated(tabLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);
        try {
            Thread.sleep(500); // Технологическая пауза для перерисовки DOM скриптами сайта
        } catch (InterruptedException ignored) {}
    }
    @Step("Получить плейсхолдер поля")
    public String getPlaceholder(By inputLocator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator)).getAttribute("placeholder");
    }
    //Тест 6
    // Селектор самого iframe платежной системы bePaid
    public static final By IFRAME_BE_PAID = By.cssSelector("iframe.bepaid-iframe, iframe[src*='bepaid']");

    // Элементы бланка счета внутри iframe
    public static final By ORDER_AMOUNT_TEXT = By.cssSelector(".pay-description__cost, .order-amount, .header__price");
    public static final By ORDER_PHONE_TEXT = By.cssSelector(".pay-description__text, .order-phone-info, .header__description");
    public static final By PAY_BUTTON = By.cssSelector(".btn-pay, .pay-btn, .btn-submit, .button-page__btn, button[type='submit']:not([class*='lang'])");

    // Текстовые подписи (лейблы) незаполненных полей карты внутри iframe
    public static final By CARD_NUMBER_LABEL = By.xpath("//label[contains(text(),'Номер карты') or contains(@for,'number') or contains(text(),'Номер')]");
    public static final By CARD_EXPIRY_LABEL = By.xpath("//label[contains(text(),'Срок действия') or contains(@for,'date') or contains(text(),'Срок')]");
    public static final By CARD_CVC_LABEL = By.xpath("//label[contains(text(),'CVC') or contains(@for,'cvc')]");
    public static final By CARD_HOLDER_LABEL = By.xpath("//label[contains(text(),'Имя держателя') or contains(@for,'name') or contains(text(),'Имя')]");

    // Иконки платежных систем внутри iframe
    public static final By PAYMENT_SYSTEMS_LOGOS = By.cssSelector(".cards-brands__item, .payment-systems-icons img, .cards-brands img");

    /**
     * Выполняет безопасный переход контекста драйвера внутрь iframe платежной системы bePaid.
     * Без этого переключения Selenium не сможет увидеть элементы внутри фрейма.
     */
    @Step("Переключить контекст управления внутрь платежного iframe bePaid")
    public void switchToPaymentIframe() {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IFRAME_BE_PAID));
    }
}