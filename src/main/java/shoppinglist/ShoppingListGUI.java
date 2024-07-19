package shoppinglist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShoppingListGUI extends JFrame {
    private DefaultListModel<ShoppingItem> listModel;
    private JList<ShoppingItem> itemList;
    private JTextField itemTextField;

    public ShoppingListGUI() {
        setTitle("Shopping List");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setCellRenderer(new ShoppingItemRenderer());
        JScrollPane scrollPane = new JScrollPane(itemList);

        itemTextField = new JTextField();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String itemName = itemTextField.getText().trim();
            if (!itemName.isEmpty()) {
                listModel.addElement(new ShoppingItem(itemName));
                itemTextField.setText("");
            }
        });

        itemList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int index = itemList.locationToIndex(evt.getPoint());
                if (index != -1) {
                    ShoppingItem item = listModel.getElementAt(index);
                    item.setPurchased(!item.isPurchased());
                    refreshList();
                }
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(itemTextField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
    }

    private void refreshList() {
        java.util.List<ShoppingItem> items = new java.util.ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            items.add(listModel.getElementAt(i));
        }
        items.sort((a, b) -> Boolean.compare(a.isPurchased(), b.isPurchased()));
        listModel.clear();
        for (ShoppingItem item : items) {
            listModel.addElement(item);
        }
    }
}
