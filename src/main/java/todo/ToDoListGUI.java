package todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
                    Task task = listModel.getElementAt(index);
                    Rectangle bounds = itemList.getCellBounds(index, index);
                    Point point = evt.getPoint();

                    if (evt.getClickCount() == 2) {
                        editTask(task, index);
                    } else {
                        Component component = itemList.getCellRenderer().getListCellRendererComponent(itemList, listModel.getElementAt(index), index, false, false);
                        JLabel deleteLabel = (JLabel) ((JPanel) component).getComponent(1);
                        Rectangle deleteBounds = deleteLabel.getBounds();
                        deleteBounds.setLocation(bounds.x + deleteLabel.getX(), bounds.y + deleteLabel.getY());

                        if (deleteBounds.contains(point)) {
                            listModel.remove(index);
                        } else {
                            task.setCompleted(!task.isCompleted());
                            itemList.repaint(itemList.getCellBounds(index, index));
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(itemList);

        JTextField itemTextField = new JTextField();
        JTextField dueDateField = new JTextField("YYYY-MM-DD");
        JComboBox<Task.Priority> priorityComboBox = new JComboBox<>(Task.Priority.values());
        JButton addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            String item = itemTextField.getText();
            String dueDateText = dueDateField.getText().trim();
            LocalDate dueDate = null;

            if (!dueDateText.isEmpty() && !dueDateText.equals("YYYY-MM-DD")) {
                try {
                    dueDate = LocalDate.parse(dueDateText);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Task.Priority priority = (Task.Priority) priorityComboBox.getSelectedItem();
            if (!item.isEmpty()) {
                listModel.addElement(new Task(item, dueDate, priority));
                itemTextField.setText("");
                dueDateField.setText("YYYY-MM-DD");
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 4));
        inputPanel.add(itemTextField);
        inputPanel.add(dueDateField);
        inputPanel.add(priorityComboBox);
        inputPanel.add(addButton);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.NORTH);
    }

    public void setListName(String newListName) {
        this.listName = newListName;
    }

    private void editTask(Task task, int index) {
        JTextField editTextField = new JTextField(task.getDescription());
        JTextField editDueDateField = new JTextField((task.getDueDate() != null) ? task.getDueDate().toString() : "");
        JComboBox<Task.Priority> editPriorityComboBox = new JComboBox<>(Task.Priority.values());
        editPriorityComboBox.setSelectedItem(task.getPriority());

        JPanel editPanel = new JPanel(new GridLayout(3, 1));
        editPanel.add(editTextField);
        editPanel.add(editDueDateField);
        editPanel.add(editPriorityComboBox);

        int result = JOptionPane.showConfirmDialog(panel, editPanel, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            task.setDescription(editTextField.getText());
            String dueDateText = editDueDateField.getText().trim();
            if (!dueDateText.isEmpty()) {
                try {
                    task.setDueDate(LocalDate.parse(dueDateText));
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                task.setDueDate(null);
            }
            task.setPriority((Task.Priority) editPriorityComboBox.getSelectedItem());
            itemList.repaint(itemList.getCellBounds(index, index));
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
