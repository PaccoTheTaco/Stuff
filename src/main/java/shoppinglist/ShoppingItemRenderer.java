package shoppinglist;

import javax.swing.*;
import java.awt.*;

public class ShoppingItemRenderer extends JPanel implements ListCellRenderer<ShoppingItem> {
    private JCheckBox checkBox;

    public ShoppingItemRenderer() {
        setLayout(new BorderLayout());
        checkBox = new JCheckBox();
        checkBox.setVerticalTextPosition(SwingConstants.TOP);
        add(checkBox, BorderLayout.WEST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ShoppingItem> list, ShoppingItem item, int index, boolean isSelected, boolean cellHasFocus) {
        String text = wrapText(item.getName(), list.getWidth() - 50);
        if (item.isPurchased()) {
            text = "<html><strike>" + text + "</strike></html>";
        } else {
            text = "<html>" + text + "</html>";
        }
        checkBox.setText(text);
        checkBox.setSelected(item.isPurchased());

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        return this;
    }

    private String wrapText(String text, int width) {
        FontMetrics fm = getFontMetrics(getFont());
        StringBuilder wrappedText = new StringBuilder();
        int lineWidth = 0;

        for (String word : text.split(" ")) {
            int wordWidth = fm.stringWidth(word + " ");
            if (lineWidth + wordWidth > width) {
                wrappedText.append("<br>");
                lineWidth = 0;
            }
            wrappedText.append(word).append(" ");
            lineWidth += wordWidth;
        }
        return wrappedText.toString().trim();
    }
}
