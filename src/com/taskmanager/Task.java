package com.taskmanager;


import java.time.LocalDate;
import java.util.Date;
class Task {
    private final String name;
    private final String description;
    private final String category;
    private final String priority;
    private final LocalDate deadline;

    public Task(String name, String description, String category, String priority, LocalDate deadline) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.deadline = deadline;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String toString() {
        return String.format("Name: %s | Priority: %s | Category: %s | Deadline: %s", name, priority, category, deadline);
    }
}

