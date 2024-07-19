package todo;

import java.time.LocalDate;

public class Task {
    private String description;
    private boolean completed;
    private LocalDate dueDate;
    private Priority priority;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public Task(String description, LocalDate dueDate, Priority priority) {
        this.description = description;
        this.completed = false;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        String dueDateStr = (dueDate != null) ? dueDate.toString() : "No due date";
        return (completed ? "[X] " : "[ ] ") + description + " (Due: " + dueDateStr + ", Priority: " + priority + ")";
    }
}
