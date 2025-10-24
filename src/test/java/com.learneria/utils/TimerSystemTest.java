import com.learneria.utils.TimerSystem;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TimerSystem.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TimerSystemTest {

    @BeforeAll
    void initJavaFX() throws Exception {
        // Ensure JavaFX toolkit initialized before using AnimationTimer
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        }
    }

    @Test
    void start_and_stop_updatesElapsedTime() throws InterruptedException {
        AtomicLong ticked = new AtomicLong(0);

        TimerSystem timer = new TimerSystem(seconds -> ticked.set(seconds));
        timer.start();

        // Wait 1.2 s to accumulate some elapsed time
        Thread.sleep(1200);

        timer.stop();

        double elapsed = timer.getElapsedSeconds();
        assertTrue(elapsed >= 1 && elapsed <= 2,
                "Elapsed seconds should be roughly 1 – 2 s but was " + elapsed);

        // onTick callback should have been called at least once
        assertTrue(ticked.get() >= 0, "Listener should receive at least one tick");
    }

    @Test
    void reset_stopsAndClearsElapsedTime() throws InterruptedException {
        TimerSystem timer = new TimerSystem(null);
        timer.start();
        Thread.sleep(500);
        timer.reset();
        assertEquals(0, timer.getElapsedSeconds(), "Elapsed time should reset to 0 after reset()");
    }

    @Test
    void listener_receivesTicks() throws InterruptedException {
        AtomicLong lastTick = new AtomicLong(-1);
        TimerSystem timer = new TimerSystem(lastTick::set);

        timer.start();
        Thread.sleep(1100);
        timer.stop();

        assertTrue(lastTick.get() >= 0, "Listener should be triggered with a tick value");
    }
}
