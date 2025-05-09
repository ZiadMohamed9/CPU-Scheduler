package cpuscheduler.algorithms;

import cpuscheduler.Process;

import java.util.*;

public class PrioritySchedulingAlgorithm extends SchedulingAlgorithm {
    @Override
    public String getName() {
        return "Priority non-preemptive";
    }

    @Override
    protected Queue<Process> getProcesses(List<Process> processes) {
        List<Process> localProcessList = cloneProcesses(processes);

        // Sort the processes by priority (lower number = higher priority)
        localProcessList.sort(Comparator.comparingInt(Process::getPriority));

        return new LinkedList<>(localProcessList);
    }
}
