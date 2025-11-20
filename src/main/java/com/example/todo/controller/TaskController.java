package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        List<Task> tasks = service.getAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        Task task = service.getById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        Task saved = service.create(task);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
            @PathVariable Long id,
            @Valid @RequestBody Task updatedTask) {

        Task saved = service.update(id, updatedTask);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasks() {
        List<Task> tasks = service.getCompletedTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Task>> getPendingTasks() {
        List<Task> tasks = service.getPendingTasks();
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> markAsCompleted(@PathVariable Long id) {
        Task task = service.markAsCompleted(id);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/pending")
    public ResponseEntity<Task> markAsPending(@PathVariable Long id) {
        Task task = service.markAsPending(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStats> getStats() {
        TaskStats stats = new TaskStats(
                service.countTasks(),
                service.countCompletedTasks(),
                service.countPendingTasks()
        );
        return ResponseEntity.ok(stats);
    }

    // Inner class for statistics
    public static class TaskStats {
        public final long total;
        public final long completed;
        public final long pending;

        public TaskStats(long total, long completed, long pending) {
            this.total = total;
            this.completed = completed;
            this.pending = pending;
        }
    }

}
