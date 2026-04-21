package com.danlju.tulip.utils;

import java.time.Instant;
import java.util.Objects;

public class Utils {

    public static String calculateDuration(Instant startedAt, Instant updatedAt) {
        long seconds = updatedAt.getEpochSecond() - startedAt.getEpochSecond();
        if (seconds < 60) {
            return seconds + "s";
        }
        long h = seconds / 60;
        long m = seconds / 60 % 60;

        String duration = "";
        duration += h + "h";

        return duration + " " + m +"m";
    }
}
