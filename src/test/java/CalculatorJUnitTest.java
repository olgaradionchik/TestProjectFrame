import org.example.Calculator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class CalculatorJUnitTest {
    Calculator calc = new Calculator();
    @Test
    @DisplayName("Тест факториала")
    void testFactorial() {
        assertEquals(120, calc.factorial(5));
    }
    @Test
    @DisplayName("Тест площади треугольника")
    void testArea() {
        assertEquals(10.0, calc.triangleArea(4, 5));
    }
    @Test
    @DisplayName("Тест деления на ноль")
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }
    @Test
    @DisplayName("Тест сравнения")
    void testCompare() {
        assertEquals("equal", calc.compare(5, 5));
    }
}
