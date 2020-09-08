package com.vaibhav.pohastore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaibhav.pohastore.domain.User;
import com.vaibhav.pohastore.domain.security.Role;
import com.vaibhav.pohastore.domain.security.UserRole;
import com.vaibhav.pohastore.service.UserService;
import com.vaibhav.pohastore.utility.SecurityUtility;


@SpringBootApplication
public class PohastoreApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(PohastoreApplication.class, args);
	}
	
	@Autowired
	UserService userService;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	/*User user1=new User();
	
	user1.setFirstName("ashok");
	user1.setLastName("bhade");
	user1.setUsername("ashok");
     user1.setPassword(SecurityUtility.passwordEncoder().encode("ashok"));
     user1.setEmail("ashokbhadeaai@gmail.com");
     
     Set<UserRole> userRoles=new HashSet<>();
     
     Role role1=new Role();
      role1.setRoleId(1);
      role1.setName("USER_ROLE");
      userRoles.add(new UserRole(user1,role1));
      
      userService.createUser(user1, userRoles);
      
      */
      
      
	}

}
