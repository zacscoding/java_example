package demo;

import com.github.dockerjava.api.async.ResultCallback;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class NoStreamCallback<A_RES_T> implements ResultCallback<A_RES_T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoStreamCallback.class);

    private Closeable stream;
    private boolean closed = false;
    private A_RES_T object;
    private Throwable firstError = null;
    private final CountDownLatch next = new CountDownLatch(1);


    @Override
    public void onStart(Closeable stream) {
        this.stream = stream;
        this.closed = false;
    }

    @Override
    public void onNext(A_RES_T object) {
        this.object = object;
        next.countDown();
        onComplete();
    }

    @Override
    public void onError(Throwable throwable) {
        if (closed) {
            return;
        }

        if (firstError == null) {
            firstError = throwable;
        }

        try {
            LOGGER.error("Error during callback", throwable);
        } finally {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onComplete() {
        try {
            LOGGER.info("onComplete is called..");
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            if (stream != null) {
                LOGGER.info("Try to close stream");
                stream.close();
            }
        }
    }

    public A_RES_T awaitNext() throws InterruptedException {
        next.await();
        return object;
    }
}
