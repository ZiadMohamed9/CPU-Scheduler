package cpuscheduler;

import java.util.List;

/**
 * @param ganttChart This string can include the chart and average times
 */
public record SchedulerResult(List<Process> completedProcesses, String ganttChart) {
}
