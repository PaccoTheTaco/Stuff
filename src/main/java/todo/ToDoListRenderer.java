package todo;

import javax.swing.*;
import java.awt.*;

public class ToDoListRenderer extends DefaultListCellRenderer {
    private static final Icon TRASH_ICON = new ImageIcon("path/to/trash/icon.png");

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            label.setText(value.toString() + "  ");
            label.setIcon(TRASH_ICON);
            label.setHorizontalTextPosition(SwingConstants.LEFT);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        return c;
    }
}
