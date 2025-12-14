package com.taskflow.util;

import com.taskflow.core.domain.Priority;
import com.taskflow.core.domain.Task;
import com.taskflow.core.domain.TaskStatus;

import java.time.LocalDateTime;
import java.util.Random;

public final class TaskGenerator {
    
    private static final Random RANDOM = new Random();
    private static final String[] TITLES = {
        "Review code changes", "Update documentation", "Fix critical bug",
        "Deploy to production", "Setup CI/CD pipeline", "Refactor authentication",
        "Optimize database queries", "Write unit tests", "Security audit",
        "Performance testing", "Update dependencies", "Client presentation"
    };
    private static final String[] USERS = {
        "Alice", "Bob", "Charlie", "Diana", "Eve", "Frank"
    };
    
    private TaskGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static Task generateRandomTask() {
        String title = TITLES[RANDOM.nextInt(TITLES.length)];
        Priority priority = Priority.values()[RANDOM.nextInt(Priority.values().length)];
        int daysUntilDeadline = RANDOM.nextInt(30) + 1;
        LocalDateTime deadline = LocalDateTime.now().plusDays(daysUntilDeadline);
        
        return Task.builder()
            .title(title)
            .description("Generated task for: " + title)
            .priority(priority)
            .deadline(deadline)
            .assignedTo(USERS[RANDOM.nextInt(USERS.length)])
            .build();
    }
    
    public static Task generateOverdueTask() {
        String title = TITLES[RANDOM.nextInt(TITLES.length)];
        int daysOverdue = RANDOM.nextInt(5) + 1;
        LocalDateTime deadline = LocalDateTime.now().minusDays(daysOverdue);
        
        return Task.builder()
            .title(title + " (OVERDUE)")
            .description("Overdue task")
            .priority(Priority.CRITICAL)
            .deadline(deadline.plusDays(10))
            .status(TaskStatus.PENDING)
            .build();
    }
    
    public static Task generateHighPriorityTask() {
        String title = TITLES[RANDOM.nextInt(TITLES.length)];
        LocalDateTime deadline = LocalDateTime.now().plusDays(2);
        
        return Task.builder()
            .title(title)
            .description("High priority urgent task")
            .priority(Priority.CRITICAL)
            .deadline(deadline)
            .assignedTo(USERS[RANDOM.nextInt(USERS.length)])
            .build();
    }
}