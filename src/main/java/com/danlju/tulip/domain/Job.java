package com.danlju.tulip.domain;

import java.util.UUID;

public class Job {
    private UUID id;
    private Project project;
    private String githubRunId;
    private String commitTitle;
    private String commitBody;
    private String commitHash;
    private String status;
    private String result;
    private String triggeredByUser;
}
