package com.matrix.rbac.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rbac_permission")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
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

    @JsonProperty("text")
    public String getText() {
        return this.name;
    }
    public enum Type {
        MENU("菜单"),
        FUNCTION("功能"),
        BLOCK("区域");

        private String display;

        Type(String display) {
            this.display = display;
        }

        public String display() {
            return display;
        }

        @Override
        public String toString() {
            return this.display + "[" + this.name() + "]";
        }
    }
}
