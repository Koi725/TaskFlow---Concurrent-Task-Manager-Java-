package com.taskflow.concurrency;

import com.taskflow.core.domain.Task;
import com.taskflow.core.domain.TaskStatus;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskProcessor {
    
    private final ExecutorService executorService;
    private final ThreadSafeTaskRepository repository;
    private final AtomicInteger processedCount;
    
    public TaskProcessor(ThreadSafeTaskRepository repository, int threadPoolSize) {
        this.repository = repository;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.processedCount = new AtomicInteger(0);
    }
    
    public CompletableFuture<Void> processTasks(List<Task> tasks) {
        List<CompletableFuture<Void>> futures = tasks.stream()
            .map(task -> CompletableFuture.runAsync(() -> processTask(task), executorService))
            .toList();
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
    
    private void processTask(Task task) {
        try {
            Thread.sleep(100);
            
            task.updateStatus(TaskStatus.IN_PROGRESS);
            
            Thread.sleep(200);
            
            task.updateStatus(TaskStatus.COMPLETED);
            processedCount.incrementAndGet();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            task.updateStatus(TaskStatus.CANCELLED);
        }
    }
    
    public Future<Task> processTaskAsync(Task task) {
        return executorService.submit(() -> {
            processTask(task);
            return task;
        });
    }
    
    public int getProcessedCount() {
        return processedCount.get();
    }
    
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}