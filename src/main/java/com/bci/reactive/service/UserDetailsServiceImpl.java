package com.bci.reactive.service;

import com.bci.reactive.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
@Slf4j
@Service
@Profile("default")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.bci.reactive.entity.User dbUser = userService.findByEmail(username);

        if (dbUser == null) {
            throw new UsernameNotFoundException(String.format("User '%s' can not be found", username));
        }
        log.info("loadUserByUsername username : {} - {}", dbUser.getId(), username);
        dbUser.setLastLogin(Instant.now());

        userService.updateUser(dbUser);

        User user = new User(dbUser.getEmail(), dbUser.getPassword(), dbUser.isActive(),
                true, true,
                true,
                loadAuthorities(dbUser));

        return user;
    }

    public Collection<GrantedAuthority> loadAuthorities(com.bci.reactive.entity.User user) {
        Collection<Role> userAuthorities = user.getRoles();
        Collection<GrantedAuthority> authorities =
                userAuthorities.stream().map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getRole())).collect(Collectors.toCollection(ArrayList::new));
        return authorities;
    }

}