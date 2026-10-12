package com.danlju.tulip.utils;

import java.time.Instant;
import java.util.Objects;

public class Utils {

    public static String calculateDuration(Instant startedAt, Instant updatedAt) {
        long seconds = updatedAt.getEpochSecond() - startedAt.getEpochSecond();
        if (seconds < 60) {
            return seconds + "s";
        }
        long hours = seconds / 3600;
        long minutes = seconds / 60 % 60;
        if (hours == 0) {
            return minutes + "m";
        }
        return hours + "h " + minutes + "m";
    }
}
