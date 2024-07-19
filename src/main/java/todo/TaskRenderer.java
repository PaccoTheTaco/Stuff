package todo;

import javax.swing.*;
import java.awt.*;

public class TaskRenderer extends JPanel implements ListCellRenderer<Task> {
    private JCheckBox checkBox;
    private JLabel dueDateLabel;
    private JLabel priorityLabel;
    private JLabel deleteLabel;

    public TaskRenderer() {
        setLayout(new BorderLayout());
        checkBox = new JCheckBox();
        dueDateLabel = new JLabel();
        priorityLabel = new JLabel();
        deleteLabel = new JLabel("X");
        deleteLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(checkBox);
        textPanel.add(dueDateLabel);
        textPanel.add(priorityLabel);

        add(textPanel, BorderLayout.CENTER);
        add(deleteLabel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
        checkBox.setText(task.getDescription());
        checkBox.setSelected(task.isCompleted());
        String dueDateText = (task.getDueDate() != null) ? "Due: " + task.getDueDate().toString() : "No due date";
        dueDateLabel.setText(dueDateText);
        priorityLabel.setText("Priority: " + task.getPriority());

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        return this;
    }
}
