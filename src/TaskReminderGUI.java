import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class TaskReminderGUI {
    private JFrame frame;
    private JTextField titleField, dueDateField, searchField;
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private TaskManager taskManager;

    private static final String DATA_FILE = "tasks.ser";

    public TaskReminderGUI() {
        taskManager = new TaskManager();
        taskManager.loadTasksFromFile(DATA_FILE);

        frame = new JFrame("Task Reminder App");
        frame.setLayout(new BorderLayout());
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top Panel
        JPanel topPanel = new JPanel();
        titleField = new JTextField(10);
        dueDateField = new JTextField(8);
        JButton addButton = new JButton("Add Task");
        searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        topPanel.add(new JLabel("Title:"));
        topPanel.add(titleField);
        topPanel.add(new JLabel("Due (YYYY-MM-DD):"));
        topPanel.add(dueDateField);
        topPanel.add(addButton);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Task List Panel
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        JButton completeButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete");
        JButton showAllButton = new JButton("Show All");
        JButton showPendingButton = new JButton("Pending");
        JButton showCompletedButton = new JButton("Completed");
        JButton exitButton = new JButton("Exit");

        bottomPanel.add(completeButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(showAllButton);
        bottomPanel.add(showPendingButton);
        bottomPanel.add(showCompletedButton);
        bottomPanel.add(exitButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button Listeners
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String dateStr = dueDateField.getText().trim();
            if (title.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields required.");
                return;
            }
            try {
                LocalDate date = LocalDate.parse(dateStr);
                taskManager.addTask(title, date);
                refreshTaskList(taskManager.getSortedTasks());
                showTrayNotification("Task added: " + title);
                titleField.setText("");
                dueDateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format.");
            }
        });

        searchButton.addActionListener(e -> {
            String kw = searchField.getText().trim();
            refreshTaskList(taskManager.searchTasks(kw));
        });

        showAllButton.addActionListener(e -> refreshTaskList(taskManager.getSortedTasks()));
        showPendingButton.addActionListener(e -> refreshTaskList(taskManager.getPendingTasks()));
        showCompletedButton.addActionListener(e -> refreshTaskList(taskManager.getCompletedTasks()));

        completeButton.addActionListener(e -> {
            int idx = taskList.getSelectedIndex();
            if (idx >= 0) {
                taskManager.markTaskCompleted(idx);
                refreshTaskList(taskManager.getSortedTasks());
                showTrayNotification("Task marked completed");
            }
        });

        deleteButton.addActionListener(e -> {
            int idx = taskList.getSelectedIndex();
            if (idx >= 0) {
                taskManager.deleteTask(idx);
                refreshTaskList(taskManager.getSortedTasks());
                showTrayNotification("Task deleted");
            }
        });

        exitButton.addActionListener(e -> {
            taskManager.saveTasksToFile(DATA_FILE);
            System.exit(0);
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                taskManager.saveTasksToFile(DATA_FILE);
            }
        });

        refreshTaskList(taskManager.getSortedTasks());
        frame.setVisible(true);
    }

    private void refreshTaskList(List<Task> tasks) {
        listModel.clear();
        for (Task task : tasks) listModel.addElement(task.toString());
    }

    private void showTrayNotification(String text) {
        if (SystemTray.isSupported()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                Image img = Toolkit.getDefaultToolkit().createImage("assets/icon.png");
                TrayIcon ti = new TrayIcon(img, "Task Reminder");
                ti.setImageAutoSize(true);
                tray.add(ti);
                ti.displayMessage("Task Reminder", text, TrayIcon.MessageType.INFO);
                tray.remove(ti);
            } catch (Exception ignored) {}
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskReminderGUI::new);
    }
}