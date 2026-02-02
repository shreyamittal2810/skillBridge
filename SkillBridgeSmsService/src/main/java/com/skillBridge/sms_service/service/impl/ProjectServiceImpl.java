package com.skillBridge.sms_service.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillBridge.sms_service.dtos.ModifyProjectMembersRequest;
import com.skillBridge.sms_service.dtos.ProjectCreateRequest;
import com.skillBridge.sms_service.dtos.ProjectPatchRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.entities.Project;
import com.skillBridge.sms_service.entities.ProjectMember;
import com.skillBridge.sms_service.entities.ProjectRequiredSkill;
import com.skillBridge.sms_service.entities.ProjectStatus;
import com.skillBridge.sms_service.entities.Role;
import com.skillBridge.sms_service.entities.Skill;
import com.skillBridge.sms_service.entities.Student;
import com.skillBridge.sms_service.exception.ResourceNotFoundException;
import com.skillBridge.sms_service.repository.ProjectMemberRepository;
import com.skillBridge.sms_service.repository.ProjectRepository;
import com.skillBridge.sms_service.repository.StudentRepository;
import com.skillBridge.sms_service.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;
    private final ProjectMemberRepository projectMemberRepository;

    
    //==========CHECKING OWNERSHIP===============
    private Project findOwnedProject(Long projectId, Long studentId) {
        Project project = findProject(projectId);

        if (!project.getCreatedBy().equals(studentId)) {
            throw new IllegalStateException(
                    "You are not allowed to modify this project"
            );
        }

        return project;
    }
    

    // ================= CREATE =================
    @Override
    public ProjectResponse create(ProjectCreateRequest request, Long loggedInStudentId) {

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setCreatedBy(loggedInStudentId);
        project.setStatus(ProjectStatus.OPEN);

     // Add creator as project member (LEAD)
        ProjectMember creatorMember = new ProjectMember();
        creatorMember.setStudentId(loggedInStudentId);
        creatorMember.setProject(project);
        project.getMembers().add(creatorMember);
        
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
    public ProjectResponse update(
            Long id,
            ProjectCreateRequest request,
            Long studentId
    ) {
        Project project = findOwnedProject(id, studentId);

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());

        project.getRequiredSkills().clear();

        if (request.getRequiredSkills() != null) {
            request.getRequiredSkills().forEach(skillStr -> {
                ProjectRequiredSkill prs = new ProjectRequiredSkill();
                prs.setSkill(Skill.valueOf(skillStr));
                prs.setProject(project);
                project.getRequiredSkills().add(prs);
            });
        }

        return mapToResponse(projectRepository.save(project));
    }


    // ================= UPDATE PARTIAL =================
    @Override
    public ProjectResponse patch(
            Long id,
            ProjectPatchRequest request,
            Long studentId
    ) {
        Project project = findOwnedProject(id, studentId);

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
                prs.setSkill(Skill.valueOf(skillStr));
                prs.setProject(project);
                project.getRequiredSkills().add(prs);
            });
        }

        return mapToResponse(projectRepository.save(project));
    }


    // ================= UPDATE STATUS =================
    @Override
    public ProjectResponse updateStatus(
            Long id,
            ProjectStatus status,
            Long studentId
    ) {
        Project project = findOwnedProject(id, studentId);
        project.setStatus(status);
        return mapToResponse(projectRepository.save(project));
    }


    // ================= DELETE =================
    @Override
    public void delete(Long id, Long studentId) {
        Project project = findOwnedProject(id, studentId);
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
                .teamMembers(
                        project.getMembers()
                                .stream()
                                .map(ProjectMember::getStudentId)
                                .collect(Collectors.toSet())
                )
                .build();
    }


    @Override
    public ProjectResponse modifyProjectMembers(
            Long projectId,
            ModifyProjectMembersRequest request,
            Long requesterId
    ) {
        // 1️⃣ Fetch project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        // 2️⃣ Fetch requester
        Student requester = studentRepository.findById(requesterId)
                .orElseThrow(() ->
                        new IllegalStateException("Student not found"));

        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isCreator = project.getCreatedBy().equals(requesterId);

        // 3️⃣ Permission check
        if (!isAdmin && !isCreator) {
            throw new IllegalStateException(
                    "You are not allowed to modify team members"
            );
        }

        // 4️⃣ ADD members
        if (request.getAddMembers() != null) {
            for (Long memberId : request.getAddMembers()) {
            	
            	if (memberId.equals(project.getCreatedBy())) {
            	    continue; // creator is already the lead
            	}
            	studentRepository.findById(memberId)
                .orElseThrow(() ->
                        new IllegalStateException("Student not found: " + memberId));

                boolean alreadyMember = project.getMembers()
                        .stream()
                        .anyMatch(m -> m.getStudentId().equals(memberId));

                if (!alreadyMember) {
                    ProjectMember member = new ProjectMember();
                    member.setStudentId(memberId);
                    member.setProject(project);
                    project.getMembers().add(member);
                }
            }
        }

        // 5️⃣ REMOVE members (creator cannot be removed)
        if (request.getRemoveMembers() != null) {
            project.getMembers().removeIf(member ->
                    request.getRemoveMembers().contains(member.getStudentId())
                    && !member.getStudentId().equals(project.getCreatedBy())
            );
        }

        // 6️⃣ Save
        Project savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);
    }

    @Override
    public boolean isMember(Long projectId, Long studentId) {
        return projectMemberRepository
                .existsByProject_ProjectIdAndStudentId(projectId, studentId);
    }



}

