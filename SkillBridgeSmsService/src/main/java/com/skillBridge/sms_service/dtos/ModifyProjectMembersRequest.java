package com.skillBridge.sms_service.dtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModifyProjectMembersRequest {

    /**
     * Student IDs to be added to the project team.
     * Optional — can be null.
     */
    private Set<Long> addMembers;

    /**
     * Student IDs to be removed from the project team.
     * Optional — can be null.
     */
   
    private Set<Long> removeMembers;
}
