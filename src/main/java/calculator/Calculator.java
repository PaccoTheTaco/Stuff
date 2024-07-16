package calculator;

import calculator.operations.*;

import java.util.HashMap;
import java.util.Map;

public class Calculator {
    private static final Map<String, Operation> operations = new HashMap<>();

    static {
        operations.put("+", new Addition());
        operations.put("-", new Subtraction());
        operations.put("*", new Multiplication());
        operations.put("/", new Division());
        operations.put(":", new Division());
    }

    public static void start() {
        CalculatorGUI.start();
    }
}
