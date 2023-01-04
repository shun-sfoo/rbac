package com.matrix.rbac.model.dao;

import com.matrix.rbac.model.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoDao extends JpaRepository<Info, Long>,
        JpaSpecificationExecutor<Info> {
}
