package com.matrix.rbac.model.dao;

import com.matrix.rbac.model.entity.FileRecord;
import org.springframework.data.repository.CrudRepository;

public interface FileRecordDao extends CrudRepository<FileRecord, Long> {

    FileRecord findByFileName(String fileName);
}
