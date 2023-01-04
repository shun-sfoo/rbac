package com.matrix.rbac.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "file_record")
@Data
public class FileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

}
