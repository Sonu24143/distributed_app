package com.sonu.distributed.model.entity.access;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "role_details")
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoleDetailsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Boolean enabled;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<UserDetailsEntity> users;
}
