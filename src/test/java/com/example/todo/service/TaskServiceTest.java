package com.example.todo.service;

import com.example.todo.exception.ResourceNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService(repository);
    }

    @Test
    void testGetAll() {
        // Given
        Task t1 = new Task();
        t1.setId(1L);
        t1.setTitle("Task 1");
        t1.setDescription("First task");
        t1.setCompleted(false);
        t1.setCreatedAt(LocalDateTime.now());

        Task t2 = new Task();
        t2.setId(2L);
        t2.setTitle("Task 2");
        t2.setDescription("Second task");
        t2.setCompleted(true);
        t2.setCreatedAt(LocalDateTime.now());

        List<Task> mockTasks = List.of(t1, t2);
        when(repository.findAll()).thenReturn(mockTasks);

        // When
        List<Task> result = service.getAll();

        // Then
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        assertFalse(result.get(0).isCompleted());
        assertTrue(result.get(1).isCompleted());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetByIdFound() {
        // Given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(task));

        // When
        Task result = service.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertFalse(result.isCompleted());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(99L)
        );

        assertEquals("Task with id 99 not found", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void testCreate() {
        // Given
        Task inputTask = new Task();
        inputTask.setTitle("New Task");
        inputTask.setDescription("New Description");
        inputTask.setCompleted(false);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setCompleted(false);
        savedTask.setCreatedAt(LocalDateTime.now());

        when(repository.save(any(Task.class))).thenReturn(savedTask);

        // When
        Task result = service.create(inputTask);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertFalse(result.isCompleted());
        assertNotNull(result.getCreatedAt());
        verify(repository, times(1)).save(inputTask);
    }

    @Test
    void testUpdate() {
        // Given
        Long taskId = 1L;

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setCompleted(false);
        existingTask.setCreatedAt(LocalDateTime.now());

        Task updateData = new Task();
        updateData.setTitle("Updated Title");
        updateData.setDescription("Updated Description");
        updateData.setCompleted(true);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setCompleted(true);
        updatedTask.setCreatedAt(existingTask.getCreatedAt());

        when(repository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(repository.save(any(Task.class))).thenReturn(updatedTask);

        // When
        Task result = service.update(taskId, updateData);

        // Then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertTrue(result.isCompleted());
        verify(repository, times(1)).findById(taskId);
        verify(repository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Long taskId = 99L;
        Task updateData = new Task();
        updateData.setTitle("Updated Title");

        when(repository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(taskId, updateData)
        );

        assertEquals("Task with id 99 not found", exception.getMessage());
        verify(repository, times(1)).findById(taskId);
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    void testDelete() {
        // Given
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Task to Delete");

        when(repository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // When
        service.delete(taskId);

        // Then
        verify(repository, times(1)).findById(taskId);
        verify(repository, times(1)).delete(existingTask);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        Long taskId = 99L;
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(taskId)
        );

        assertEquals("Task with id 99 not found", exception.getMessage());
        verify(repository, times(1)).findById(taskId);
        verify(repository, never()).delete(any(Task.class));
    }

    @Test
    void testGetCompletedTasks() {
        // Given
        Task completedTask = new Task();
        completedTask.setId(1L);
        completedTask.setTitle("Completed Task");
        completedTask.setCompleted(true);

        List<Task> completedTasks = List.of(completedTask);
        when(repository.findByCompleted(true)).thenReturn(completedTasks);

        // When
        List<Task> result = service.getCompletedTasks();

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).isCompleted());
        assertEquals("Completed Task", result.get(0).getTitle());
        verify(repository, times(1)).findByCompleted(true);
    }

    @Test
    void testGetPendingTasks() {
        // Given
        Task pendingTask = new Task();
        pendingTask.setId(1L);
        pendingTask.setTitle("Pending Task");
        pendingTask.setCompleted(false);

        List<Task> pendingTasks = List.of(pendingTask);
        when(repository.findByCompleted(false)).thenReturn(pendingTasks);

        // When
        List<Task> result = service.getPendingTasks();

        // Then
        assertEquals(1, result.size());
        assertFalse(result.get(0).isCompleted());
        assertEquals("Pending Task", result.get(0).getTitle());
        verify(repository, times(1)).findByCompleted(false);
    }

    @Test
    void testMarkAsCompleted() {
        // Given
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Task to Complete");
        task.setCompleted(false);

        Task completedTask = new Task();
        completedTask.setId(taskId);
        completedTask.setTitle("Task to Complete");
        completedTask.setCompleted(true);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(completedTask);

        // When
        Task result = service.markAsCompleted(taskId);

        // Then
        assertTrue(result.isCompleted());
        verify(repository, times(1)).findById(taskId);
        verify(repository, times(1)).save(task);
    }

    @Test
    void testMarkAsPending() {
        // Given
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Task to Mark Pending");
        task.setCompleted(true);

        Task pendingTask = new Task();
        pendingTask.setId(taskId);
        pendingTask.setTitle("Task to Mark Pending");
        pendingTask.setCompleted(false);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(pendingTask);

        // When
        Task result = service.markAsPending(taskId);

        // Then
        assertFalse(result.isCompleted());
        verify(repository, times(1)).findById(taskId);
        verify(repository, times(1)).save(task);
    }

    @Test
    void testCountTasks() {
        // Given
        when(repository.count()).thenReturn(5L);

        // When
        long result = service.countTasks();

        // Then
        assertEquals(5L, result);
        verify(repository, times(1)).count();
    }

    @Test
    void testCountCompletedTasks() {
        // Given
        List<Task> completedTasks = List.of(new Task(), new Task());
        when(repository.findByCompleted(true)).thenReturn(completedTasks);

        // When
        long result = service.countCompletedTasks();

        // Then
        assertEquals(2L, result);
        verify(repository, times(1)).findByCompleted(true);
    }

    @Test
    void testCountPendingTasks() {
        // Given
        List<Task> pendingTasks = List.of(new Task(), new Task(), new Task());
        when(repository.findByCompleted(false)).thenReturn(pendingTasks);

        // When
        long result = service.countPendingTasks();

        // Then
        assertEquals(3L, result);
        verify(repository, times(1)).findByCompleted(false);
    }
}
