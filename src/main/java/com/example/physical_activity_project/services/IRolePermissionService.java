package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.RolePermission;

import java.util.List;

public interface IRolePermissionService {
    List<RolePermission> findAll();

    List<Long> findPermissionIdsByRoleId(Long roleId);

}