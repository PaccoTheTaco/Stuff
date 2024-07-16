package calculator.operations;

public class Percentage implements Operation {
    @Override
    public double calculate(double num1, double num2) {
        return (num1 * num2) / 100;
    }
}
