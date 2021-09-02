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

    public RR(List<Process> allProcessList) {
        readyQueue = new LinkedList<>();
        processesToSchedule = new LinkedList<>();
        totalNumProcesses = allProcessList.size();
        processesToSchedule.addAll(allProcessList);
    }

    public void schedule() {

    }

    public Process pickNextProcess() {
        return readyQueue.remove();
    }
}
