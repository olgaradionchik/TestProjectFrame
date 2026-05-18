package org.example;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
public class DriverManager {
    private WebDriver driver;
    private WebDriverWait wait;
    //Метод для инициализации и настройки драйвера
    public WebDriver initDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Настройка неявного ожидания (опционально)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return driver;
    }
    // Геттер для получения объекта WebDriverWait в тестах
    public WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        }
        return wait;
    }
    // Метод для безопасного закрытия браузера
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}