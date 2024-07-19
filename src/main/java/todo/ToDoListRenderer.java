package todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToDoListRenderer extends JPanel implements ListCellRenderer<String> {
    private JLabel deleteLabel;
    private JLabel editLabel;
    private JTextField editTextField;
    private JLabel nameLabel;

    public ToDoListRenderer() {
        setLayout(new BorderLayout());

        deleteLabel = new JLabel("X");
        deleteLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        editLabel = new JLabel("✎");
        editLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        editLabel.setPreferredSize(new Dimension(20, 20));

        editTextField = new JTextField();
        editTextField.setVisible(false);

        nameLabel = new JLabel();
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(editLabel, BorderLayout.WEST);
        buttonPanel.add(deleteLabel, BorderLayout.EAST);

        add(nameLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        editLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameLabel.setVisible(false);
                editTextField.setText(nameLabel.getText());
                editTextField.setVisible(true);
                add(editTextField, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        editTextField.addActionListener(e -> {
            String newValue = editTextField.getText();
            if (!newValue.trim().isEmpty()) {
                JList<String> list = (JList<String>) SwingUtilities.getAncestorOfClass(JList.class, editLabel);
                DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
                int index = list.getSelectedIndex();
                model.set(index, newValue);
            }
            editTextField.setVisible(false);
            nameLabel.setText(newValue);
            nameLabel.setVisible(true);
            remove(editTextField);
            revalidate();
            repaint();
        });

        deleteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<String> list = (JList<String>) SwingUtilities.getAncestorOfClass(JList.class, deleteLabel);
                DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
                int index = list.locationToIndex(e.getPoint());
                model.remove(index);
            }
        });
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText(value);
        if (editTextField.isVisible()) {
            editTextField.setText(value);
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        return this;
    }
}
