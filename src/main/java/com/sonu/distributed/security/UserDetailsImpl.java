package com.sonu.distributed.security;

import com.sonu.distributed.model.entity.access.RoleDetailsEntity;
import com.sonu.distributed.model.entity.access.UserDetailsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final UserDetailsEntity userDetailsEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<RoleDetailsEntity> roles = userDetailsEntity.getRoles();
        return roles.stream().map(it -> new SimpleGrantedAuthority(it.getName())).toList();
    }

    @Override
    public String getPassword() {
        return userDetailsEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetailsEntity.getUsername();
    }
}
