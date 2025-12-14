package com.taskflow.util;

import com.taskflow.core.domain.Task;

import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ConsoleFormatter {
    
    private static final String LINE = "=".repeat(90);
    private static final String SEPARATOR = "-".repeat(90);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private ConsoleFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static void printHeader(String title) {
        System.out.println("\n" + LINE);
        System.out.println(centerText(title, 90));
        System.out.println(LINE);
    }
    
    public static void printSection(String title) {
        System.out.println("\n" + title);
        System.out.println(SEPARATOR);
    }
    
    public static void printTask(Task task) {
        System.out.printf("%-12s: %s%n", "ID", task.getTaskId());
        System.out.printf("%-12s: %s%n", "Title", task.getTitle());
        System.out.printf("%-12s: %s%n", "Priority", task.getPriority().getDescription());
        System.out.printf("%-12s: %s%n", "Status", task.getStatus().getDisplayName());
        System.out.printf("%-12s: %s%n", "Deadline", task.getDeadline().format(FORMATTER));
        System.out.printf("%-12s: %s%n", "Assigned To", task.getAssignedTo() != null ? task.getAssignedTo() : "Unassigned");
        System.out.printf("%-12s: %s%n", "Overdue", task.isOverdue() ? "YES" : "NO");
        System.out.println(SEPARATOR);
    }
    
    public static void printTaskList(List<Task> tasks, int limit) {
        System.out.printf("%-10s %-30s %-15s %-15s %-20s%n",
            "ID", "Title", "Priority", "Status", "Deadline");
        System.out.println(SEPARATOR);
        
        int count = Math.min(tasks.size(), limit);
        for (int i = 0; i < count; i++) {
            Task t = tasks.get(i);
            System.out.printf("%-10s %-30s %-15s %-15s %-20s%n",
                t.getTaskId(),
                truncate(t.getTitle(), 28),
                t.getPriority().name(),
                t.getStatus().name(),
                t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")));
        }
        
        if (tasks.size() > limit) {
            System.out.printf("%n... and %d more tasks%n", tasks.size() - limit);
        }
    }
    
    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }
    
    public static void printInfo(String message) {
        System.out.println("ℹ " + message);
    }
    
    public static void printWarning(String message) {
        System.out.println("⚠ " + message);
    }
    
    public static void printStatistic(String label, Object value) {
        System.out.printf("%-35s: %s%n", label, value);
    }
    
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
    
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }
}