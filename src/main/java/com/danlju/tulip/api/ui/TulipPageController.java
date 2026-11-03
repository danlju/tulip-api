package com.danlju.tulip.api.ui;

import com.danlju.tulip.api.controller.model.ModelMapper;
import com.danlju.tulip.application.repository.ProjectRepository;
import com.danlju.tulip.application.usecases.BuildUseCases;
import com.danlju.tulip.application.usecases.ProjectUseCases;
import com.danlju.tulip.core.domain.Project;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class TulipPageController {
    private final ProjectRepository projectRepository;
    private final ProjectUseCases projectUseCases;
    private final BuildUseCases buildUseCases;

    public TulipPageController(ProjectRepository projectRepository,
                               ProjectUseCases projectUseCases,
                               BuildUseCases buildUseCases) {
        this.projectRepository = projectRepository;
        this.projectUseCases = projectUseCases;
        this.buildUseCases = buildUseCases;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return "redirect:/projects";
    }

    @GetMapping(value = "/projects", produces = MediaType.TEXT_HTML_VALUE)
    public String projects(Model model) {
        model.addAttribute("projects", projectRepository.findAll().stream()
                .map(ModelMapper::toModel)
                .toList());
        model.addAttribute("activePage", "projects");
        return "projects";
    }

    @GetMapping(value = "/projects/{projectId}", produces = MediaType.TEXT_HTML_VALUE)
    public String project(@PathVariable String projectId, Model model) {
        Project project = findProject(projectId);
        if (project == null) {
            return error(model, "Project not found", "This project may have been removed or the link may be incorrect.");
        }
        model.addAttribute("project", ModelMapper.toModel(project));
        model.addAttribute("activePage", "projects");
        return "project";
    }

    @GetMapping(value = "/projects/{projectId}/builds", produces = MediaType.TEXT_HTML_VALUE)
    public String builds(@PathVariable String projectId, Model model) {
        UUID id;
        try {
            id = UUID.fromString(projectId);
        } catch (IllegalArgumentException exception) {
            return buildListError(projectId, model);
        }
        model.addAttribute("builds", buildUseCases.getBuildsForProjectByPublicId(id).stream()
                .map(ModelMapper::toBuildResponseModel)
                .toList());
        model.addAttribute("apiError", false);
        model.addAttribute("projectId", projectId);
        return "fragments/build-list :: build-list";
    }

    @PostMapping(value = "/projects", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createProject(@RequestParam String name,
                                @RequestParam String cloneUrl,
                                @RequestParam(defaultValue = "GENERIC") String sourceProvider,
                                RedirectAttributes redirectAttributes) {
        projectUseCases.createProject(UUID.randomUUID(), name.trim(), cloneUrl.trim(), sourceProvider);
        redirectAttributes.addFlashAttribute("notice", "Project connected.");
        return "redirect:/projects";
    }

    @PostMapping(value = "/projects/{projectId}/builds", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String startBuild(@PathVariable String projectId,
                             @RequestParam String branch,
                             @RequestParam(defaultValue = "") String commitSha,
                             RedirectAttributes redirectAttributes) {
        UUID.fromString(projectId);
        buildUseCases.requestBuild(projectId, branch.trim(), commitSha.trim());
        redirectAttributes.addFlashAttribute("notice", "Build requested.");
        return "redirect:/projects/" + projectId;
    }

    private Project findProject(String projectId) {
        try {
            return projectRepository.getByPublicId(UUID.fromString(projectId));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private String buildListError(String projectId, Model model) {
        model.addAttribute("builds", List.of());
        model.addAttribute("apiError", true);
        model.addAttribute("projectId", projectId);
        return "fragments/build-list :: build-list";
    }

    private String error(Model model, String title, String message) {
        model.addAttribute("title", title);
        model.addAttribute("message", message);
        return "error";
    }
}
