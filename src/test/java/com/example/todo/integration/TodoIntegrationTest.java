package com.example.todo.integration;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for the entire Todo application
 * Uses H2 in-memory database for testing
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class TodoIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configure MockMvc manually for SpringBootTest
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        taskRepository.deleteAll();
    }

    @Test
    void testCompleteTaskWorkflow() throws Exception {
        // Create a task using raw JSON to avoid LocalDateTime issues
        String createTaskJson = """
                {
                    "title": "Integration Test Task",
                    "description": "Test the complete workflow",
                    "completed": false
                }
                """;

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTaskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.completed").value(false))
                .andReturn().getResponse().getContentAsString();

        Task createdTask = objectMapper.readValue(response, Task.class);
        Long taskId = createdTask.getId();

        // Get the created task
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Task"));

        // Update the task using raw JSON
        String updateTaskJson = """
                {
                    "title": "Integration Test Task",
                    "description": "Updated description",
                    "completed": true
                }
                """;

        mockMvc.perform(put("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.description").value("Updated description"));

        // Verify the task is in the list
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].completed").value(true));

        // Delete the task
        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());

        // Verify the task is deleted
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testWebInterfaceRendering() throws Exception {
        // Create test data
        Task task = new Task();
        task.setTitle("Web Test Task");
        task.setDescription("Testing web interface");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);

        // Test home page renders
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tasks"));

        // Test create form renders
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void testErrorHandling() throws Exception {
        // Test 404 for non-existent task
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Task with id 999 not found"));
    }

    @Test
    void testValidation() throws Exception {
        // Test validation for empty title using raw JSON
        String invalidTaskJson = """
                {
                    "description": "No title provided",
                    "completed": false
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTaskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }
}
