package com.taskflow.concurrency;

import com.taskflow.core.domain.Task;
import com.taskflow.core.domain.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ThreadSafeTaskRepository {
    
    private final ConcurrentHashMap<String, Task> tasks;
    private final ReadWriteLock lock;
    
    public ThreadSafeTaskRepository() {
        this.tasks = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }
    
    public void addTask(Task task) {
        lock.writeLock().lock();
        try {
            if (tasks.containsKey(task.getTaskId())) {
                throw new IllegalArgumentException("Task already exists: " + task.getTaskId());
            }
            tasks.put(task.getTaskId(), task);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public Optional<Task> findById(String taskId) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(tasks.get(taskId));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<Task> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(tasks.values());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<Task> findByStatus(TaskStatus status) {
        lock.readLock().lock();
        try {
            return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean removeTask(String taskId) {
        lock.writeLock().lock();
        try {
            return tasks.remove(taskId) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public int size() {
        return tasks.size();
    }
    
    public void clear() {
        lock.writeLock().lock();
        try {
            tasks.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}