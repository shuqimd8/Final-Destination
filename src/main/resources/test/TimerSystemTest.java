import com.learneria.utils.TimerSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TimerSystemTest {

    private TimerSystem timer;
    private long[] ticks;

    @BeforeAll
    static void initJavaFX() throws Exception {
        // Ensures JavaFX runtime starts once for all tests
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await(2, TimeUnit.SECONDS);
    }

    @BeforeEach
    void setup() {
        ticks = new long[1];
        timer = new TimerSystem(seconds -> ticks[0] = seconds);
    }

    @Test
    void testStartInitializesTimerAndRunningTrue() throws Exception {
        assertDoesNotThrow(() -> timer.start());
        assertTrue(getPrivateBoolean(timer, "running"), "Timer should be running after start()");
        assertNotNull(getPrivateObject(timer, "timer"), "AnimationTimer should be initialized");
        timer.stop();
    }

    @Test
    void testStopHaltsTimerAndSetsRunningFalse() throws Exception {
        timer.start();
        timer.stop();
        assertFalse(getPrivateBoolean(timer, "running"), "Timer should stop after stop()");
    }

    @Test
    void testResetStopsAndResetsElapsedTime() throws Exception {
        timer.start();
        timer.stop();
        setPrivateField(timer, "elapsedTime", 10L);
        timer.reset();
        assertEquals(0, timer.getElapsedSeconds(), "Elapsed time should reset to 0");
        assertFalse(getPrivateBoolean(timer, "running"), "Timer should not be running after reset()");
    }

    @Test
    void testOnTickListenerReceivesElapsedUpdates() throws Exception {
        timer.start();
        AnimationTimer fxTimer = (AnimationTimer) getPrivateObject(timer, "timer");
        long fakeStart = (long) getPrivateObject(timer, "startTime");

        fxTimer.handle(fakeStart + 5_000_000_000L); // +5 seconds
        assertTrue(ticks[0] >= 5, "Listener should receive seconds >= 5");

        fxTimer.handle(fakeStart + 10_000_000_000L); // +10 seconds
        assertTrue(ticks[0] >= 10, "Listener should receive seconds >= 10");
    }

    @Test
    void testGetElapsedSecondsReflectsManualHandleCalls() throws Exception {
        timer.start();
        AnimationTimer fxTimer = (AnimationTimer) getPrivateObject(timer, "timer");
        long fakeStart = (long) getPrivateObject(timer, "startTime");
        fxTimer.handle(fakeStart + 7_000_000_000L);
        assertEquals(7, timer.getElapsedSeconds(), "Elapsed time should be about 7s");
    }

    // ===== Reflection helpers =====
    private static Object getPrivateObject(Object obj, String fieldName) throws Exception {
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(obj);
    }

    private static boolean getPrivateBoolean(Object obj, String fieldName) throws Exception {
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.getBoolean(obj);
    }

    private static void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(obj, value);
    }
}
