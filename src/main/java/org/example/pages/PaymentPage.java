package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

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

    public void clickDetailsLink() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(DETAILS_LINK));
        link.click();
    }

    public void fillForm(String phone, String sum) {
        WebElement phoneField = wait.until(ExpectedConditions.elementToBeClickable(PHONE_INPUT));
        phoneField.clear();
        phoneField.sendKeys(phone);

        WebElement sumField = wait.until(ExpectedConditions.elementToBeClickable(SUM_INPUT));
        sumField.clear();
        sumField.sendKeys(sum);
    }

    public void clickContinue() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(SUBMIT_BUTTON));
        // Используем JS-клик для 100% защиты от случайных перекрытий анимациями сайта
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }
}