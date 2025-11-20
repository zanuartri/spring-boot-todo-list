package com.example.todo.service;

import com.example.todo.exception.ResourceNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public List<Task> getAll() {
        return repository.findAll();
    }

    public Task getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    public Task create(Task task) {
        return repository.save(task);
    }

    public Task update(Long id, Task newTask) {
        Task existing = getById(id);
        existing.setTitle(newTask.getTitle());
        existing.setDescription(newTask.getDescription());
        existing.setCompleted(newTask.isCompleted());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Task existing = getById(id);
        repository.delete(existing);
    }

    public List<Task> getCompletedTasks() {
        return repository.findByCompleted(true);
    }

    public List<Task> getPendingTasks() {
        return repository.findByCompleted(false);
    }

    public Task markAsCompleted(Long id) {
        Task task = getById(id);
        task.setCompleted(true);
        return repository.save(task);
    }

    public Task markAsPending(Long id) {
        Task task = getById(id);
        task.setCompleted(false);
        return repository.save(task);
    }

    public long countTasks() {
        return repository.count();
    }

    public long countCompletedTasks() {
        return repository.findByCompleted(true).size();
    }

    public long countPendingTasks() {
        return repository.findByCompleted(false).size();
    }
}
