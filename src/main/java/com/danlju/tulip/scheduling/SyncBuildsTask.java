package com.danlju.tulip.scheduling;

import com.danlju.tulip.config.TulipConfig;
import com.danlju.tulip.core.domain.Project;
import com.danlju.tulip.application.repository.ProjectRepository;
import com.danlju.tulip.application.service.BuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SyncBuildsTask {

    private static Logger logger = LoggerFactory.getLogger(SyncBuildsTask.class);

    public static final long SYNC_RATE_MS = 25000;

    @Autowired
    private BuildService buildService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TulipConfig tulipConfig;

    @Scheduled(fixedRate = SYNC_RATE_MS)
    public void syncBuilds() {
        logger.info("Github account from config: {}", tulipConfig.getGithubProperties().getAccount());
        var projects = projectRepository.findAll();

        for (Project project : projects) {
            logger.info("Syncing for {} ({})", project.getName(), project.getGithubName());
            buildService.syncBuilds("danlju", project.getGithubName());
        }
    }
}
