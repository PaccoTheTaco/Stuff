package todo;

import javax.swing.*;
import java.awt.*;

public class TaskRenderer extends JCheckBox implements ListCellRenderer<Task> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
        setText(task.getDescription());
        setSelected(task.isCompleted());
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        return this;
    }
}
