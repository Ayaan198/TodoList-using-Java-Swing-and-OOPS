package com.taskmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

public class TaskManagerGUI {

    private JFrame frame;
    private JTextField taskNameField;
    private JTextField taskDescriptionField;
    private JComboBox<String> taskPriorityComboBox;
    private JComboBox<String> taskCategoryComboBox;
    private JTextField taskDeadlineField;
    private DefaultListModel<String> taskListModel;
    private java.util.List<Task> tasks;

    public TaskManagerGUI() {
        frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        tasks = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Task Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Task Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(nameLabel, gbc);

        taskNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(taskNameField, gbc);

        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(descriptionLabel, gbc);

        taskDescriptionField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(taskDescriptionField, gbc);

        JLabel categoryLabel = new JLabel("Category:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(categoryLabel, gbc);

        String[] categories = {"Work", "Personal", "Other"};
        taskCategoryComboBox = new JComboBox<>(categories);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(taskCategoryComboBox, gbc);

        JLabel priorityLabel = new JLabel("Priority:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(priorityLabel, gbc);

        String[] priorities = {"Low", "Medium", "High"};
        taskPriorityComboBox = new JComboBox<>(priorities);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(taskPriorityComboBox, gbc);

        JLabel deadlineLabel = new JLabel("Deadline (YYYY-MM-DD):");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(deadlineLabel, gbc);

        taskDeadlineField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(taskDeadlineField, gbc);

        RoundedButton addButton = new RoundedButton("Add Task", new Color(51, 153, 255), new Color(102, 178, 255));
        addButton.addActionListener(e -> addTask());
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        RoundedButton removeButton = new RoundedButton("Remove Task", new Color(255, 69, 0), new Color(255, 120, 80));
        removeButton.addActionListener(e -> removeTask());
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(removeButton, gbc);

        taskListModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void addTask() {
        String name = taskNameField.getText();
        String desc = taskDescriptionField.getText();
        String priority = (String) taskPriorityComboBox.getSelectedItem();
        String category = (String) taskCategoryComboBox.getSelectedItem();
        String deadlineStr = taskDeadlineField.getText();

        if (name.isEmpty() || desc.isEmpty() || deadlineStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate deadline = LocalDate.parse(deadlineStr);
            Task task = new Task(name, desc, category, priority, deadline);
            tasks.add(task);
            tasks.sort(Comparator.comparing(Task::getDeadline));
            refreshTaskList();
            taskNameField.setText("");
            taskDescriptionField.setText("");
            taskDeadlineField.setText("");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTaskList() {
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task.toString());
        }
    }

    private void removeTask() {
        int index = JOptionPane.showOptionDialog(frame, "Select a task to remove:", "Remove Task",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                taskListModel.toArray(), null);
        if (index != -1 && index < tasks.size()) {
            tasks.remove(index);
            refreshTaskList();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerGUI::new); 
    }
}


// Rounded button with hover effect
class RoundedButton extends JButton {
    private final Color normalColor;
    private final Color hoverColor;
    private boolean hovering = false;
    private final int radius = 25;

    public RoundedButton(String text, Color normal, Color hover) {
        super(text);
        this.normalColor = normal;
        this.hoverColor = hover;
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setOpaque(false);
        setPreferredSize(new Dimension(150, 40));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(hovering ? hoverColor : normalColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(hovering ? hoverColor : normalColor);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }
}
