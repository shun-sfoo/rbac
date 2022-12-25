package com.matrix.rbac.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rbac_permission")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Permission parent;

    @Column(nullable = false)
    private String name;

    @Column(name = "permission_key", nullable = false, length = 32)
    private String permissionKey;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String path;

    private String resource;
    private Boolean enable = false;

    private String description;

    private Integer weight = 0;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id", updatable = false)
    private List<Permission> children;

    public enum Type {

    }
}
