/**
 * RR (Round Robin) scheduling algorithm.
 *
 */
 
import java.util.*;

public class RR implements Algorithm
{
    private final Queue<Process> readyQueue;
    private final Queue<Process> processesToSchedule;
    private final int totalNumProcesses;
    private final int timeQuantum = 5;

    public RR(List<Process> allProcessList) {
        readyQueue = new LinkedList<>();
        processesToSchedule = new LinkedList<>();
        totalNumProcesses = allProcessList.size();
        processesToSchedule.addAll(allProcessList);
    }

    public void schedule() {
        System.out.println("Round Robin scheduling");
        Process currentProcess;
        Process p = processesToSchedule.remove();
        if (CPU.getCurrentTime() < p.getArrivalTime()) {
            CPU.advanceTimeTo(p.getArrivalTime());
        }
        readyQueue.add(p);
        while (!readyQueue.isEmpty()) {
            currentProcess = pickNextProcess();
            if (currentProcess.getCPUBurstTime() < timeQuantum) {
                CPU.run(currentProcess, currentProcess.getCPUBurstTime());
            } else {
                CPU.run(currentProcess, timeQuantum);
            } currentProcess.setBurst(currentProcess.getCPUBurstTime() - timeQuantum);
            for (Process q : processesToSchedule) {
                if (CPU.getCurrentTime() > q.getArrivalTime()) {
                    readyQueue.add(q);
                }
            }
            processesToSchedule.removeAll(readyQueue);
            if (currentProcess.getCPUBurstTime() <= 0) {
                System.out.println(currentProcess.getName() + " has finished at time " + CPU.getCurrentTime());
            } else {
                readyQueue.add(currentProcess);
            }
        }
    }

    public Process pickNextProcess() {
        return readyQueue.remove();
    }
}
