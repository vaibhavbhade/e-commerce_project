package com.vaibhav.pohastore.repo;

import org.springframework.data.repository.CrudRepository;

import com.vaibhav.pohastore.domain.security.Role;

public interface RoleRepostiory extends CrudRepository<Role, Long>{
Role findByname(String name);

}
