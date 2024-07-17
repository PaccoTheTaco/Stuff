import calculator.Calculator;
import todo.ToDoListManagerGUI;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Main Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 1));

            JButton calculatorButton = new JButton("Start Calculator");
            calculatorButton.addActionListener(e -> {
                Calculator.start();
            });

            JButton toDoListManagerButton = new JButton("Manage ToDo Lists");
            toDoListManagerButton.addActionListener(e -> {
                ToDoListManagerGUI managerGUI = new ToDoListManagerGUI();
                managerGUI.setVisible(true);
            });

            JButton exitButton = new JButton("Exit");
            exitButton.addActionListener(e -> System.exit(0));

            panel.add(calculatorButton);
            panel.add(toDoListManagerButton);
            panel.add(exitButton);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
