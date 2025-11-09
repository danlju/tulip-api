package com.danlju.tulip.utils;

import java.time.Instant;
import java.util.Objects;

public class Utils {

    public static String mapGithubStatus(String conclusion, String status) {
        if (conclusion == null || conclusion.isBlank()) {
            if (Objects.equals(status, "failed")) {
                return "failure";
            } else if (Objects.equals(status, "in_progress")) {
                return "running";
            } else if (
                    Objects.equals(status, "waiting")
                            || Objects.equals(status, "queued")
                            || Objects.equals(status, "requested")) {
                return "pending";
            }
        } else if (conclusion.equals("failure")) {
            return "failure";
        } else if (conclusion.equals("success")) {
            return "success";
        }
        return "unknown";
    }

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
