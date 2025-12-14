package com.taskflow.concurrency;

import com.taskflow.core.domain.Task;
import com.taskflow.core.domain.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadSafeTaskRepositoryTest {
    
    public static void main(String[] args) {
        ThreadSafeTaskRepositoryTest test = new ThreadSafeTaskRepositoryTest();
        test.runAllTests();
    }
    
    public void runAllTests() {
        System.out.println("Running ThreadSafeTaskRepository Tests...\n");
        
        testConcurrentAdd();
        testFindOperations();
        
        System.out.println("\n✓ All Repository tests passed!");
    }
    
    private void testConcurrentAdd() {
        System.out.println("Test: Concurrent Task Addition");
        
        ThreadSafeTaskRepository repo = new ThreadSafeTaskRepository();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(10);
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    Task task = Task.builder()
                        .taskId("TASK-" + index)
                        .title("Task " + index)
                        .deadline(LocalDateTime.now().plusDays(1))
                        .build();
                    repo.addTask(task);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(5, TimeUnit.SECONDS);
            assertEqual(10, repo.size(), "Should have 10 tasks");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
        
        System.out.println("  ✓ Passed\n");
    }
    
    private void testFindOperations() {
        System.out.println("Test: Find Operations");
        
        ThreadSafeTaskRepository repo = new ThreadSafeTaskRepository();
        
        Task task = Task.builder()
            .taskId("TEST-1")
            .title("Test Task")
            .deadline(LocalDateTime.now().plusDays(1))
            .status(TaskStatus.PENDING)
            .build();
        
        repo.addTask(task);
        
        assertTrue(repo.findById("TEST-1").isPresent(), "Should find task by ID");
        
        List<Task> pending = repo.findByStatus(TaskStatus.PENDING);
        assertEqual(1, pending.size(), "Should have 1 pending task");
        
        System.out.println("  ✓ Passed\n");
    }
    
    private void assertEqual(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message);
        }
    }
    
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}