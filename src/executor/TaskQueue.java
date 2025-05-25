package executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
    private final BlockingQueue<Runnable> queue;

    public TaskQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public boolean offer(Runnable task) {
        return queue.offer(task);
    }

    public Runnable take() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }
}
