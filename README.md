# CPU Scheduling Simulator

## Overview
The CPU Scheduling Simulator is a Java-based application designed to simulate various CPU scheduling algorithms. It provides a graphical user interface (GUI) for users to input processes, select scheduling algorithms, and visualize the results, including Gantt charts and performance metrics such as average waiting time, turnaround time, and response time.  

## Features

### Supported Scheduling Algorithms:
- First Come First Serve (FCFS)
- Shortest Job First (SJF)
- Priority non-preemptive
- Round Robin (RR)

### Process Management:
- Add processes with attributes such as Process ID, Burst Time, Priority, and Arrival Time.
- Clear all processes with a single button.

### Round Robin Quantum Time:
- Input and configure quantum time for the Round Robin algorithm.

### Results Visualization:
- Display scheduling results in a table.
- Generate Gantt charts for process execution.
- Calculate and display average waiting time, turnaround time, and response time.

## Project Structure
```bash
src/
└── cpuscheduler/
├── algorithms/
│ ├── FCFSSchedulingAlgorithm.java # Implements the First Come First Serve algorithm
│ ├── SJFSchedulingAlgorithm.java # Implements the Shortest Job First algorithm
│ └── RoundRobinSchedulingAlgorithm.java # Implements the Round Robin algorithm
├── Process.java # Represents a process with attributes like burst time, priority, and arrival time
└── SchedulerResult.java # Encapsulates the results of a scheduling algorithm
└── gui/ # Contains the GUI components for user interaction
```

## How to Use

### Add Processes:
1. Enter process details (Burst Time, Priority, Arrival Time) in the input fields.
2. Click the **"Add Process"** button to add the process to the list.

### Select Algorithm:
1. Choose a scheduling algorithm from the dropdown menu.
2. If **"Round Robin"** is selected, input the quantum time in the provided field.

### Run Scheduler:
1. Click the **"Run Selected Algorithm"** button to execute the selected algorithm.
2. View the results in the table and Gantt chart area.

### Clear Processes:
- Click the **"Clear Processes"** button to reset the process list and input fields.

## Requirements
- **Java Development Kit (JDK):** Version 8 or higher.
- **IDE:** IntelliJ IDEA (recommended) or any Java-compatible IDE.

## Installation
```bash
git clone https://github.com/your-username/cpu-scheduling-simulator.git
```
1. Open the project in your preferred IDE.
2. Build and run the project.

## Example Output
- **Gantt Chart:** Visual representation of process execution order.
- **Results Table:** Displays process attributes and calculated metrics:
  - Waiting Time
  - Turnaround Time
  - Response Time

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes and push them to your fork.
4. Submit a pull request.
