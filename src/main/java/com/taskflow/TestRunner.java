package com.taskflow;

import com.taskflow.concurrency.ThreadSafeTaskRepositoryTest;
import com.taskflow.core.domain.TaskTest;
import com.taskflow.sorting.TaskSorterTest;
import com.taskflow.util.Logger;

public class TestRunner {
    
    public static void main(String[] args) {
        Logger.info("=== TEST SUITE STARTED ===");
        
        System.out.println("================================================================================");
        System.out.println("                            TASKFLOW TEST SUITE");
        System.out.println("================================================================================\n");
        
        int passed = 0;
        int failed = 0;
        
        try {
            Logger.info("Running TaskTest");
            new TaskTest().runAllTests();
            passed++;
            Logger.success("TaskTest passed");
            
            Logger.info("Running TaskSorterTest");
            new TaskSorterTest().runAllTests();
            passed++;
            Logger.success("TaskSorterTest passed");
            
            Logger.info("Running ThreadSafeTaskRepositoryTest");
            new ThreadSafeTaskRepositoryTest().runAllTests();
            passed++;
            Logger.success("ThreadSafeTaskRepositoryTest passed");
            
        } catch (AssertionError e) {
            failed++;
            Logger.error("Test failed: " + e.getMessage(), e);
            System.err.println("\n✗ TEST FAILED: " + e.getMessage());
        } catch (Exception e) {
            failed++;
            Logger.error("Unexpected test error", e);
            System.err.println("\n✗ UNEXPECTED ERROR: " + e.getMessage());
        }
        
        System.out.println("\n================================================================================");
        System.out.println("RESULTS: Passed: " + passed + " | Failed: " + failed);
        System.out.println("================================================================================\n");
        
        Logger.info("Test results - Passed: " + passed + ", Failed: " + failed);
        Logger.info("=== TEST SUITE ENDED ===");
        Logger.close();
    }
}