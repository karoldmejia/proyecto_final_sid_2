package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    List<Permission> findAll();
    Optional<Permission> findById(Long id);
    Permission save(Permission permission);
    Permission update(Long id, Permission permission);
    void deleteById(Long id);
    public List<Permission> getPermissionsByIds(List<Long> ids);

}
