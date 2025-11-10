package com.example.physical_activity_project.services.impl;


import com.example.physical_activity_project.repository.IPermissionRepository;
import com.example.physical_activity_project.repository.IRolePermissionRepository;
import com.example.physical_activity_project.services.IPermissionService;
import com.example.physical_activity_project.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private IPermissionRepository permissionRepository;
    @Autowired
    private IRolePermissionRepository rolePermissionRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Long id, Permission permission) {
        Optional<Permission> existing = permissionRepository.findById(id);
        if (existing.isPresent()) {
            Permission p = existing.get();
            p.setName(permission.getName());
            p.setDescription(permission.getDescription());
            return permissionRepository.save(p);
        } else {
            throw new RuntimeException("Permission not found with id " + id);
        }
    }

    @Override
    public void deleteById(Long id) {
        rolePermissionRepository.deleteAllByPermissionId(id);
        permissionRepository.deleteById(id);
    }


    @Override
    public List<Permission> getPermissionsByIds(List<Long> ids) {
        return permissionRepository.findAllById(ids);
    }
}
