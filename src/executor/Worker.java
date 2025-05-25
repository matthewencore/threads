package executor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Worker extends Thread {
    private final TaskQueue queue;
    private final long keepAliveMillis;
    private final CustomThreadPool pool;
    private static final Logger logger = Logger.getLogger("ThreadPool");

    public Worker(TaskQueue queue, long keepAliveTime, TimeUnit unit, CustomThreadPool pool) {
        this.queue = queue;
        this.keepAliveMillis = unit.toMillis(keepAliveTime);
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            while (!pool.isShutdown()) {
                Runnable task = queue.take();
                if (task != null) {
                    logger.info("[Worker] " + getName() + " executes " + task);
                    task.run();
                }
            }
        } catch (InterruptedException ignored) {
        } finally {
            logger.info("[Worker] " + getName() + " terminated.");
        }
    }
}
