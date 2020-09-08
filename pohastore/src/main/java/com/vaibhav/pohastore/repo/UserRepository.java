package com.vaibhav.pohastore.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.vaibhav.pohastore.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	 public User findByUsername(String username);
	 
	public User findByEmail(String email);
	

}
