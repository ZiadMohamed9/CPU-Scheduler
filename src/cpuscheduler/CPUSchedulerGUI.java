package cpuscheduler;

import cpuscheduler.algorithms.FCFSSchedulingAlgorithm;
import cpuscheduler.algorithms.SchedulingAlgorithm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPUSchedulerGUI extends JFrame {

    // Input fields
    private final JTextField tfProcessId;
    private final JTextField tfBurstTime;
    private final JTextField tfPriority;

    // Algorithm selection
    private final JComboBox<String> algorithmComboBox;
    private final Map<String, SchedulingAlgorithm> availableAlgorithms;

    private final DefaultTableModel inputTableModel;

    private final DefaultTableModel resultsTableModel;
    private final JTextArea ganttChartArea;

    private final List<Process> processList; // Stores processes added by the user
    private int nextProcessId = 1;

    public CPUSchedulerGUI() {
        super("CPU Scheduler Simulator"); // Updated title
        processList = new ArrayList<>();
        availableAlgorithms = new HashMap<>();
        loadAlgorithms(); // Initialize available algorithms

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Process ID:"), gbc);
        tfProcessId = new JTextField(5);
        tfProcessId.setText(String.valueOf(nextProcessId));
        tfProcessId.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(tfProcessId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("CPU Time (Burst):"), gbc);
        tfBurstTime = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(tfBurstTime, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Priority:"), gbc);
        tfPriority = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(tfPriority, gbc);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        JButton btnAddProcess = new JButton("Add Process");
        JButton btnRunScheduler = new JButton("Run Selected Algorithm");
        btnRunScheduler.setBackground(new Color(100, 180, 100));
        btnRunScheduler.setForeground(Color.WHITE);
        btnRunScheduler.setOpaque(true);
        btnRunScheduler.setBorderPainted(false);
        buttonPanel.add(btnAddProcess);
        buttonPanel.add(btnRunScheduler);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        // --- Algorithm Selection Panel ---
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Select Algorithm:"));
        algorithmComboBox = new JComboBox<>(availableAlgorithms.keySet().toArray(new String[0]));
        if (!availableAlgorithms.isEmpty()) {
            algorithmComboBox.setSelectedIndex(0); // Select first algorithm by default
        }
        selectionPanel.add(algorithmComboBox);


        // --- Process Input Table ---
        String[] inputColumnNames = {"ID", "Burst Time", "Priority", "Arrival Time"};
        inputTableModel = new DefaultTableModel(inputColumnNames, 0);
        // Table to display added processes
        JTable processInputTable = new JTable(inputTableModel);
        JScrollPane inputTableScrollPane = new JScrollPane(processInputTable);
        inputTableScrollPane.setBorder(BorderFactory.createTitledBorder("Process Queue (Arrival Time 0)"));

        // --- Results Panel ---
        JPanel resultsPanel = new JPanel(new BorderLayout(10,10));
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Scheduling Results"));

        String[] resultsColumnNames = {"P.ID", "Burst", "Priority", "Arrival", "Completion", "Response", "Waiting", "Turnaround", "State"};
        resultsTableModel = new DefaultTableModel(resultsColumnNames, 0);
        JTable resultsTable = new JTable(resultsTableModel);
        JScrollPane resultsTableScrollPane = new JScrollPane(resultsTable);
        resultsTableScrollPane.setPreferredSize(new Dimension(700, 200)); // Set preferred size

        ganttChartArea = new JTextArea(7, 40);
        ganttChartArea.setEditable(false);
        ganttChartArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartArea);
        ganttScrollPane.setBorder(BorderFactory.createTitledBorder("Gantt Chart & Averages"));
        ganttScrollPane.setPreferredSize(new Dimension(700, 150));

        resultsPanel.add(resultsTableScrollPane, BorderLayout.CENTER);
        resultsPanel.add(ganttScrollPane, BorderLayout.SOUTH);


        // --- Main Layout ---
        setLayout(new BorderLayout(10, 10));

        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(selectionPanel, BorderLayout.NORTH);
        northContainer.add(inputPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(northContainer, BorderLayout.WEST);
        topPanel.add(inputTableScrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnAddProcess.addActionListener(_ -> addProcess());

        btnRunScheduler.addActionListener(_ -> runSelectedAlgorithm());

        // Frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadAlgorithms() {
        // Add FCFS
        SchedulingAlgorithm fcfs = new FCFSSchedulingAlgorithm();
        availableAlgorithms.put(fcfs.getName(), fcfs);

        // To add more algorithms later:
        // SchedulingAlgorithm sjf = new ShortestJobFirstAlgorithm(); // Example
        // availableAlgorithms.put(sjf.getName(), sjf);
        // ... and so on
        // Update JComboBox if algorithms are loaded after GUI initialization (not the case here)
    }

    private void addProcess() {
        try {
            int burstTime = Integer.parseInt(tfBurstTime.getText().trim());
            int priority = Integer.parseInt(tfPriority.getText().trim());

            if (burstTime <= 0) {
                JOptionPane.showMessageDialog(this, "Burst Time must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Process newProcess = new Process(nextProcessId, burstTime, priority);
            processList.add(newProcess);

            inputTableModel.addRow(new Object[]{
                    newProcess.getProcessId(),
                    newProcess.getBurstTime(),
                    newProcess.getPriority(),
                    newProcess.getArrivalTime()
            });

            nextProcessId++;
            tfProcessId.setText(String.valueOf(nextProcessId));
            tfBurstTime.setText("");
            tfPriority.setText("");
            tfBurstTime.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Burst Time and Priority.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runSelectedAlgorithm() {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No processes to schedule. Please add processes first.", "Scheduling Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedAlgoName = (String) algorithmComboBox.getSelectedItem();
        if (selectedAlgoName == null) {
            JOptionPane.showMessageDialog(this, "Please select an algorithm.", "Algorithm Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SchedulingAlgorithm algorithm = availableAlgorithms.get(selectedAlgoName);

        if (algorithm == null) { // Should not happen if ComboBox is populated correctly
            JOptionPane.showMessageDialog(this, "Selected algorithm not found.", "Internal Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // The algorithm implementation (e.g., FCFSSchedulingAlgorithm) now handles creating copies
        // of processes if needed, so we can pass the GUI's processList.
        // Or, for ultimate safety ensure copies are made before calling, as done in FCFS impl.
        SchedulerResult result = algorithm.schedule(new ArrayList<>(processList)); // Pass a copy of the list


        // Clear previous results from GUI
        resultsTableModel.setRowCount(0);
        ganttChartArea.setText("");

        // Populate results table
        if (result != null && result.completedProcesses() != null) {
            for (Process p : result.completedProcesses()) {
                resultsTableModel.addRow(new Object[]{
                        p.getProcessId(),
                        p.getBurstTime(),
                        p.getPriority(),
                        p.getArrivalTime(),
                        p.getCompletionTime(),
                        p.getResponseTime(),
                        p.getWaitingTime(),
                        p.getTurnaroundTime(),
                        p.getState()
                });
            }
            ganttChartArea.setText(result.ganttChart()); // Gantt chart string now includes averages
        } else {
            ganttChartArea.setText("Error: Algorithm did not return valid results.");
        }
    }


    public static void main(String[] args) {
        // Ensure setVisible(true) is called in constructor or here
        SwingUtilities.invokeLater(CPUSchedulerGUI::new);
    }
}