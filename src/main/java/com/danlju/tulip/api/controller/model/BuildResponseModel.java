package com.danlju.tulip.api.controller.model;

public record BuildResponseModel(
        long id,
        String name,
        String status,
        String startedByUser,
        String commit,
        String branch,
        String runNumber,
        String displayTitle,
        String duration
) {}
