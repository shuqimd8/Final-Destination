package com.learneria.utils;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;

/**
 * Ensures JavaFX platform is initialized before any test that touches JavaFX UI classes.
 */
public abstract class JavaFXTestBase {

    @BeforeAll
    static void initJavaFX() throws Exception {
        if (!Platform.isFxApplicationThread() && !Platform.isImplicitExit()) {
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                Platform.startup(latch::countDown);
            }, "JavaFX Init Thread").start();
            latch.await();
        }
    }
}
