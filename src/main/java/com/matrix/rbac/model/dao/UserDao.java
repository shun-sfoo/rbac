package com.matrix.rbac.model.dao;

import com.matrix.rbac.model.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, Long> {
    int countByAccount(String account);
}
