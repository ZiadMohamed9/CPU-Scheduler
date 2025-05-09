package cpuscheduler.algorithms;

import cpuscheduler.Process;

import java.util.*;

public class SJFSchedulingAlgorithm extends SchedulingAlgorithm {
    @Override
    public String getName() {
        return "Shortest Job First (SJF)";
    }

    @Override
    protected Queue<Process> getProcesses(List<Process> processes) {
        List<Process> localProcessList = cloneProcesses(processes);

        // Sort the processes by burst time
        localProcessList.sort(Comparator.comparingInt(Process::getBurstTime));

        return new LinkedList<>(localProcessList);
    }
}
