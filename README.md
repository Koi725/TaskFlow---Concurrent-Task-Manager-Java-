# TaskFlow - Concurrent Task Manager

A task management system built to learn sorting algorithms and concurrent programming in Java.

## What It Does

TaskFlow helps you organize tasks by priority and deadline, then processes them using multiple threads. Think of it like a to-do list that can handle hundreds of tasks at once without slowing down.

I built this to understand:

- How sorting algorithms work when you implement them yourself
- Why concurrent programming matters for performance
- How to write thread-safe code that won't crash when multiple users access it

## Quick Start

```bash
# Run everything
chmod +x build.sh
./build.sh all
```

That's it. The script compiles the code, runs tests, and shows a demo.

## What You'll See

**Part 1: Sorting**
Creates 12 random tasks and sorts them three different ways:

- By priority (urgent tasks first)
- By deadline (earliest first)
- Combined (urgent AND soon)

**Part 2: Concurrent Processing**
Processes 20 tasks simultaneously using 4 threads. Shows the speed difference between doing things one-by-one versus parallel.

**Part 3: Statistics**
Shows how many tasks are pending, in progress, or completed.

## Requirements (What Professor Asked For)

### 1. Sorting (Ordenação)

Built QuickSort from scratch. No Arrays.sort() or Collections.sort().

Why QuickSort? It's fast (O(n log n) average) and teaches you about divide-and-conquer algorithms. I can explain exactly how the partition step works because I wrote it myself.

### 2. Concurrency (Programação Concorrente)

Multiple threads can read and write tasks at the same time without corrupting data.

How? Three techniques:

- `ConcurrentHashMap` for the main storage
- `ReadWriteLock` so many readers don't block each other
- `synchronized` methods on the Task class itself

Also uses `ExecutorService` with a thread pool to process tasks in parallel.

## Project Structure

```
src/main/java/com/taskflow/
├── Application.java           # Runs the demo
├── core/
│   ├── domain/               # Task, Priority, Status classes
│   └── service/              # Main business logic
├── sorting/                  # QuickSort + comparators
├── concurrency/              # Thread-safe repository
└── util/                     # Helpers and logging
```

Everything's organized by what it does, not by technical patterns.

## Technical Details

### Custom Sorting

Implemented QuickSort with a partition function. You can sort by:

- Priority level (Critical > High > Medium > Low)
- Deadline (soonest first)
- Both (high priority tasks with near deadlines first)

The comparator pattern lets you plug in different sorting strategies without changing the algorithm.

### Thread Safety

The repository uses `ReadWriteLock` which is smarter than just `synchronized`:

- Multiple threads can read at once (fast)
- Only one thread can write at a time (safe)
- Readers don't block other readers

Tasks use `synchronized` on status updates because multiple threads might try to change the same task.

### Concurrent Processing

The `TaskProcessor` uses an `ExecutorService` with 4 worker threads:

- Sequential: 20 tasks × 300ms = 6000ms
- Parallel (4 threads): ~1500ms
- **4x faster** because tasks run simultaneously

## Building Manually

If the script doesn't work:

```bash
# Compile
find src/main/java -name "*.java" > sources.txt
javac -d build/classes -sourcepath src/main/java @sources.txt

# Run
java -cp build/classes com.taskflow.Application
```

## Testing

Unit tests cover:

- Task creation and updates
- Sorting correctness (is highest priority actually first?)
- Thread safety (do 10 threads adding tasks give us 10 tasks?)

Run tests:

```bash
./build.sh test
```

All tests use a custom assertion framework. No JUnit dependency because I wanted to understand how testing frameworks work under the hood.

## Debugging

Check `logs.txt` after running. It shows:

- Every task created
- Sorting operations
- Thread processing timeline
- Any errors with stack traces

The logger automatically creates a fresh log file each run.

## What I Learned

**Sorting algorithms aren't just theory.** When you have to sort tasks by priority, you realize why QuickSort's in-place partitioning matters. No wasted memory creating new arrays.

**Concurrency is hard but worth it.** My first version had race conditions. Tasks would disappear or get corrupted. Learning about locks and atomic operations fixed that.

**Tests catch bugs before users do.** The concurrent test that spawns 10 threads revealed a bug I never would've found manually.

## Design Decisions

**Why Builder Pattern for Tasks?**
Tasks have 7 fields. Constructor with 7 parameters is ugly. Builder makes it readable:

```java
Task.builder()
    .title("Fix bug")
    .priority(Priority.HIGH)
    .deadline(tomorrow)
    .build();
```

**Why ReadWriteLock instead of synchronized?**
Most operations are reads (checking task status). ReadWriteLock lets multiple threads read simultaneously. Way faster for read-heavy workloads.

**Why Custom QuickSort instead of using Collections.sort()?**
Because then I'd learn nothing. Implementing it yourself teaches you about:

- Choosing a pivot
- Partitioning elements
- Recursion depth
- When it's faster than MergeSort

## Performance

Tested with 1000 tasks:

- QuickSort: ~15ms
- Finding by status (ConcurrentHashMap): <1ms
- Concurrent processing (4 threads): 4x faster than sequential
