package com.bci.reactive.service;

import com.bci.reactive.entity.Role;
import com.bci.reactive.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByRole(String roleStr) {
        return roleRepository.findByRole(roleStr);
    }

    public Role saveRole(String role) {
        Role roleObj = Role.builder().role(role).build();
        roleRepository.save(roleObj);
        return roleObj;
    }
}