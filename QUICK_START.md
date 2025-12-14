# Quick Start Guide

Get TaskFlow running in 2 min.

---

## Prerequisites

- Java JDK 11+
- Terminal (Bash/Zsh/Git Bash)

---

## Run It

```bash
# Clone repository
git clone <your-repo-url>
cd taskflow-concurrent-manager

# Make executable
chmod +x build.sh

# Run everything
./build.sh all
```

---

## Expected Output

```
1. SORTING DEMONSTRATION
- 12 tasks sorted by priority
- 12 tasks sorted by deadline
- Combined priority + deadline sorting

2. CONCURRENT PROCESSING
- 8 tasks processed in parallel
- 4 worker threads
- ~600ms total execution time

3. STATISTICS
- Task counts by status
- High-priority task count
- Overdue warnings
```

---

## Commands

```bash
./build.sh compile    # Compile only
./build.sh test       # Run tests
./build.sh run        # Run demo
./build.sh all        # Everything
```

---

## Troubleshooting

**Script won't run:**

```bash
chmod +x build.sh
```

**No javac:**
Install JDK 11+ from oracle.com/java

**Errors:**

```bash
./build.sh clean
./build.sh all
```

---

## Structure

```
src/main/java/com/taskflow/
├── Application.java       # Entry point
├── core/domain/          # Task, Priority, Status
├── sorting/              # QuickSort + Comparators
└── concurrency/          # Thread-safe operations
```

---

## Quick Test

```bash
./build.sh test
```

Should show: `RESULTS: Passed: 3 | Failed: 0`

---

Done!
