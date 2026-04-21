package com.danlju.tulip.api.controller.model;

public record BuildResponseModel(
        String id,
        String status,
        String commit,
        String branch,
        String runNumber,
        String duration
) {}
