package calculator;

import calculator.operations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("================================");
            System.out.println("Calculator Menu");
            System.out.println("================================");
            System.out.println("Enter your expression (e.g., 2 + 2 - 1 or 2+2-1) or 'q' to quit:");
            System.out.println("================================");
            System.out.print("Enter your expression: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Calculator exiting.");
                break;
            }

            try {
                double result = evaluateExpression(input);
                System.out.println("The result is: " + result);
            } catch (Exception e) {
                System.out.println("Invalid expression. Please enter a valid expression.");
            }
        }
    }

    private static double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();

        String[] tokens = tokenizeExpression(expression);
        for (String token : tokens) {
            if (isNumber(token)) {
                numbers.push(Double.parseDouble(token));
            } else if (operations.containsKey(token)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    double num2 = numbers.pop();
                    double num1 = numbers.pop();
                    String op = operators.pop();
                    double result = operations.get(op).calculate(num1, num2);
                    numbers.push(result);
                }
                operators.push(token);
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!operators.isEmpty()) {
            double num2 = numbers.pop();
            double num1 = numbers.pop();
            String op = operators.pop();
            double result = operations.get(op).calculate(num1, num2);
            numbers.push(result);
        }

        return numbers.pop();
    }

    private static String[] tokenizeExpression(String expression) {
        Pattern pattern = Pattern.compile("([0-9]+\\.?[0-9]*)|([+\\-*/:])");
        Matcher matcher = pattern.matcher(expression);
        Stack<String> tokens = new Stack<>();

        while (matcher.find()) {
            tokens.push(matcher.group());
        }

        return tokens.toArray(new String[0]);
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int precedence(String operator) {
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if (operator.equals("*") || operator.equals("/") || operator.equals(":")) {
            return 2;
        } else {
            return 0;
        }
    }
}
