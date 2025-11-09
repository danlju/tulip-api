package com.danlju.tulip.api.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ListBuildsResponseModel(
    @JsonProperty("total_count") int totalCount,
    @JsonProperty("builds") List<BuildResponseModel> builds) {
}
