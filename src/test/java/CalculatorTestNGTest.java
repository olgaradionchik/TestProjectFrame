import org.example.Calculator;
import org.testng.annotations.Test; // Аннотация TestNG
import org.testng.Assert;
public class CalculatorTestNGTest {
    Calculator calc = new Calculator();
    @Test(description = "Проверка сложения")
    public void testAddition() {
        Assert.assertEquals(calc.add(2, 3), 5);
    }
    @Test(description = "Проверка факториала")
    public void testFactorial() {
        Assert.assertEquals(calc.factorial(4), 24);
    }
    @Test(expectedExceptions = ArithmeticException.class)
    public void testDivideByZero() {
        calc.divide(10, 0);
    }
    @Test
    public void testCompare() {
        Assert.assertEquals(calc.compare(10, 5), "greater");
    }
}
