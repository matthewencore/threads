package executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ThreadEncoreFactory implements ThreadFactory {
    private static final Logger logger = Logger.getLogger("ThreadPool");
    private final AtomicInteger counter = new AtomicInteger(0);
    private final String namePrefix = "MyPool-worker-";

    @Override
    public Thread newThread(Runnable r) {
        String name = namePrefix + counter.incrementAndGet();
        Thread thread = new Thread(r, name);
        logger.info("[ThreadFactory] Creating new thread: " + name);
        return thread;
    }
}
