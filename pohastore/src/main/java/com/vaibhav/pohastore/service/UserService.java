package com.vaibhav.pohastore.service;

import java.util.Set;

import com.vaibhav.pohastore.domain.User;
import com.vaibhav.pohastore.domain.UserBilling;
import com.vaibhav.pohastore.domain.UserPayment;
import com.vaibhav.pohastore.domain.UserShipping;
import com.vaibhav.pohastore.domain.security.PasswordResetToken;
import com.vaibhav.pohastore.domain.security.UserRole;

public interface UserService {
	
	  PasswordResetToken getPasswordResetToken(final String Token);
	  void createPasswordResetTokenForUser(final String token,final User user);
      User findByUsername(String username);
      User findByEmail(String email);
      User createUser(User user,Set<UserRole> userRoles) throws Exception;
      User save(User user);
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	void setUserDefaultPayment(Long defaultPaymentId, User user);
	void updateUserShipping(UserShipping userShipping, User user);
	void setUserDefaultShipping(Long defaultShippingId, User user);
      
}
