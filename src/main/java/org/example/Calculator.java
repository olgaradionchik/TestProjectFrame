package org.example;
public class Calculator {
    //1 - Факториал числа
    public long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Число должно быть положительным");
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    //2 - Площадь треугольника
    public double triangleArea(double base, double height) {
        if (base <= 0 || height <= 0) throw new IllegalArgumentException("Параметры должны быть > 0");
        return 0.5 * base * height;
    }
    //3 - Арифметические действия
    public int add(int a, int b) { return a + b; }
    public int subtract(int a, int b) { return a - b; }
    public int multiply(int a, int b) { return a * b; }
    public int divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("Деление на ноль");
        return a / b;
    }
    //4 - Сравнение двух чисел
    public String compare(int a, int b) {
        if (a > b) return "greater";
        if (a < b) return "less";
        return "equal";
    }
}
