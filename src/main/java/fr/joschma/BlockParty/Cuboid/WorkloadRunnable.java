package fr.joschma.BlockParty.Cuboid;

import fr.joschma.BlockParty.Arena.Arena;
import org.bukkit.Bukkit;

import java.util.ArrayDeque;
import java.util.Deque;

public class WorkloadRunnable implements Runnable {
    private static double MAX_MILLIS_PER_TICK = 2.5;
    private static final int MAX_NANOS_PER_TICK = (int) (MAX_MILLIS_PER_TICK * 1E6);
    public Deque<PlacableBlock> workloadDeque = new ArrayDeque<PlacableBlock>();
    Arena a;

    public WorkloadRunnable (Arena a) {
        this.MAX_MILLIS_PER_TICK = a.getMaxMillisSecondPerTickToGenerateArenaDanceFloor();
        this.a = a;
    }

    public void addWorkload(PlacableBlock workload) {
        this.workloadDeque.add(workload);
    }

    @Override
    public void run() {
        long stopTime = System.nanoTime() + MAX_NANOS_PER_TICK;
        PlacableBlock nextLoad;
        // Note: Don't permute the conditions because sometimes the time will be over but the queue will still be polled then.
        while (System.nanoTime() <= stopTime && (nextLoad = this.workloadDeque.poll()) != null) {
            nextLoad.compute();
        }
    }
}
