package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.domain.Project;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "projects")
public class ProjectDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID publicId;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String githubName;

    public ProjectDbEntity() {
    }

    public ProjectDbEntity(Integer id, UUID publicId, String name, String githubName) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.githubName = githubName;
    }

    public ProjectDbEntity(UUID publicId, String name, String githubName) {
        this.publicId = publicId;
        this.name = name;
        this.githubName = githubName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubName() {
        return githubName;
    }

    public void setGithubName(String githubName) {
        this.githubName = githubName;
    }

    public static ProjectDbEntity fromProject(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectDbEntity(project.getPublicId(), project.getName(), project.getGithubName());
    }

    public static Project toProject(ProjectDbEntity dbEntity) {
        return new Project(dbEntity.id, dbEntity.publicId, dbEntity.name, dbEntity.githubName);
    }
}
