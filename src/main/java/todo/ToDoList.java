package todo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private List<Task> items;
    private static final String FILE_PATH = "todolist.json";

    public ToDoList() {
        items = new ArrayList<>();
        loadFromFile(); // Laden der Daten beim Erstellen des Objekts
    }

    public void addItem(Task item) {
        items.add(item);
        saveToFile();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            saveToFile();
        }
    }

    public List<Task> getItems() {
        return items;
    }

    public void saveToFile() {
        JSONArray jsonArray = new JSONArray();
        for (Task task : items) {
            JSONObject taskJson = new JSONObject();
            taskJson.put("description", task.getDescription());
            taskJson.put("completed", task.isCompleted());
            taskJson.put("dueDate", task.getDueDate() != null ? task.getDueDate().toString() : null);
            taskJson.put("priority", task.getPriority().name());
            jsonArray.put(taskJson);
        }

        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(jsonArray.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile(); // Datei erstellen, wenn sie nicht existiert
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(FILE_PATH));
            String content = new String(bytes);
            if (!content.trim().isEmpty()) {
                JSONArray jsonArray = new JSONArray(content);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject taskJson = jsonArray.getJSONObject(i);
                    String description = taskJson.getString("description");
                    boolean completed = taskJson.getBoolean("completed");
                    String dueDateStr = taskJson.optString("dueDate", null);
                    LocalDate dueDate = dueDateStr != null ? LocalDate.parse(dueDateStr) : null;
                    Task.Priority priority = Task.Priority.valueOf(taskJson.getString("priority"));
                    Task task = new Task(description, dueDate, priority);
                    task.setCompleted(completed);
                    items.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
