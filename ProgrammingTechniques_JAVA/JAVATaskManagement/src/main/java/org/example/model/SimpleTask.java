package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;

public final class SimpleTask extends Task {

    private static final long serialVersionUID = 1L;

    public SimpleTask(String title, LocalDateTime startTime, LocalDateTime endTime) {
        super(title, startTime, endTime);
    }

    @Override
    public double estimateDuration() {
        if (getStartTime() != null && getEndTime() != null) {
            Duration duration = Duration.between(getStartTime(), getEndTime());
            return duration.toHours() + duration.toMinutesPart() / 60.0;
        }
        return 0;
    }
}
