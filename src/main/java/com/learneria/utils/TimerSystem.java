package com.learneria.utils;

import javafx.animation.AnimationTimer;

/**
 * The {@code TimerSystem} class provides a simple real-time timer mechanism
 * for use in Papa’s Learneria mini-games and interactive sessions.
 * <p>
 * It uses JavaFX’s {@link AnimationTimer} to track elapsed time continuously
 * while the game is running, allowing real-time UI updates or score timing logic.
 * </p>
 */
public class TimerSystem {
    /** Timestamp (in ns) when the timer started. */
    private long startTime;
    /** Elapsed time in seconds since start. */
    private long elapsedTime;
    /** Indicates whether the timer is currently running. */
    private boolean running;
    /** Internal JavaFX animation timer used for per-frame updates. */
    private AnimationTimer timer;
    /** Optional listener that receives periodic tick callbacks. */
    private TimerListener listener;

    /**
     * Listener interface for receiving timer tick updates.
     * Implement this in controllers that need to react to time changes.
     */
    public interface TimerListener {
        /**
         * Called each frame (roughly 60 FPS) while the timer is active.
         *
         * @param secondsElapsed number of seconds since the timer started
         */
        void onTick(long secondsElapsed);
    }

    /**
     * Constructs a new {@code TimerSystem} with the specified listener.
     *
     * @param listener the listener to notify on each tick — may be {@code null}
     */
    public TimerSystem(TimerListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the timer and begins sending tick events to the listener.
     * <p>
     * If the timer is already running, it will restart from zero.
     * </p>
     */
    public void start() {
        startTime = System.nanoTime();
        running = true;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    elapsedTime = (now - startTime) / 1_000_000_000;
                    if (listener != null) listener.onTick(elapsedTime);
                }
            }
        };
        timer.start();
    }

    /**
     * Stops the timer and halts tick callbacks.
     * <p>
     * The elapsed time value remains available until {@link #reset()} is called.
     * </p>
     */
    public void stop() {
        running = false;
        if (timer != null) timer.stop();
    }

    /**
     * Returns the number of seconds elapsed since the timer started.
     *
     * @return elapsed time in seconds
     */
    public double getElapsedSeconds() {
        return elapsedTime;
    }

    /**
     * Resets the timer to zero and stops it.
     * <p>
     * Use this to prepare for a new session or replay cycle.
     * </p>
     */
    public void reset() {
        stop();
        elapsedTime = 0;
    }
}

