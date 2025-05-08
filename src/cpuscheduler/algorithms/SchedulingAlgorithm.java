package cpuscheduler.algorithms;

import cpuscheduler.SchedulerResult;
import cpuscheduler.Process;

import java.util.List;

public interface SchedulingAlgorithm {
    /**
     * Gets the display name of the algorithm.
     * @return The name of the algorithm.
     */
    String getName();

    /**
     * Schedules the given list of processes.
     * The input list should be treated as read-only or copied if modifications are needed internally,
     * to allow running multiple algorithms on the same initial set of processes.
     * @param processes A list of Process objects to be scheduled.
     * @return A SchedulerResult object containing the completed processes and scheduling metrics.
     */
    SchedulerResult schedule(List<Process> processes);
}
