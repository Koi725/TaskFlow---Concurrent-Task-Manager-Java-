package com.taskflow.sorting;

import com.taskflow.core.domain.Task;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TaskSorter {
    
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        Objects.requireNonNull(tasks, "Task list cannot be null");
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        
        List<Task> sorted = new ArrayList<>(tasks);
        quickSort(sorted, 0, sorted.size() - 1, comparator);
        return sorted;
    }
    
    private void quickSort(List<Task> tasks, int low, int high, Comparator<Task> comparator) {
        if (low < high) {
            int pivotIndex = partition(tasks, low, high, comparator);
            quickSort(tasks, low, pivotIndex - 1, comparator);
            quickSort(tasks, pivotIndex + 1, high, comparator);
        }
    }
    
    private int partition(List<Task> tasks, int low, int high, Comparator<Task> comparator) {
        Task pivot = tasks.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(tasks.get(j), pivot) <= 0) {
                i++;
                swap(tasks, i, j);
            }
        }
        
        swap(tasks, i + 1, high);
        return i + 1;
    }
    
    private void swap(List<Task> tasks, int i, int j) {
        Task temp = tasks.get(i);
        tasks.set(i, tasks.get(j));
        tasks.set(j, temp);
    }
    
    public List<Task> mergeSortedLists(List<Task> list1, List<Task> list2, Comparator<Task> comparator) {
        List<Task> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < list1.size() && j < list2.size()) {
            if (comparator.compare(list1.get(i), list2.get(j)) <= 0) {
                merged.add(list1.get(i++));
            } else {
                merged.add(list2.get(j++));
            }
        }
        
        while (i < list1.size()) {
            merged.add(list1.get(i++));
        }
        
        while (j < list2.size()) {
            merged.add(list2.get(j++));
        }
        
        return merged;
    }
}