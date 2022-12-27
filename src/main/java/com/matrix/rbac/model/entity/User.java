package com.matrix.rbac.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rbac_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 16, nullable = false)
    private String account;

    @Column(nullable = false, length = 128)
    private String password;

    @Column(name = "user_name", length = 32)
    private String userName;

    private String tel;

    private Boolean enable = false;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rbac_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}

