package com.example.todo.util;

import com.example.todo.model.Task;

import java.time.LocalDateTime;

/**
 * Utility class for creating test data
 */
public class TestDataUtils {

    /**
     * Creates a basic task for testing
     */
    public static Task createBasicTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        return task;
    }

    /**
     * Creates a completed task for testing
     */
    public static Task createCompletedTask() {
        Task task = createBasicTask();
        task.setTitle("Completed Test Task");
        task.setCompleted(true);
        return task;
    }

    /**
     * Creates a task with custom title and description
     */
    public static Task createTaskWithDetails(String title, String description, boolean completed) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);
        task.setCreatedAt(LocalDateTime.now());
        return task;
    }

    /**
     * Creates a task with minimal required fields
     */
    public static Task createMinimalTask(String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setCreatedAt(LocalDateTime.now());
        return task;
    }
}
