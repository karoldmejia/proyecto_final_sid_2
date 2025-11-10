package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<Permission, Long>{
}
