package com.sonu.distributed.security;

import com.sonu.distributed.model.entity.access.UserDetailsEntity;
import com.sonu.distributed.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUsername(username);
        if(userDetailsEntity == null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            return new UserDetailsImpl(userDetailsEntity);
        }
    }
}