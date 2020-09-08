package com.vaibhav.pohastore.utility;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.vaibhav.pohastore.domain.User;

@Component
public class MailConstructor {
	
	@Autowired
	private Environment env;
	
	public SimpleMailMessage constructResetTokenEmail(String contextPath,Locale local,String token,User user,String password ) {
		
		String url=contextPath+"/newUser?token="+token;
		String message="\nPlease click on this link to verify your mail and edit your personal information.Your password is :\n"+password;
		
	  SimpleMailMessage email=new SimpleMailMessage();
	  
	  email.setTo(user.getEmail());
	  email.setSubject("Akash Poha's = new User");
	  
	  email.setText("<a>"+url+message+"</a>");
	  email.setFrom(env.getProperty("support.email"));
	  return email;
	}
	

}
