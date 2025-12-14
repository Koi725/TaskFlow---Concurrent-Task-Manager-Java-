package com.taskflow;

import com.taskflow.concurrency.ThreadSafeTaskRepository;
import com.taskflow.core.domain.Task;
import com.taskflow.core.domain.TaskStatus;
import com.taskflow.core.service.TaskService;
import com.taskflow.util.ConsoleFormatter;
import com.taskflow.util.TaskGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Application {
    
    private final TaskService taskService;
    
    public Application() {
        ThreadSafeTaskRepository repository = new ThreadSafeTaskRepository();
        this.taskService = new TaskService(repository, 4);
    }
    
    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
    
    public void run() {
        ConsoleFormatter.printHeader("TASKFLOW - CONCURRENT TASK MANAGEMENT SYSTEM");
        
        try {
            demonstrateSorting();
            demonstrateConcurrency();
            displayStatistics();
            
            ConsoleFormatter.printSuccess("System demonstration completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            taskService.shutdown();
        }
    }
    
    private void demonstrateSorting() {
        ConsoleFormatter.printSection("1. TASK SORTING DEMONSTRATION");
        
        ConsoleFormatter.printInfo("Creating 12 tasks with random priorities and deadlines...");
        for (int i = 0; i < 12; i++) {
            Task task = TaskGenerator.generateRandomTask();
            taskService.createTask(task);
        }
        ConsoleFormatter.printSuccess("Created " + taskService.getTotalTaskCount() + " tasks");
        
        System.out.println();
        ConsoleFormatter.printInfo("Sorting tasks by PRIORITY (High to Low):");
        List<Task> byPriority = taskService.getTasksSortedByPriority();
        ConsoleFormatter.printTaskList(byPriority, 5);
        
        System.out.println();
        ConsoleFormatter.printInfo("Sorting tasks by DEADLINE (Earliest first):");
        List<Task> byDeadline = taskService.getTasksSortedByDeadline();
        ConsoleFormatter.printTaskList(byDeadline, 5);
        
        System.out.println();
        ConsoleFormatter.printInfo("Sorting tasks by PRIORITY then DEADLINE:");
        List<Task> combined = taskService.getTasksSortedByPriorityAndDeadline();
        ConsoleFormatter.printTaskList(combined, 5);
    }
    
    private void demonstrateConcurrency() {
        ConsoleFormatter.printSection("2. CONCURRENT TASK PROCESSING");
        
        ConsoleFormatter.printInfo("Adding 8 pending tasks for concurrent processing...");
        for (int i = 0; i < 8; i++) {
            Task task = TaskGenerator.generateHighPriorityTask();
            taskService.createTask(task);
        }
        
        System.out.println();
        List<Task> pendingBefore = taskService.getTasksByStatus(TaskStatus.PENDING);
        ConsoleFormatter.printStatistic("Tasks in PENDING status", pendingBefore.size());
        
        System.out.println();
        ConsoleFormatter.printInfo("Processing tasks concurrently with thread pool (4 threads)...");
        
        long startTime = System.currentTimeMillis();
        CompletableFuture<Void> processingFuture = taskService.processAllPendingTasks();
        
        try {
            processingFuture.get();
            long duration = System.currentTimeMillis() - startTime;
            
            ConsoleFormatter.printSuccess("Concurrent processing completed in " + duration + "ms");
            
            System.out.println();
            List<Task> completed = taskService.getTasksByStatus(TaskStatus.COMPLETED);
            ConsoleFormatter.printStatistic("Tasks now COMPLETED", completed.size());
            ConsoleFormatter.printStatistic("Total tasks processed", taskService.getProcessedTaskCount());
            
        } catch (Exception e) {
            System.err.println("Processing error: " + e.getMessage());
        }
    }
    
    private void displayStatistics() {
        ConsoleFormatter.printSection("3. SYSTEM STATISTICS");
        
        ConsoleFormatter.printStatistic("Total tasks in system", taskService.getTotalTaskCount());
        ConsoleFormatter.printStatistic("Tasks completed", 
            taskService.getTasksByStatus(TaskStatus.COMPLETED).size());
        ConsoleFormatter.printStatistic("Tasks pending", 
            taskService.getTasksByStatus(TaskStatus.PENDING).size());
        ConsoleFormatter.printStatistic("Tasks in progress", 
            taskService.getTasksByStatus(TaskStatus.IN_PROGRESS).size());
        
        System.out.println();
        List<Task> highPriority = taskService.getHighPriorityTasks();
        ConsoleFormatter.printStatistic("High/Critical priority tasks", highPriority.size());
        
        List<Task> overdue = taskService.getOverdueTasks();
        if (!overdue.isEmpty()) {
            ConsoleFormatter.printWarning("Overdue tasks found: " + overdue.size());
        }
    }
}