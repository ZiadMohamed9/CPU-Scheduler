package cpuscheduler;

public class Process {
    private final int processId;
    private final int priority;
    private final int arrivalTime;
    private final int burstTime;
    private int startTime;
    private String state;

    // Calculated values
    private int responseTime;
    private int waitingTime;
    private int turnaroundTime;
    private int completionTime;

    public Process(int processId, int burstTime, int priority) {
        this.processId = processId;
        this.burstTime = burstTime;
        this.priority = priority;
        this.arrivalTime = 0; // Requirement: all processes arrive at time 0
        this.state = "Ready"; // Initial state
    }

    // Getters
    public int getProcessId() {
        return processId;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public String getState() {
        return state;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    // Setters
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    @Override
    public String toString() {
        return "P" + processId + " (BT:" + burstTime + ", Prio:" + priority + ")";
    }


}