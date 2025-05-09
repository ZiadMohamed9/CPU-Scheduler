package cpuscheduler.algorithms;

import cpuscheduler.Process;
import cpuscheduler.SchedulerResult;

import java.util.*;

public class RoundRobinSchedulingAlgorithm extends SchedulingAlgorithm {
    private static int quantumTime;

    public int getQuantumTime() {
        return quantumTime;
    }

    public void setQuantumTime(int quantumTime) {
        RoundRobinSchedulingAlgorithm.quantumTime = quantumTime;
    }

    @Override
    public String getName() {
        return "Round Robin";
    }

    @Override
    public SchedulerResult schedule(List<Process> processes) {
        Queue<Process> readyQueue = getProcesses(processes);

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int quantumTime = getQuantumTime();

        StringBuilder ganttChartBuilder = new StringBuilder("|");
        StringBuilder ganttTimingBuilder = new StringBuilder("0");

        System.out.println("\n--- Round Robin Scheduling Logic ---");
        System.out.println("Process | Burst | Arrival | Completion | Response | Turnaround | Waiting");

        // Map each process ID to its remaining burst time
        Map<Integer, Integer> processBurstTimeMap = new HashMap<>();
        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
            int currentProcessId = currentProcess.getProcessId();
            int currentProcessBurstTime = currentProcess.getBurstTime();

            if (!processBurstTimeMap.containsKey(currentProcessId)) {
                processBurstTimeMap.put(currentProcessId, currentProcessBurstTime);
                currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setStartTime(currentTime);
            }

            currentProcess.setState("Running");

            int remainingBurstTime = processBurstTimeMap.get(currentProcessId);
            if (remainingBurstTime > quantumTime) {
                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime() - (currentProcessBurstTime - remainingBurstTime));
                processBurstTimeMap.put(currentProcessId, remainingBurstTime - quantumTime);
                currentTime += quantumTime;
                currentProcess.setState("Ready");
                readyQueue.add(currentProcess);
            }
            else {
                currentTime += remainingBurstTime;
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcessBurstTime);
                currentProcess.setState("Terminated");
                completedProcesses.add(currentProcess);
            }

            // Build Gantt chart strings
            ganttChartBuilder
                    .append(" P")
                    .append(currentProcessId)
                    .append(" (")
                    .append(remainingBurstTime)
                    .append(") |");
            ganttTimingBuilder.append(String.format("%" + ((" P" + currentProcessId + " (" + currentProcessBurstTime + ") ").length() + 1) + "s", currentTime));

            System.out.printf("P%-7d | %-5d | %-7d | %-10d | %-7d | %-10d | %-7d | %-10s\n",
                    currentProcessId,
                    remainingBurstTime,
                    currentProcess.getArrivalTime(),
                    currentProcess.getCompletionTime(),
                    currentProcess.getResponseTime(),
                    currentProcess.getTurnaroundTime(),
                    currentProcess.getWaitingTime(),
                    currentProcess.getState());
        }

        String ganttChartOutput = generateStatistics(completedProcesses, ganttChartBuilder, ganttTimingBuilder);

        completedProcesses.sort(Comparator.comparingInt(Process::getProcessId));

        return new SchedulerResult(completedProcesses, ganttChartOutput);
    }
}
