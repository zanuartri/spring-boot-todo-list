package com.example.todo.repository;

import com.example.todo.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository repository;

    @Test
    void testSaveAndFind() {
        Task task = new Task();
        task.setTitle("Repo Test");
        task.setDescription("Testing repository");

        repository.save(task);

        List<Task> tasks = repository.findAll();
        assertEquals(1, tasks.size());
        assertEquals("Repo Test", tasks.get(0).getTitle());
    }

    @Test
    void testFindByCompleted() {
        Task t1 = new Task();
        t1.setTitle("Task 1");
        t1.setCompleted(true);

        Task t2 = new Task();
        t2.setTitle("Task 2");
        t2.setCompleted(false);

        repository.save(t1);
        repository.save(t2);

        List<Task> completed = repository.findByCompleted(true);

        assertEquals(1, completed.size());
        assertTrue(completed.get(0).isCompleted());
    }
}
