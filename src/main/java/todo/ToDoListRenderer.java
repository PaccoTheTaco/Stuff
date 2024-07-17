package todo;

import javax.swing.*;
import java.awt.*;

public class ToDoListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            label.setText(value.toString());
            label.setHorizontalTextPosition(SwingConstants.LEFT);
            label.setHorizontalAlignment(SwingConstants.LEFT);

            JPanel panel = new JPanel(new BorderLayout());
            JLabel deleteLabel = new JLabel("X");
            deleteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            panel.add(label, BorderLayout.CENTER);
            panel.add(deleteLabel, BorderLayout.EAST);

            return panel;
        }
        return c;
    }
}
