package com.danlju.tulip.scheduling;

import com.danlju.tulip.domain.Project;
import com.danlju.tulip.repo.ProjectRepository;
import com.danlju.tulip.service.BuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SyncBuildsTask {

    private static Logger logger = LoggerFactory.getLogger(SyncBuildsTask.class);

    @Autowired
    private BuildService buildService;

    @Autowired
    private ProjectRepository projectRepository;

    @Scheduled(fixedRate = 20000)
    public void syncBuilds() {
        var projects = projectRepository.findAll();

        for (Project project : projects) {
            logger.info("Syncing for {} ({})", project.getName(), project.getGithubName());
            buildService.syncBuilds("danlju", project.getGithubName());
        }
    }
}
