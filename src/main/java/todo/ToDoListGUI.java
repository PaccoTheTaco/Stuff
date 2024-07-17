package todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToDoListGUI {
    private DefaultListModel<Task> listModel;
    private JList<Task> itemList;
    private JPanel panel;
    private String listName;

    public ToDoListGUI(String listName) {
        this.listName = listName;

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setCellRenderer(new TaskRenderer());
        itemList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int index = itemList.locationToIndex(evt.getPoint());
                if (index != -1) {
                    TaskRenderer renderer = (TaskRenderer) itemList.getCellRenderer();
                    Rectangle bounds = itemList.getCellBounds(index, index);
                    Point point = evt.getPoint();
                    JLabel deleteLabel = renderer.getDeleteLabel();
                    Rectangle deleteBounds = deleteLabel.getBounds();
                    deleteBounds.setLocation(bounds.x + deleteLabel.getX(), bounds.y + deleteLabel.getY());

                    if (deleteBounds.contains(point)) {
                        listModel.remove(index);
                    } else {
                        Task task = listModel.getElementAt(index);
                        task.setCompleted(!task.isCompleted());
                        itemList.repaint(itemList.getCellBounds(index, index));
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(itemList);

        JTextField itemTextField = new JTextField();
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(e -> {
            String item = itemTextField.getText();
            if (!item.isEmpty()) {
                listModel.addElement(new Task(item));
                itemTextField.setText("");
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(itemTextField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(removeButton);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}
