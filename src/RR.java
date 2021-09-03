/**
 * RR (Round Robin) scheduling algorithm.
 *
 */
 
import java.util.*;

public class RR implements Algorithm
{
    private final Queue<Process> readyQueue;
    private final Queue<Process> processesToSchedule;
    private final HashMap<String, Integer> originalBursts;
    private int totalNumProcesses;

    public RR(List<Process> allProcessList) {
        readyQueue = new LinkedList<>();
        processesToSchedule = new LinkedList<>();
        originalBursts = new HashMap<>();
        totalNumProcesses = allProcessList.size();
        processesToSchedule.addAll(allProcessList);
    }

    public void schedule() {
        System.out.println("Round Robin scheduling");
        totalNumProcesses = processesToSchedule.size();
        int totalWaitingTime = 0;
        Process p = processesToSchedule.remove();
        if (CPU.getCurrentTime() < p.getArrivalTime()) {
            CPU.advanceTimeTo(p.getArrivalTime());
        }
        readyQueue.add(p);
        while (!readyQueue.isEmpty()) {
            Process currentProcess = pickNextProcess();
            if (!originalBursts.containsKey(currentProcess.getName())) {
                originalBursts.put(currentProcess.getName(), currentProcess.getCPUBurstTime());
            }
            int timeQuantum = 10;
            CPU.run(currentProcess, Math.min(currentProcess.getCPUBurstTime(), timeQuantum));
            currentProcess.setBurst(currentProcess.getCPUBurstTime() - timeQuantum);
            for (Process q : processesToSchedule) {
                if (CPU.getCurrentTime() > q.getArrivalTime()) {
                    readyQueue.add(q);
                }
            }
            totalWaitingTime += CPU.getCurrentTime() - currentProcess.getArrivalTime() - originalBursts.get(currentProcess.getName());;
            processesToSchedule.removeAll(readyQueue);
            if (currentProcess.getCPUBurstTime() <= 0) {
                System.out.println(currentProcess.getName() + " has finished at time " + CPU.getCurrentTime() + " with a waiting time of " +
                        (CPU.getCurrentTime() - currentProcess.getArrivalTime() - originalBursts.get(currentProcess.getName())));
            } else {
                readyQueue.add(currentProcess);
            }
            if (readyQueue.isEmpty()) {
                for (Process r : processesToSchedule) {
                    if (r.getArrivalTime() > CPU.getCurrentTime()) {
                        CPU.advanceTimeTo(r.getArrivalTime());
                        readyQueue.add(processesToSchedule.remove());
                    } break;
                }
            }
        }
        System.out.printf("The average waiting time is %.2f\n", (double) totalWaitingTime / totalNumProcesses);
    }

    public Process pickNextProcess() {
        return readyQueue.remove();
    }
}
