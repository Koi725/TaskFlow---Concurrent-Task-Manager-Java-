package com.taskflow.sorting;

import com.taskflow.core.domain.Task;
import java.util.Comparator;

public final class TaskComparator {
    
    private TaskComparator() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static Comparator<Task> byPriority() {
        return (t1, t2) -> Integer.compare(
            t2.getPriority().getLevel(),
            t1.getPriority().getLevel()
        );
    }
    
    public static Comparator<Task> byDeadline() {
        return Comparator.comparing(Task::getDeadline);
    }
    
    public static Comparator<Task> byCreationDate() {
        return Comparator.comparing(Task::getCreatedAt);
    }
    
    public static Comparator<Task> byTitle() {
        return Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<Task> byStatus() {
        return Comparator.comparing(task -> task.getStatus().ordinal());
    }
    
    public static Comparator<Task> byPriorityThenDeadline() {
        return byPriority().thenComparing(byDeadline());
    }
}