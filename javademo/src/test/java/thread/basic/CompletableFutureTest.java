package thread.basic;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 *
 */
public class CompletableFutureTest {

    private CompletableFuture<String> future = new CompletableFuture<>();

    @Test
    public void testCompletableFuturesThenApplyMethods() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("## Start thread");
                TimeUnit.SECONDS.sleep(1L);
                future.complete("Complete future....");
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        });
        thread.start();

        String result = future.thenApply(s -> {
                String ret = s.toUpperCase();
                System.out.printf("First apply call... %s --> %s\n", s, ret);
                return s.toUpperCase();
            }
        ).thenApply(s -> {
            String ret = s.substring(0, s.length() / 2);
            System.out.printf("Second apply call.... %s --> %s\n", s, ret);
            return ret;
        }).get();
        System.out.println("## Final result :: " + result);
    }
}
