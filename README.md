# 📋 Task Reminder App (Java Swing)

A simple **Java GUI-based To-Do application** built using **Java Swing** to help users manage daily tasks.

## ✨ Features

- Add new task with title and due date
- View all tasks (pending and completed)
- Mark tasks as completed
- Delete tasks
- Sort tasks by due date
- Search and filter tasks
- Desktop notification on task actions
- Save/load tasks using file serialization

## 📁 Structure

```
TaskReminderApp/
├── src/
│   ├── Task.java
│   ├── TaskManager.java
│   └── TaskReminderGUI.java
├── assets/
│   └── icon.png
├── README.md
└── .gitignore
```

## 🛠 Run Instructions

Compile and run with:
```
javac -d out src/*.java
java -cp out TaskReminderGUI
```

## 📄 License

MIT License