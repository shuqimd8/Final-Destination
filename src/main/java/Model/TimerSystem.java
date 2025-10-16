package Model;

import javafx.animation.AnimationTimer;

public class TimerSystem {
    private long startTime;
    private long elapsedTime;
    private boolean running;
    private AnimationTimer timer;
    private TimerListener listener;

    public interface TimerListener {
        void onTick(long secondsElapsed);
    }

    public TimerSystem(TimerListener listener) {
        this.listener = listener;
    }

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

    public void stop() {
        running = false;
        if (timer != null) timer.stop();
    }

    public double getElapsedSeconds() {
        return elapsedTime;
    }

    public void reset() {
        stop();
        elapsedTime = 0;
    }
}

