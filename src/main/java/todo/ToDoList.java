package todo;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private List<String> items;

    public ToDoList() {
        items = new ArrayList<>();
    }

    public void addItem(String item) {
        items.add(item);
        System.out.println("Item added to the list.");
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            System.out.println("Item removed from the list.");
        } else {
            System.out.println("Invalid index. Please try again.");
        }
    }

    public void viewItems() {
        if (items.isEmpty()) {
            System.out.println("The ToDo list is empty.");
        } else {
            System.out.println("ToDo List:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i));
            }
        }
    }
}
