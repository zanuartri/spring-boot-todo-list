package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final TaskService service;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tasks", service.getAll());
        return "index";
    }

    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "create";
    }

    @PostMapping("/tasks")
    public String create(Task task) {
        service.create(task);
        return "redirect:/";
    }

    @GetMapping("/tasks/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", service.getById(id));
        return "edit";
    }

    @PostMapping("/tasks/update/{id}")
    public String update(@PathVariable Long id, Task updatedTask) {
        service.update(id, updatedTask);
        return "redirect:/";
    }

    @GetMapping("/tasks/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }
}
