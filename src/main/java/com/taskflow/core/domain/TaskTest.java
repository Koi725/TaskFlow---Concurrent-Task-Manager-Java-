package com.taskflow.core.domain;

import java.time.LocalDateTime;

public class TaskTest {
    
    public static void main(String[] args) {
        TaskTest test = new TaskTest();
        test.runAllTests();
    }
    
    public void runAllTests() {
        System.out.println("Running Task Tests...\n");
        
        testTaskCreation();
        testStatusUpdate();
        testOverdueDetection();
        
        System.out.println("\n✓ All Task tests passed!");
    }
    
    private void testTaskCreation() {
        System.out.println("Test: Task Creation");
        
        Task task = Task.builder()
            .title("Test Task")
            .description("Testing")
            .priority(Priority.HIGH)
            .deadline(LocalDateTime.now().plusDays(1))
            .build();
        
        assertEqual("Test Task", task.getTitle(), "Title should match");
        assertEqual(Priority.HIGH, task.getPriority(), "Priority should be HIGH");
        assertEqual(TaskStatus.PENDING, task.getStatus(), "Initial status should be PENDING");
        
        System.out.println("  ✓ Passed\n");
    }
    
    private void testStatusUpdate() {
        System.out.println("Test: Status Update");
        
        Task task = Task.builder()
            .title("Test")
            .deadline(LocalDateTime.now().plusDays(1))
            .build();
        
        task.updateStatus(TaskStatus.IN_PROGRESS);
        assertEqual(TaskStatus.IN_PROGRESS, task.getStatus(), "Status should update");
        
        task.updateStatus(TaskStatus.COMPLETED);
        assertEqual(TaskStatus.COMPLETED, task.getStatus(), "Status should be COMPLETED");
        
        System.out.println("  ✓ Passed\n");
    }
    
    private void testOverdueDetection() {
        System.out.println("Test: Overdue Detection");
        
        Task overdueTask = Task.builder()
            .title("Overdue")
            .deadline(LocalDateTime.now().plusDays(5))
            .build();
        
        assertFalse(overdueTask.isOverdue(), "Future task should not be overdue");
        
        System.out.println("  ✓ Passed\n");
    }
    
    private void assertEqual(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Expected: " + expected + ", Got: " + actual);
        }
    }
    
    private void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }
}