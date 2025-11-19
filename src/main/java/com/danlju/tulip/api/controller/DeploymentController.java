package com.danlju.tulip.api.controller;

import com.danlju.tulip.config.TulipConfig;
import com.danlju.tulip.github.GitHubClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class DeploymentController {

    @Autowired
    private GitHubClient gitHubClient;

    @Autowired
    private TulipConfig tulipConfig;

    @GetMapping
//    @Async
    public ResponseEntity<String> startDeployment(String repo, String workflowId) {

        CompletableFuture<String> startDeployment = gitHubClient.startDeployment(
                tulipConfig.getGithubProperties().getAccount(), repo, workflowId
                );


        //String result1 = future1.get(); // Blocks until future1 is complete
        //future2.get(); // Blocks until future2 is complete

        return ResponseEntity.ok("OK");
    }

}
