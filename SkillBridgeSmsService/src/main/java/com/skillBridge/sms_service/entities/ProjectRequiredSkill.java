package com.skillBridge.sms_service.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_required_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequiredSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // ✅ required by MySQL cloud DB

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
