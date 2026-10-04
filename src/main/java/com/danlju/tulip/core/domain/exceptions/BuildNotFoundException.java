package com.danlju.tulip.core.domain.exceptions;

public class BuildNotFoundException extends RuntimeException {
    public BuildNotFoundException(Integer buildId) {
        super("Build not found: " + buildId);
    }
}
