import executor.CustomThreadPool;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool pool = new CustomThreadPool(
                2, 4, 5,
                5, TimeUnit.SECONDS, 1
        );

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.execute(() -> {
                try {
                    System.out.println("Running task #" + taskId);
                    Thread.sleep(3000);
                    System.out.println("Finished task #" + taskId);
                } catch (InterruptedException ignored) {}
            });
        }

        Thread.sleep(15000);
        pool.shutdown();
    }
}
