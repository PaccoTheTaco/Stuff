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
        JButton addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            String item = itemTextField.getText();
            if (!item.isEmpty()) {
                listModel.addElement(new Task(item));
                itemTextField.setText("");
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(itemTextField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

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
        int result = JOptionPane.showConfirmDialog(panel, editTextField, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            task.setDescription(editTextField.getText());
            itemList.repaint(itemList.getCellBounds(index, index));
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
