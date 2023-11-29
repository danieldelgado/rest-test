package com.bci.reactive.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(unique=true, length = 100)
    private String email;

    @Column(length = 100)
    private String password;

    private boolean active;

    private Instant created;

    private Instant modified;

    private Instant lastLogin;

    private String keyAccess; //token de accesso en uuid


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "fk_user", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "fk_role",
                    nullable = false, updatable = false)})
    private List<Role> roles;


}