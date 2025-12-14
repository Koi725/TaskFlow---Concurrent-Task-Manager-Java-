# TaskFlow - Concurrent Task Manager

A Java-based task management system demonstrating advanced sorting algorithms and concurrent programming techniques.

## Overview

TaskFlow is a lightweight task management application built to showcase efficient sorting implementations and thread-safe concurrent operations. It processes tasks with different priorities, handles multiple users simultaneously, and maintains data integrity in multi-threaded environments.

## Requirements Covered

### Sorting (Ordenação)

- Custom QuickSort implementation for task organization
- Multiple comparison strategies (priority, deadline, combined)
- Efficient O(n log n) average-case performance
- Merge operations for sorted lists

### Concurrency (Programação Concorrente)

- Thread-safe repository with ReadWriteLock
- Concurrent task processing with ExecutorService
- Asynchronous operations using CompletableFuture
- Atomic operations for counters and statistics

## Features

### Task Management

- Create tasks with priorities, deadlines, and assignments
- Update task status (Pending, In Progress, Completed, Cancelled)
- Track overdue tasks automatically
- Assign tasks to users

### Sorting Capabilities

- Sort by priority (Critical → Low)
- Sort by deadline (earliest first)
- Sort by creation date
- Combined sorting (priority then deadline)
- Custom comparator support

### Concurrent Processing

- Process multiple tasks simultaneously
- Thread pool with configurable size
- Thread-safe read/write operations
- Atomic counters for statistics

## Architecture

The project follows clean architecture principles:

```
com.taskflow/
├── core/
│   ├── domain/          # Business entities (Task, Priority, Status)
│   └── service/         # Business logic (TaskService)
├── sorting/             # Sorting algorithms and comparators
├── concurrency/         # Thread-safe operations
└── util/                # Helper classes
```

### Design Patterns

- **Builder Pattern**: Flexible task creation
- **Repository Pattern**: Data access abstraction
- **Strategy Pattern**: Pluggable sorting algorithms
- **Thread-Safe Singleton**: Shared repository instance

## Building and Running

### Prerequisites

- Java JDK 11 or higher
- Bash shell (Linux/Mac) or Git Bash (Windows)

### Quick Start

```bash
# Make build script executable
chmod +x build.sh

# Run everything (compile + test + demo)
./build.sh all
```

### Individual Commands

```bash
# Compile only
./build.sh compile

# Run tests
./build.sh test

# Run application
./build.sh run

# Clean build artifacts
./build.sh clean
```

### Manual Build

```bash
# Compile
find src/main/java -name "*.java" > sources.txt
javac -d build/classes -sourcepath src/main/java @sources.txt

# Run
java -cp build/classes com.taskflow.Application
```

## What You'll See

### 1. Sorting Demonstration

Creates 12 random tasks and shows:

- Tasks sorted by priority (highest first)
- Tasks sorted by deadline (earliest first)
- Combined sorting (priority + deadline)

### 2. Concurrent Processing

Demonstrates multi-threaded task processing:

- Creates 8 pending tasks
- Processes them concurrently using 4 threads
- Shows completion time and statistics

### 3. System Statistics

Displays:

- Total tasks in system
- Tasks by status (pending, in progress, completed)
- High-priority task count
- Overdue task warnings

## Technical Implementation

### Custom Sorting

Uses QuickSort with custom comparators:

```java
TaskComparator.byPriority()           // Sort by priority level
TaskComparator.byDeadline()           // Sort by deadline
TaskComparator.byPriorityThenDeadline() // Combined sort
```

### Thread Safety

Multiple mechanisms ensure data integrity:

- `ConcurrentHashMap` for task storage
- `ReadWriteLock` for coordinated access
- `synchronized` methods on Task operations
- `AtomicInteger` for counters

### Concurrent Processing

Uses Java's modern concurrency utilities:

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
CompletableFuture<Void> future = CompletableFuture.allOf(tasks);
```

## Testing

Comprehensive test suite covering:

- Task creation and status updates
- Sorting algorithm correctness
- Thread-safe concurrent operations
- Edge cases and boundary conditions

Run tests: `./build.sh test`

## Code Quality

- Zero compiler warnings
- Complete encapsulation (private fields)
- Immutable enums for type safety
- Comprehensive input validation
- Professional naming conventions
- Javadoc on public methods

## Performance

### Sorting Complexity

- QuickSort: O(n log n) average, O(n²) worst
- Space complexity: O(log n) for recursion stack

### Concurrency Benefits

- Sequential processing: ~300ms per task × 8 = 2400ms
- Concurrent with 4 threads: ~600ms total
- **4x speedup** with parallel execution

## Future Enhancements

Potential additions:

- Persistence layer with database
- REST API for remote access
- Web-based dashboard
- Task dependencies and workflows
- Notification system
- User authentication

## Learning Outcomes

This project demonstrates:

- Custom sorting algorithm implementation
- Thread-safe concurrent programming
- Clean architecture and SOLID principles
- Builder pattern for complex objects
- Modern Java concurrency APIs

## License

Academic project - MIT License

## Acknowledgments

Built for the Data Structures and Programming course at University of Aveiro - ESTGA.
