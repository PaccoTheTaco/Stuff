package calculator;

import calculator.operations.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorGUI extends JFrame {
    private static final Map<String, Operation> operations = new HashMap<>();
    private JTextField display;

    static {
        operations.put("+", new Addition());
        operations.put("-", new Subtraction());
        operations.put("*", new Multiplication());
        operations.put("/", new Division());
        operations.put(":", new Division());
    }

    public CalculatorGUI() {
        setTitle("Calculator");
        setSize(400, 600);
        setMinimumSize(new Dimension(400, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || "+-*/,".indexOf(c) != -1 || c == KeyEvent.VK_BACK_SPACE) {
                    handleKeyPress(c);
                } else if (c == KeyEvent.VK_ENTER) {
                    handleEnterPress();
                }
            }
        });
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
                "1", "2", "3", "+",
                "4", "5", "6", "-",
                "7", "8", "9", "*",
                "C", "0", "=", "/",
                ","
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> display.requestFocusInWindow());
    }

    private void handleKeyPress(char c) {
        if (c == KeyEvent.VK_BACK_SPACE) {
            String text = display.getText();
            if (!text.isEmpty()) {
                display.setText(text.substring(0, text.length() - 1));
            }
        } else if (c == ',') {
            display.setText(display.getText() + '.');
        } else {
            display.setText(display.getText() + c);
        }
    }

    private void handleEnterPress() {
        try {
            String result = String.valueOf(evaluateExpression(display.getText()));
            if (result.endsWith(".0")) {
                result = result.substring(0, result.length() - 2);
            }
            display.setText(result);
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.charAt(0) == 'C') {
                display.setText("");
            } else if (command.charAt(0) == '=') {
                handleEnterPress();
            } else if (command.charAt(0) == ',') {
                display.setText(display.getText() + '.');
            } else {
                display.setText(display.getText() + command);
            }
        }
    }

    private double evaluateExpression(String expression) {
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

    private String[] tokenizeExpression(String expression) {
        Pattern pattern = Pattern.compile("([0-9]+\\.?[0-9]*)|([+\\-*/:])");
        Matcher matcher = pattern.matcher(expression);
        Stack<String> tokens = new Stack<>();

        while (matcher.find()) {
            tokens.push(matcher.group());
        }

        return tokens.toArray(new String[0]);
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int precedence(String operator) {
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if (operator.equals("*") || operator.equals("/") || operator.equals(":")) {
            return 2;
        } else {
            return 0;
        }
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI calculator = new CalculatorGUI();
            calculator.setVisible(true);
            calculator.toFront();
            calculator.setAlwaysOnTop(true);
            calculator.setAlwaysOnTop(false);
        });
    }
}
