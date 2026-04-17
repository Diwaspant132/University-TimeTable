#  UniScheduler Pro — Zero Dependency Edition

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing/AWT-blue?style=for-the-badge)
![Zero Dependencies](https://img.shields.io/badge/Dependencies-None-brightgreen?style=for-the-badge)

A modern, glassmorphism-themed **University Timetable Generator** built completely in standard Java. Designed with a sleek aesthetic, this application natively utilizes Java's AWT/Swing combined with advanced rendering techniques to provide a professional user experience. 

It completely eliminates external database dependencies by storing states securely in local serialized objects (`timetable_data.ser`), ensuring absolute plug-and-play simplicity.

##  Features

- **Modern Glassmorphism UI:** Complete visual overhaul of classic Swing components, featuring transparency, blur effects, gradients, and a sleek dark/light mode adaptable feel. 
- ** Custom Mappings:** Advanced professor-to-subject mapping specifically configured for the **Computer Science** and **Electronics** departments.
- ** Room Assignments:** Department-specific room allocation and collision detection routines.
- ** Zero Setup Data Persistence:** No SQL or JDBC required! Data naturally persists between runs gracefully using Java's built-in serialization. It's fully seeded out of the box.
- ** Modular Architecture:** Cleanly organized into a 3-tier architecture to simulate collaborative development.

##  Quick Start

### 1. Run in VS Code (Recommended)
Clone the repository and open the folder in VS Code:
```bash
code UniTimetable_NoDB
```
Open `src/timetable/MainApp.java` and click the **▶ Run** button (or press `F5`).

### 2. Run via Terminal / Command Line
If you prefer the command line, simply compile and run:
```bash
# Compile the components
javac -d out src/timetable/*.java

# Start the application
java -cp out timetable.MainApp
```

##  Project Structure

This project uses a modular design approach, cleanly mapping responsibilities across 3 core sectors:

```text
UniTimetable_NoDB/
├── src/timetable/
│   ├── MainApp.java          ← Programmer 1 · User Interface & Theming
│   ├── SchedulingModule.java ← Programmer 2 · Scheduling Logic & Collision Handling
│   └── DatabaseModule.java   ← Programmer 3 · Local File Serialization & Data Access
├── .vscode/                  ← Local VS Code Workspace Configurations
└── README.md                 ← Project Documentation
