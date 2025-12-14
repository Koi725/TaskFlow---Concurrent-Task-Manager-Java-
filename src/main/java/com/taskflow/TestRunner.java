package com.taskflow;

import com.taskflow.concurrency.ThreadSafeTaskRepositoryTest;
import com.taskflow.core.domain.TaskTest;
import com.taskflow.sorting.TaskSorterTest;

public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("                            TASKFLOW TEST SUITE");
        System.out.println("================================================================================\n");
        
        int passed = 0;
        int failed = 0;
        
        try {
            new TaskTest().runAllTests();
            passed++;
            
            new TaskSorterTest().runAllTests();
            passed++;
            
            new ThreadSafeTaskRepositoryTest().runAllTests();
            passed++;
            
        } catch (AssertionError e) {
            failed++;
            System.err.println("\n✗ TEST FAILED: " + e.getMessage());
        }
        
        System.out.println("\n================================================================================");
        System.out.println("RESULTS: Passed: " + passed + " | Failed: " + failed);
        System.out.println("================================================================================\n");
    }
}