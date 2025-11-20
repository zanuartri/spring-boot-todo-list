package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @TestConfiguration
    static class TaskControllerTestConfig {
        @Bean
        @Primary
        public TaskService taskService() {
            return mock(TaskService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;


    @BeforeEach
    void setUp() {
        // Reset the mock before each test
        reset(taskService);
    }

    @Test
    void testGetAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCompleted(false);
        task1.setCreatedAt(LocalDateTime.now());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCompleted(true);
        task2.setCreatedAt(LocalDateTime.now());

        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.getAll()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].completed").value(true));
    }

    @Test
    void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        when(taskService.getById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testCreateTask() throws Exception {
        // Use raw JSON to avoid LocalDateTime serialization issues
        String inputJson = """
                {
                    "title": "New Task",
                    "description": "New Description",
                    "completed": false
                }
                """;

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setCompleted(false);
        savedTask.setCreatedAt(LocalDateTime.now());

        when(taskService.create(any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void testUpdateTask() throws Exception {
        // Use raw JSON to avoid LocalDateTime serialization issues
        String inputJson = """
                {
                    "title": "Updated Task",
                    "description": "Updated Description",
                    "completed": true
                }
                """;

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setCompleted(true);
        updatedTask.setCreatedAt(LocalDateTime.now());

        when(taskService.update(eq(1L), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
