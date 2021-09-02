/**
 * Non-preemptive SJF (Shortest-Job First) scheduling algorithm.
 *
 */

import java.util.*;

public class SJF implements Algorithm
{
    private final LinkedList<Process> readyQueue;
    private final Queue<Process> processesToSchedule;
    private final int totalNumProcesses;

    public SJF(List<Process> allProcessList) {
        readyQueue = new LinkedList<>();
        processesToSchedule = new LinkedList<>();
        totalNumProcesses = allProcessList.size();
        processesToSchedule.addAll(allProcessList);
    }

    public void schedule() {
        int totalWaitingTime = 0;
        Process currentProcess;
        Process p = processesToSchedule.remove();
        if (CPU.getCurrentTime() < p.getArrivalTime()) {
            CPU.advanceTimeTo(p.getArrivalTime());
        }
        readyQueue.add(p);
        while (!readyQueue.isEmpty()) {
            currentProcess = pickNextProcess();
            int wTime = 0;
            if (CPU.getCurrentTime() > currentProcess.getArrivalTime()){
                wTime = CPU.getCurrentTime() - currentProcess.getArrivalTime();
            }
            totalWaitingTime += wTime;
            CPU.run(currentProcess, currentProcess.getCPUBurstTime());
            System.out.println(currentProcess.getName() + " finished at time "+CPU.getCurrentTime() + ". Its waiting time is: " + wTime);
            while (!processesToSchedule.isEmpty()){
                p = processesToSchedule.peek();
                if (p.getArrivalTime() <= CPU.getCurrentTime()) {
                    readyQueue.add(processesToSchedule.remove());
                }else{
                    if (readyQueue.isEmpty()) {
                        CPU.advanceTimeTo(p.getArrivalTime());
                        readyQueue.add(processesToSchedule.remove());
                    }
                    break;
                }
            }
        }
        double averageWaitingTime = totalWaitingTime / (double) totalNumProcesses ;
        System.out.printf("\nThe average waiting time is: %.2f\n", averageWaitingTime);
    }

    public Process pickNextProcess() {
        Process nextProcess = readyQueue.get(0);
        for (Process p : readyQueue) {
            if (p.getArrivalTime() < nextProcess.getArrivalTime()) {
                nextProcess = p;
            }
        }
        readyQueue.remove(nextProcess);
        return nextProcess;
    }
}
