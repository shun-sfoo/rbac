package com.matrix.rbac.model.dao;

import com.matrix.rbac.model.entity.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends CrudRepository<Permission, Long> {

    List<Permission> findAllByParentIsNull();

    List<Permission> findAllByParent(Permission parent);
}
