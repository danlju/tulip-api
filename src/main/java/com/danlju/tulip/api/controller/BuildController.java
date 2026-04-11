package com.danlju.tulip.api.controller;

import com.danlju.tulip.api.controller.model.StartWorkflowRequest;
import com.danlju.tulip.application.repository.ProjectRepository;
import com.danlju.tulip.application.service.WorkflowRunsService;
import com.danlju.tulip.application.usecases.BuildUseCases;
import com.danlju.tulip.config.TulipConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * To replace the workflow endpoints
 */
@RestController
public class BuildController {

    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkflowRunsService workflowRunsService;

    @Autowired
    private BuildUseCases buildService;

    @Autowired
    private TulipConfig tulipConfig;

    @PostMapping(value = "/projects/{repo}/build", consumes = "application/json")
    public String startWorkflowRun(@RequestBody StartWorkflowRequest startWorkflowRequest) {
        // TODO: fix parameters
        // repo
        // branch
        // owner
        // user?
        return "";
        //return buildService.requestBuild();
    }
}
