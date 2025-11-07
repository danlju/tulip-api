package com.danlju.tulip.utils;

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

}
