package cpuscheduler.algorithms;

import cpuscheduler.Process;
import cpuscheduler.SchedulerResult;

import java.util.*;

public class PrioritySchedulingAlgorithm implements SchedulingAlgorithm {
    @Override
    public String getName() {
        return "Priority non-preemptive";
    }

    @Override
    public SchedulerResult schedule(List<Process> processes) {
        Queue<Process> readyQueue = getProcesses(processes);

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        StringBuilder ganttChartBuilder = new StringBuilder("|");
        StringBuilder ganttTimingBuilder = new StringBuilder("0");

        System.out.println("\n--- Priority non-preemptive Scheduling Logic ---");
        System.out.println("Process | Burst | Arrival | Completion | Response | Turnaround | Waiting");


        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
            currentProcess.setState("Running"); // Conceptual state during its execution turn

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

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }

        double avgWaitingTime = completedProcesses.isEmpty() ? 0 : totalWaitingTime / completedProcesses.size();
        double avgTurnaroundTime = completedProcesses.isEmpty() ? 0 : totalTurnaroundTime / completedProcesses.size();

        System.out.printf("\nAverage Waiting Time (Priority non-preemptive Logic): %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time (Priority non-preemptive Logic): %.2f\n", avgTurnaroundTime);


        String ganttChartOutput = ganttChartBuilder + "\n" + ganttTimingBuilder;
        ganttChartOutput += String.format("\n\nAverage Waiting Time: %.2f", avgWaitingTime);
        ganttChartOutput += String.format("\nAverage Turnaround Time: %.2f", avgTurnaroundTime);

        return new SchedulerResult(completedProcesses, ganttChartOutput, avgWaitingTime, avgTurnaroundTime);
    }

    private Queue<Process> getProcesses(List<Process> processes) {
        List<Process> localProcessList = new ArrayList<>();
        for (Process p : processes) {
            Process processCopy = new Process(p.getProcessId(), p.getBurstTime(), p.getPriority());
            localProcessList.add(processCopy);
        }

        // Sort the processes by burst time
        localProcessList.sort(Comparator.comparingInt(Process::getPriority));

        return new LinkedList<>(localProcessList);
    }
}
