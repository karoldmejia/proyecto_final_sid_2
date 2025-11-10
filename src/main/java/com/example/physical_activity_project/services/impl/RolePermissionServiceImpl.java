package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.RolePermission;
import com.example.physical_activity_project.repository.IRolePermissionRepository;
import com.example.physical_activity_project.services.IRolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements IRolePermissionService {

    private final IRolePermissionRepository rolePermissionRepository;

    @Override
    public List<RolePermission> findAll() {
        return rolePermissionRepository.findAll();
    }
    @Override
    public List<Long> findPermissionIdsByRoleId(Long roleId){
        return rolePermissionRepository.findPermissionIdsByRoleId(roleId);
    }


}
