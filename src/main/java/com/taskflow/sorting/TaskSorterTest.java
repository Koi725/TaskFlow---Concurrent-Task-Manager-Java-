package com.taskflow.sorting;

import com.taskflow.core.domain.Priority;
import com.taskflow.core.domain.Task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TaskSorterTest {
    
    public static void main(String[] args) {
        TaskSorterTest test = new TaskSorterTest();
        test.runAllTests();
    }
    
    public void runAllTests() {
        System.out.println("Running TaskSorter Tests...\n");
        
        testSortByPriority();
        testSortByDeadline();
        
        System.out.println("\n✓ All TaskSorter tests passed!");
    }
    
    private void testSortByPriority() {
        System.out.println("Test: Sort By Priority");
        
        Task low = Task.builder()
            .title("Low Priority")
            .priority(Priority.LOW)
            .deadline(LocalDateTime.now().plusDays(1))
            .build();
        
        Task high = Task.builder()
            .title("High Priority")
            .priority(Priority.HIGH)
            .deadline(LocalDateTime.now().plusDays(1))
            .build();
        
        List<Task> tasks = Arrays.asList(low, high);
        TaskSorter sorter = new TaskSorter();
        List<Task> sorted = sorter.sort(tasks, TaskComparator.byPriority());
        
        assertEqual(Priority.HIGH, sorted.get(0).getPriority(), "First should be HIGH priority");
        assertEqual(Priority.LOW, sorted.get(1).getPriority(), "Second should be LOW priority");
        
        System.out.println("  ✓ Passed\n");
    }
    
    private void testSortByDeadline() {
        System.out.println("Test: Sort By Deadline");
        
        Task later = Task.builder()
            .title("Later")
            .deadline(LocalDateTime.now().plusDays(5))
            .build();
        
        Task sooner = Task.builder()
            .title("Sooner")
            .deadline(LocalDateTime.now().plusDays(2))
            .build();
        
        List<Task> tasks = Arrays.asList(later, sooner);
        TaskSorter sorter = new TaskSorter();
        List<Task> sorted = sorter.sort(tasks, TaskComparator.byDeadline());
        
        assertTrue(sorted.get(0).getDeadline().isBefore(sorted.get(1).getDeadline()),
            "First task should have earlier deadline");
        
        System.out.println("  ✓ Passed\n");
    }
    
    private void assertEqual(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message);
        }
    }
    
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}