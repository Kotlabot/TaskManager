# Task Manager (Java Console Application)

## Overview

Task manager is a simple console-based application written in Java, which allows users to manage personal tasks. It contains following features: creating, editing and seleting tasks, searching tasks and sorting of tasks, saving tasks to files (in TXT or JSON format) and loading tasks from files.

## Documentation
- User documentation is available in `TaskManagerDocumentation.pdf`
- Developer documentation is provided in the source code as Javadoc comments (generated Javadoc HTML is not included in the repository).

## Features

The application provides following features:

### Task management
- Add new tasks
- Delete existing tasks
- Edit existing tasks (name, description, deadline, priority, type, completion status)

### Task viewing
- Print all tasks
- Sort tasks by priority
- Sort tasks by deadline
- Filter tasks by type
- Search tasks by keyword (with highlighted matches)

### Statistics
- Total number of tasks
- Number of completed and incompleted tasks
- Number of upcoming and overdue tasks
- Average priority
- Distribution of tasks by type

### File operations
- Save tasks to TXT file
- Load tasks from TXT file
- Save tasks to JSON file (Jackson library)
- Load tasks from JSON file

## Technologies used
- Java 25
- Maven
- Jackson (JSON serialization)
- JUnit 6 (for testing)
- Standard Java libraries:
  - Collections (List, Map)
  - Streams
  - Date and Time API (LocalDate)

## How to run

### Using IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Ensure Maven dependencies are loaded
3. Run `Main.java`

### Using Maven
```bash
mvn compile
mvn exec:java