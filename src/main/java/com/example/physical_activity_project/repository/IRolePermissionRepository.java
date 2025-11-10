package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IRolePermissionRepository extends JpaRepository<RolePermission, Long>  {
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
    long countByRole(Role role); // para contar cuántos permisos tiene un rol
    void deleteByRoleAndPermission(Role role, Permission permission);
    void deleteAllByRole(Role role);
    @Query("SELECT rp.permission.id FROM RolePermission rp WHERE rp.role.id = :roleId")
    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);
    @Modifying
    @Transactional
    @Query("DELETE FROM RolePermission rp WHERE rp.permission.id = :permissionId")
    void deleteAllByPermissionId(Long permissionId);

}
