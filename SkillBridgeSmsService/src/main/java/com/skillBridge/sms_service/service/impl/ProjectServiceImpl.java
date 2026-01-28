package com.skillBridge.sms_service.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillBridge.sms_service.dtos.ProjectCreateRequest;
import com.skillBridge.sms_service.dtos.ProjectPatchRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.entities.Project;
import com.skillBridge.sms_service.entities.ProjectRequiredSkill;
import com.skillBridge.sms_service.entities.ProjectStatus;
import com.skillBridge.sms_service.entities.Skill;
import com.skillBridge.sms_service.exception.ResourceNotFoundException;
import com.skillBridge.sms_service.repository.ProjectRepository;
import com.skillBridge.sms_service.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    // ================= CREATE =================
    @Override
    public ProjectResponse create(ProjectCreateRequest request, Long loggedInStudentId) {

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setCreatedBy(loggedInStudentId);
        project.setStatus(ProjectStatus.OPEN);

        if (request.getRequiredSkills() != null) {
            request.getRequiredSkills().forEach(skillStr -> {
                ProjectRequiredSkill prs = new ProjectRequiredSkill();
                prs.setSkill(Skill.valueOf(skillStr)); // ✅ conversion
                prs.setProject(project);
                project.getRequiredSkills().add(prs);
            });
        }


        return mapToResponse(projectRepository.save(project));
    }

    // ================= READ ONE =================
    @Override
    @Transactional(readOnly = true)
    public ProjectResponse get(Long id) {
        return mapToResponse(findProject(id));
    }

    // ================= READ ALL =================
    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= UPDATE FULL =================
    @Override
    public ProjectResponse update(Long id, ProjectCreateRequest request) {

        Project project = findProject(id);

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());

        // replace skills
        project.getRequiredSkills().clear();

        if (request.getRequiredSkills() != null) {
            request.getRequiredSkills().forEach(skillStr -> {
                ProjectRequiredSkill prs = new ProjectRequiredSkill();
                prs.setSkill(Skill.valueOf(skillStr)); // ✅ conversion
                prs.setProject(project);
                project.getRequiredSkills().add(prs);
            });
        }


        return mapToResponse(projectRepository.save(project));
    }

    // ================= UPDATE PARTIAL =================
    @Override
    public ProjectResponse patch(Long id, ProjectPatchRequest request) {

        Project project = findProject(id);

        if (request.getTitle() != null) {
            project.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        if (request.getRequiredSkills() != null) {
            project.getRequiredSkills().clear();

            request.getRequiredSkills().forEach(skillStr -> {
                ProjectRequiredSkill prs = new ProjectRequiredSkill();
                prs.setSkill(Skill.valueOf(skillStr)); // ✅ FIX
                prs.setProject(project);
                project.getRequiredSkills().add(prs);
            });
        }

        return mapToResponse(projectRepository.save(project));
    }

    // ================= UPDATE STATUS =================
    @Override
    public ProjectResponse updateStatus(Long id, ProjectStatus status) {
        Project project = findProject(id);
        project.setStatus(status);
        return mapToResponse(projectRepository.save(project));
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {
        Project project = findProject(id);
        projectRepository.delete(project);
    }

    // ================= HELPERS =================
    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found with id: " + id));
    }

    private ProjectResponse mapToResponse(Project project) {

        return ProjectResponse.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .description(project.getDescription())
                .status(project.getStatus())
                .createdBy(project.getCreatedBy())
                .requiredSkills(
                        project.getRequiredSkills()
                                .stream()
                                .map(prs -> prs.getSkill().name())
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
