import java.util.*;
import java.time.LocalDate;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(String title, LocalDate date) {
        tasks.add(new Task(title, date));
    }

    public List<Task> getSortedTasks() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
        return tasks;
    }

    public void markTaskCompleted(int index) {
        if (index >= 0 && index < tasks.size()) {
            getSortedTasks().get(index).setCompleted(true);
        }
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(getSortedTasks().get(index));
        }
    }
}
