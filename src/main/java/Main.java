import calculator.Calculator;
import passwordmanager.PasswordManagerGUI;
import todo.ToDoList;
import todo.ToDoListManagerGUI;
import shoppinglist.ShoppingListGUI;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static ToDoList toDoList;

    public static void main(String[] args) {
        toDoList = new ToDoList();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            toDoList.saveToFile();
            System.out.println("ToDo list saved on exit.");
        }));

        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Main Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(5, 1));

            JButton calculatorButton = new JButton("Start Calculator");
            calculatorButton.addActionListener(e -> {
                Calculator.start();
            });

            JButton toDoListManagerButton = new JButton("Manage ToDo Lists");
            toDoListManagerButton.addActionListener(e -> {
                ToDoListManagerGUI managerGUI = new ToDoListManagerGUI(toDoList);
                managerGUI.setVisible(true);
            });

            JButton shoppingListButton = new JButton("Open Shopping List");
            shoppingListButton.addActionListener(e -> {
                ShoppingListGUI shoppingListGUI = new ShoppingListGUI();
                shoppingListGUI.setVisible(true);
            });

            JButton passwordManagerButton = new JButton("Open Password Manager");
            passwordManagerButton.addActionListener(e -> {
                PasswordManagerGUI passwordManagerGUI = new PasswordManagerGUI();
                passwordManagerGUI.setVisible(true);
            });

            JButton exitButton = new JButton("Exit");
            exitButton.addActionListener(e -> System.exit(0));

            panel.add(calculatorButton);
            panel.add(toDoListManagerButton);
            panel.add(shoppingListButton);
            panel.add(passwordManagerButton);
            panel.add(exitButton);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
