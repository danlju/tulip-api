package com.danlju.tulip.application.usecases;

public class Result<T> {

    private final boolean success;
    private final T data;
    private final String errorMessage;

    public Result(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public Result(T data) {
        this.data = data;
        this.success = true;
        this.errorMessage = "";
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
