package cpuscheduler.algorithms;

import cpuscheduler.SchedulerResult;
import cpuscheduler.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class SchedulingAlgorithm {
    /**
     * Gets the display name of the algorithm.
     * @return The name of the algorithm.
     */
    public abstract String getName();

    /**
     * Schedules the given list of processes.
     * The input list should be treated as read-only or copied if modifications are needed internally,
     * to allow running multiple algorithms on the same initial set of processes.
     * @param processes A list of Process objects to be scheduled.
     * @return A SchedulerResult object containing the completed processes and scheduling metrics.
     */
    public SchedulerResult schedule(List<Process> processes) {
        Queue<Process> readyQueue = getProcesses(processes);

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        StringBuilder ganttChartBuilder = new StringBuilder("|");
        StringBuilder ganttTimingBuilder = new StringBuilder("0");

        System.out.printf("\n--- {%s} Scheduling Logic ---\n", getName());
        System.out.println("Process | Burst | Arrival | Completion | Response | Turnaround | Waiting");


        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
            currentProcess.setState("Running"); // Conceptual state during its execution turn

            currentProcess.setStartTime(currentTime);
            currentProcess.setCompletionTime(currentTime + currentProcess.getBurstTime());
            currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

            currentTime = currentProcess.getCompletionTime();
            currentProcess.setState("Terminated");
            completedProcesses.add(currentProcess);

            // Build Gantt chart strings
            ganttChartBuilder
                    .append(" P")
                    .append(currentProcess.getProcessId())
                    .append(" (")
                    .append(currentProcess.getBurstTime())
                    .append(") |");
            ganttTimingBuilder.append(String.format("%" + ((" P" + currentProcess.getProcessId() + " (" + currentProcess.getBurstTime() + ") ").length() + 1) + "s", currentTime));

            System.out.printf("P%-7d | %-5d | %-7d | %-10d | %-7d | %-10d | %-7d\n",
                    currentProcess.getProcessId(),
                    currentProcess.getBurstTime(),
                    currentProcess.getArrivalTime(),
                    currentProcess.getCompletionTime(),
                    currentProcess.getResponseTime(),
                    currentProcess.getTurnaroundTime(),
                    currentProcess.getWaitingTime());
        }

        String ganttChartOutput = generateStatistics(completedProcesses, ganttChartBuilder, ganttTimingBuilder);

        return new SchedulerResult(completedProcesses, ganttChartOutput);
    }

    /**
     * Generates statistics for the completed processes and builds the Gantt chart output.
     * @param completedProcesses A list of completed Process objects.
     * @param ganttChartBuilder A StringBuilder for the Gantt chart.
     * @param ganttTimingBuilder A StringBuilder for the timing of the Gantt chart.
     * @return A string containing the Gantt chart and statistics.
     */
    protected String generateStatistics(List<Process> completedProcesses, StringBuilder ganttChartBuilder, StringBuilder ganttTimingBuilder) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        double totalResponseTime = 0;
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
            totalResponseTime += p.getResponseTime();
        }

        double avgWaitingTime = completedProcesses.isEmpty() ? 0 : totalWaitingTime / completedProcesses.size();
        double avgTurnaroundTime = completedProcesses.isEmpty() ? 0 : totalTurnaroundTime / completedProcesses.size();
        double avgResponseTime = completedProcesses.isEmpty() ? 0 : totalResponseTime / completedProcesses.size();

        System.out.printf("\nAverage Waiting Time (%s): %.2f\n", getName(), avgWaitingTime);
        System.out.printf("Average Turnaround Time (%s): %.2f\n", getName(), avgTurnaroundTime);
        System.out.printf("Average Response Time (%s): %.2f\n", getName(), avgResponseTime);


        String ganttChartOutput = ganttChartBuilder + "\n" + ganttTimingBuilder;
        ganttChartOutput += String.format("\n\nAverage Waiting Time: %.2f", avgWaitingTime);
        ganttChartOutput += String.format("\nAverage Turnaround Time: %.2f", avgTurnaroundTime);
        ganttChartOutput += String.format("\nAverage Response Time: %.2f", avgResponseTime);

        return ganttChartOutput;
    }

    /**
     * Gets the processes in a queue for scheduling.
     * @param processes A list of Process objects to be scheduled.
     * @return A Queue of Process objects ready for scheduling.
     */
    protected Queue<Process> getProcesses(List<Process> processes) {
        List<Process> localProcessList = cloneProcesses(processes);

        return new LinkedList<>(localProcessList);
    }

    protected List<Process> cloneProcesses(List<Process> processes) {
        List<Process> localProcessList = new ArrayList<>();
        for (Process p : processes) {
            Process processCopy = new Process(p.getProcessId(), p.getBurstTime(), p.getPriority());
            localProcessList.add(processCopy);
        }
        return localProcessList;
    }
}
