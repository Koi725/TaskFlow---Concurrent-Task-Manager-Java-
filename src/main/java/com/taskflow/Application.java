package com.taskflow;

import com.taskflow.concurrency.ThreadSafeTaskRepository;
import com.taskflow.core.domain.Task;
import com.taskflow.core.domain.TaskStatus;
import com.taskflow.core.service.TaskService;
import com.taskflow.util.ConsoleFormatter;
import com.taskflow.util.Logger;
import com.taskflow.util.TaskGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Application {
    
    private final TaskService taskService;
    
    public Application() {
        Logger.info("Initializing TaskFlow Application");
        ThreadSafeTaskRepository repository = new ThreadSafeTaskRepository();
        this.taskService = new TaskService(repository, 4);
        Logger.success("Application initialized successfully");
    }
    
    public static void main(String[] args) {
        Logger.info("=== TASKFLOW APPLICATION STARTED ===");
        
        Application app = new Application();
        
        try {
            app.run();
        } catch (Exception e) {
            Logger.error("Application failed", e);
        } finally {
            Logger.info("=== TASKFLOW APPLICATION ENDED ===");
            Logger.close();
        }
    }
    
    public void run() {
        ConsoleFormatter.printHeader("TASKFLOW - CONCURRENT TASK MANAGEMENT SYSTEM");
        
        try {
            Logger.info("Starting demonstration sequence");
            
            demonstrateSorting();
            demonstrateConcurrency();
            displayStatistics();
            
            ConsoleFormatter.printSuccess("System demonstration completed successfully!");
            Logger.success("Demonstration completed successfully");
            
        } catch (Exception e) {
            Logger.error("Error during demonstration", e);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            taskService.shutdown();
            Logger.info("TaskService shutdown complete");
        }
    }
    
    private void demonstrateSorting() {
        Logger.info("=== SORTING DEMONSTRATION START ===");
        ConsoleFormatter.printSection("1. TASK SORTING DEMONSTRATION");
        
        try {
            ConsoleFormatter.printInfo("Creating 12 tasks with random priorities and deadlines...");
            Logger.debug("Creating 12 random tasks");
            
            for (int i = 0; i < 12; i++) {
                Task task = TaskGenerator.generateRandomTask();
                taskService.createTask(task);
                Logger.debug("Created task: " + task.getTaskId() + " - " + task.getTitle());
            }
            
            ConsoleFormatter.printSuccess("Created " + taskService.getTotalTaskCount() + " tasks");
            Logger.success("Created " + taskService.getTotalTaskCount() + " tasks");
            
            System.out.println();
            ConsoleFormatter.printInfo("Sorting tasks by PRIORITY (High to Low):");
            Logger.debug("Sorting by priority");
            List<Task> byPriority = taskService.getTasksSortedByPriority();
            ConsoleFormatter.printTaskList(byPriority, 5);
            Logger.success("Priority sorting completed");
            
            System.out.println();
            ConsoleFormatter.printInfo("Sorting tasks by DEADLINE (Earliest first):");
            Logger.debug("Sorting by deadline");
            List<Task> byDeadline = taskService.getTasksSortedByDeadline();
            ConsoleFormatter.printTaskList(byDeadline, 5);
            Logger.success("Deadline sorting completed");
            
            System.out.println();
            ConsoleFormatter.printInfo("Sorting tasks by PRIORITY then DEADLINE:");
            Logger.debug("Combined sorting");
            List<Task> combined = taskService.getTasksSortedByPriorityAndDeadline();
            ConsoleFormatter.printTaskList(combined, 5);
            Logger.success("Combined sorting completed");
            
            Logger.info("=== SORTING DEMONSTRATION END ===");
            
        } catch (Exception e) {
            Logger.error("Sorting demonstration failed", e);
            throw e;
        }
    }
    
    private void demonstrateConcurrency() {
        Logger.info("=== CONCURRENCY DEMONSTRATION START ===");
        ConsoleFormatter.printSection("2. CONCURRENT TASK PROCESSING");
        
        try {
            ConsoleFormatter.printInfo("Adding 8 pending tasks for concurrent processing...");
            Logger.debug("Creating 8 high-priority tasks");
            
            for (int i = 0; i < 8; i++) {
                Task task = TaskGenerator.generateHighPriorityTask();
                taskService.createTask(task);
                Logger.debug("Created high-priority task: " + task.getTaskId());
            }
            
            System.out.println();
            List<Task> pendingBefore = taskService.getTasksByStatus(TaskStatus.PENDING);
            ConsoleFormatter.printStatistic("Tasks in PENDING status", pendingBefore.size());
            Logger.info("Pending tasks before processing: " + pendingBefore.size());
            
            System.out.println();
            ConsoleFormatter.printInfo("Processing tasks concurrently with thread pool (4 threads)...");
            Logger.info("Starting concurrent processing with 4 threads");
            
            long startTime = System.currentTimeMillis();
            CompletableFuture<Void> processingFuture = taskService.processAllPendingTasks();
            
            processingFuture.get();
            long duration = System.currentTimeMillis() - startTime;
            
            ConsoleFormatter.printSuccess("Concurrent processing completed in " + duration + "ms");
            Logger.success("Processing completed in " + duration + "ms");
            
            System.out.println();
            List<Task> completed = taskService.getTasksByStatus(TaskStatus.COMPLETED);
            ConsoleFormatter.printStatistic("Tasks now COMPLETED", completed.size());
            ConsoleFormatter.printStatistic("Total tasks processed", taskService.getProcessedTaskCount());
            
            Logger.info("Completed tasks: " + completed.size());
            Logger.info("Total processed: " + taskService.getProcessedTaskCount());
            Logger.info("=== CONCURRENCY DEMONSTRATION END ===");
            
        } catch (Exception e) {
            Logger.error("Concurrency demonstration failed", e);
            throw new RuntimeException("Processing error: " + e.getMessage(), e);
        }
    }
    
    private void displayStatistics() {
        Logger.info("=== STATISTICS DISPLAY START ===");
        ConsoleFormatter.printSection("3. SYSTEM STATISTICS");
        
        try {
            int total = taskService.getTotalTaskCount();
            int completed = taskService.getTasksByStatus(TaskStatus.COMPLETED).size();
            int pending = taskService.getTasksByStatus(TaskStatus.PENDING).size();
            int inProgress = taskService.getTasksByStatus(TaskStatus.IN_PROGRESS).size();
            
            ConsoleFormatter.printStatistic("Total tasks in system", total);
            ConsoleFormatter.printStatistic("Tasks completed", completed);
            ConsoleFormatter.printStatistic("Tasks pending", pending);
            ConsoleFormatter.printStatistic("Tasks in progress", inProgress);
            
            Logger.debug("Statistics - Total: " + total + ", Completed: " + completed + 
                        ", Pending: " + pending + ", In Progress: " + inProgress);
            
            System.out.println();
            List<Task> highPriority = taskService.getHighPriorityTasks();
            ConsoleFormatter.printStatistic("High/Critical priority tasks", highPriority.size());
            Logger.debug("High priority tasks: " + highPriority.size());
            
            List<Task> overdue = taskService.getOverdueTasks();
            if (!overdue.isEmpty()) {
                ConsoleFormatter.printWarning("Overdue tasks found: " + overdue.size());
                Logger.warning("Overdue tasks detected: " + overdue.size());
            }
            
            Logger.info("=== STATISTICS DISPLAY END ===");
            
        } catch (Exception e) {
            Logger.error("Statistics display failed", e);
            throw e;
        }
    }
}