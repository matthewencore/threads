package executor;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class CustomThreadPool implements CustomExecutor {
    private final int corePoolSize;
    private final int maxPoolSize;
    private final int queueSize;
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final int minSpareThreads;

    private final TaskQueue queue;
    private final List<Worker> workers = new ArrayList<>();
    private final ThreadEncoreFactory threadFactory = new ThreadEncoreFactory();
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    private final Logger logger = Logger.getLogger("ThreadPool");

    public CustomThreadPool(int corePoolSize, int maxPoolSize, int queueSize,
                            long keepAliveTime, TimeUnit timeUnit, int minSpareThreads) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.queueSize = queueSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.minSpareThreads = minSpareThreads;
        this.queue = new TaskQueue(queueSize);

        for (int i = 0; i < corePoolSize; i++) {
            addWorker();
        }
    }

    private void addWorker() {
        if (workers.size() >= maxPoolSize) return;
        Worker worker = new Worker(queue, keepAliveTime, timeUnit, this);
        workers.add(worker);
        threadFactory.newThread(worker).start();
    }

    @Override
    public void execute(Runnable command) {
        if (isShutdown.get()) {
            logger.warning("[Rejected] Task was rejected due to shutdown.");
            return;
        }

        boolean added = queue.offer(command);
        if (!added) {
            if (workers.size() < maxPoolSize) {
                addWorker();
                queue.offer(command);
            } else {
                logger.warning("[Rejected] Task was rejected due to overload!");
            }
        } else {
            logger.info("[Pool] Task accepted into queue: " + command);
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        FutureTask<T> task = new FutureTask<>(callable);
        execute(task);
        return task;
    }

    @Override
    public void shutdown() {
        isShutdown.set(true);
        workers.forEach(Thread::interrupt);
    }

    @Override
    public void shutdownNow() {
        isShutdown.set(true);
        workers.forEach(Thread::interrupt);
    }

    public boolean isShutdown() {
        return isShutdown.get();
    }
}
