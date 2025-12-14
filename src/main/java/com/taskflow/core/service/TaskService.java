package com.taskflow.core.service;

import com.taskflow.concurrency.ThreadSafeTaskRepository;
import com.taskflow.concurrency.TaskProcessor;
import com.taskflow.core.domain.Priority;
import com.taskflow.core.domain.Task;
import com.taskflow.core.domain.TaskStatus;
import com.taskflow.sorting.TaskComparator;
import com.taskflow.sorting.TaskSorter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TaskService {
    
    private final ThreadSafeTaskRepository repository;
    private final TaskSorter sorter;
    private final TaskProcessor processor;
    
    public TaskService(ThreadSafeTaskRepository repository, int processorThreads) {
        this.repository = Objects.requireNonNull(repository);
        this.sorter = new TaskSorter();
        this.processor = new TaskProcessor(repository, processorThreads);
    }
    
    public void createTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        repository.addTask(task);
    }
    
    public Optional<Task> getTask(String taskId) {
        Objects.requireNonNull(taskId, "Task ID cannot be null");
        return repository.findById(taskId);
    }
    
    public List<Task> getAllTasks() {
        return repository.findAll();
    }
    
    public List<Task> getTasksByStatus(TaskStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        return repository.findByStatus(status);
    }
    
    public List<Task> getTasksSortedByPriority() {
        List<Task> tasks = repository.findAll();
        return sorter.sort(tasks, TaskComparator.byPriority());
    }
    
    public List<Task> getTasksSortedByDeadline() {
        List<Task> tasks = repository.findAll();
        return sorter.sort(tasks, TaskComparator.byDeadline());
    }
    
    public List<Task> getTasksSortedByPriorityAndDeadline() {
        List<Task> tasks = repository.findAll();
        return sorter.sort(tasks, TaskComparator.byPriorityThenDeadline());
    }
    
    public List<Task> getOverdueTasks() {
        return repository.findAll().stream()
            .filter(Task::isOverdue)
            .collect(Collectors.toList());
    }
    
    public List<Task> getHighPriorityTasks() {
        return repository.findAll().stream()
            .filter(task -> task.getPriority() == Priority.HIGH || 
                           task.getPriority() == Priority.CRITICAL)
            .collect(Collectors.toList());
    }
    
    public CompletableFuture<Void> processAllPendingTasks() {
        List<Task> pendingTasks = repository.findByStatus(TaskStatus.PENDING);
        return processor.processTasks(pendingTasks);
    }
    
    public void updateTaskStatus(String taskId, TaskStatus newStatus) {
        Task task = repository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        task.updateStatus(newStatus);
    }
    
    public void assignTask(String taskId, String user) {
        Task task = repository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        task.assignTo(user);
    }
    
    public boolean deleteTask(String taskId) {
        return repository.removeTask(taskId);
    }
    
    public int getTotalTaskCount() {
        return repository.size();
    }
    
    public int getProcessedTaskCount() {
        return processor.getProcessedCount();
    }
    
    public void shutdown() {
        processor.shutdown();
    }
}