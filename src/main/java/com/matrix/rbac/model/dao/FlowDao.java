package com.matrix.rbac.model.dao;

import com.matrix.rbac.model.entity.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowDao extends JpaRepository<Flow, Long>,
        JpaSpecificationExecutor<Flow> {
}
