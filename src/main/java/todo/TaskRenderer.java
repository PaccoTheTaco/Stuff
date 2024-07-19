package todo;

import javax.swing.*;
import java.awt.*;

public class TaskRenderer extends JPanel implements ListCellRenderer<Task> {
    private JCheckBox checkBox;
    private JLabel deleteLabel;

    public TaskRenderer() {
        setLayout(new BorderLayout());
        checkBox = new JCheckBox();
        deleteLabel = new JLabel("X");
        deleteLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(checkBox, BorderLayout.CENTER);
        add(deleteLabel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
        checkBox.setText(task.getDescription());
        checkBox.setSelected(task.isCompleted());

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        return this;
    }
}
