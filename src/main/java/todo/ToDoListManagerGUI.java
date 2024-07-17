package todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ToDoListManagerGUI extends JFrame {
    private Map<String, ToDoListGUI> toDoLists;
    private DefaultListModel<String> listModel;
    private JList<String> listDisplay;
    private JPanel taskPanel;

    public ToDoListManagerGUI() {
        setTitle("ToDo List Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        toDoLists = new HashMap<>();
        listModel = new DefaultListModel<>();
        listDisplay = new JList<>(listModel);
        listDisplay.setCellRenderer(new ToDoListRenderer());
        JScrollPane scrollPane = new JScrollPane(listDisplay);

        listDisplay.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int index = listDisplay.locationToIndex(evt.getPoint());
                if (index != -1) {
                    Rectangle bounds = listDisplay.getCellBounds(index, index);
                    Point point = evt.getPoint();
                    if (point.getX() > bounds.getX() + bounds.getWidth() - 30) {
                        // If the click is on the trash can icon
                        String selectedList = listModel.getElementAt(index);
                        toDoLists.remove(selectedList);
                        listModel.remove(index);
                        taskPanel.removeAll();
                        taskPanel.revalidate();
                        taskPanel.repaint();
                    } else if (evt.getClickCount() == 2) {
                        // If it's a double click
                        String selectedList = listModel.getElementAt(index);
                        showToDoList(selectedList);
                    }
                }
            }
        });

        JTextField listNameField = new JTextField();
        JButton addListButton = new JButton("Add List");

        addListButton.addActionListener(e -> {
            String listName = listNameField.getText();
            if (!listName.isEmpty() && !toDoLists.containsKey(listName)) {
                toDoLists.put(listName, new ToDoListGUI(listName));
                listModel.addElement(listName);
                listNameField.setText("");
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(listNameField, BorderLayout.CENTER);
        inputPanel.add(addListButton, BorderLayout.EAST);

        taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, taskPanel);
        splitPane.setDividerLocation(200);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void showToDoList(String listName) {
        ToDoListGUI toDoListGUI = toDoLists.get(listName);
        taskPanel.removeAll();
        taskPanel.add(toDoListGUI.getPanel(), BorderLayout.CENTER);
        taskPanel.revalidate();
        taskPanel.repaint();
    }
}
