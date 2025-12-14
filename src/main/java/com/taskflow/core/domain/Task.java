package com.taskflow.core.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Task {
    private final String taskId;
    private final String title;
    private final String description;
    private final Priority priority;
    private final LocalDateTime deadline;
    private final LocalDateTime createdAt;
    private TaskStatus status;
    private String assignedTo;
    
    private Task(Builder builder) {
        this.taskId = builder.taskId;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.deadline = builder.deadline;
        this.createdAt = builder.createdAt;
        this.status = builder.status;
        this.assignedTo = builder.assignedTo;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public synchronized void updateStatus(TaskStatus newStatus) {
        if (this.status == TaskStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update cancelled task");
        }
        this.status = newStatus;
    }
    
    public synchronized void assignTo(String user) {
        if (this.status == TaskStatus.COMPLETED || this.status == TaskStatus.CANCELLED) {
            throw new IllegalStateException("Cannot reassign completed or cancelled task");
        }
        this.assignedTo = user;
    }
    
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(deadline) && status.isActive();
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public synchronized TaskStatus getStatus() {
        return status;
    }
    
    public synchronized String getAssignedTo() {
        return assignedTo;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return taskId.equals(task.taskId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }
    
    @Override
    public String toString() {
        return String.format("Task[id=%s, title=%s, priority=%s, status=%s]",
            taskId, title, priority, status);
    }
    
    public static class Builder {
        private String taskId;
        private String title;
        private String description;
        private Priority priority;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private TaskStatus status;
        private String assignedTo;
        
        private Builder() {
            this.taskId = UUID.randomUUID().toString().substring(0, 8);
            this.createdAt = LocalDateTime.now();
            this.status = TaskStatus.PENDING;
            this.priority = Priority.MEDIUM;
        }
        
        public Builder taskId(String taskId) {
            this.taskId = Objects.requireNonNull(taskId, "Task ID cannot be null");
            return this;
        }
        
        public Builder title(String title) {
            this.title = Objects.requireNonNull(title, "Title cannot be null");
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder priority(Priority priority) {
            this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
            return this;
        }
        
        public Builder deadline(LocalDateTime deadline) {
            this.deadline = Objects.requireNonNull(deadline, "Deadline cannot be null");
            return this;
        }
        
        public Builder status(TaskStatus status) {
            this.status = Objects.requireNonNull(status, "Status cannot be null");
            return this;
        }
        
        public Builder assignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }
        
        public Task build() {
            Objects.requireNonNull(title, "Title is required");
            Objects.requireNonNull(deadline, "Deadline is required");
            
            if (deadline.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Deadline cannot be in the past");
            }
            
            return new Task(this);
        }
    }
}