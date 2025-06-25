import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class TaskReminderGUI {
    private JFrame frame;
    private JTextField titleField, dueDateField;
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private TaskManager taskManager;

    public TaskReminderGUI() {
        taskManager = new TaskManager();
        frame = new JFrame("Task Reminder App");

        // Inputs
        titleField = new JTextField(15);
        dueDateField = new JTextField(10);
        JButton addButton = new JButton("Add Task");

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        JButton completeButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete Task");
        JButton exitButton = new JButton("Exit");

        // Layout
        JPanel panel = new JPanel();
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(addButton);

        JScrollPane scrollPane = new JScrollPane(taskList);
        panel.add(scrollPane);
        panel.add(completeButton);
        panel.add(deleteButton);
        panel.add(exitButton);

        frame.add(panel);
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Add Task Action
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String dateStr = dueDateField.getText().trim();
            if (title.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
                return;
            }
            try {
                LocalDate date = LocalDate.parse(dateStr);
                taskManager.addTask(title, date);
                refreshTaskList();
                titleField.setText("");
                dueDateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format.");
            }
        });

        completeButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                taskManager.markTaskCompleted(index);
                refreshTaskList();
            }
        });

        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                taskManager.deleteTask(index);
                refreshTaskList();
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    private void refreshTaskList() {
        listModel.clear();
        for (Task task : taskManager.getSortedTasks()) {
            listModel.addElement(task.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskReminderGUI::new);
    }
}
