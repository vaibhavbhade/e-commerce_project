package com.vaibhav.pohastore.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaibhav.pohastore.domain.Payment;
import com.vaibhav.pohastore.domain.ShoppingCart;
import com.vaibhav.pohastore.domain.User;
import com.vaibhav.pohastore.domain.UserBilling;
import com.vaibhav.pohastore.domain.UserPayment;
import com.vaibhav.pohastore.domain.UserShipping;
import com.vaibhav.pohastore.domain.security.PasswordResetToken;
import com.vaibhav.pohastore.domain.security.UserRole;
import com.vaibhav.pohastore.repo.PasswordResetTokenRepository;
import com.vaibhav.pohastore.repo.RoleRepostiory;
import com.vaibhav.pohastore.repo.UserPaymentRepository;
import com.vaibhav.pohastore.repo.UserRepository;
import com.vaibhav.pohastore.repo.UserShippingRepository;
import com.vaibhav.pohastore.service.UserService;

@Service
public class UserServiceImpl implements UserService{  

	 private final static Logger LOG=LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepostiory roleRepostiory;
	
	@Autowired
	private UserPaymentRepository userPaymentRepository; 
	
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		// TODO Auto-generated method stub
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(final String token, final User user) {
		// TODO Auto-generated method stub
		final PasswordResetToken myToken=new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional

	public User createUser(User user, Set<UserRole> userRoles){
		User localUser=userRepository.findByUsername(user.getUsername());
		
		if(localUser != null) {
		//	throw new Exception("User already exists.Nothing will be done.");
			LOG.info("User {} already exists.Nothing will be done.",user.getUsername());
		}
		else {
			for (UserRole ur : userRoles) {
			roleRepostiory.save(ur.getRole());
			}
			
		}
		
		user.getUserRoles().addAll(userRoles);
		
		ShoppingCart shoppingCart=new ShoppingCart();
		
		shoppingCart.setUser(user);
		
		user.setShoppingCart(shoppingCart);
		
		user.setUserShippingList(new ArrayList<UserShipping>());
		
		user.setUserPaymentList(new ArrayList<UserPayment>());
		localUser=userRepository.save(user);
		
return localUser;
		}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
		
	}

	@Override
	public void setUserDefaultPayment(Long defaultPaymentId, User user) {
  List<UserPayment> userPaymentList=	(List<UserPayment>) userPaymentRepository.findAll();
  
for (UserPayment userPayment : userPaymentList) {
	if(userPayment.getId() == defaultPaymentId) {
		userPayment.setDefaultPayment(true);
		userPaymentRepository.save(userPayment);
	}
	else {
		userPayment.setDefaultPayment(false);
		userPaymentRepository.save(userPayment);

	}
		
}
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		System.out.println("**************************************************************"+user.getUserShippingList().add(userShipping));
		save(user);
		
	}

	@Override
	public void setUserDefaultShipping(Long defaultShippingId, User user) {
		// TODO Auto-generated method stub
		List<UserShipping> userShippingList=(List<UserShipping>) userShippingRepository.findAll();
			for (UserShipping userShipping : userShippingList) {
				if(userShipping.getId() == defaultShippingId) {
					userShipping.setUserShippingDefault(true);
					userShippingRepository.save(userShipping);
				}
				else {
					userShipping.setUserShippingDefault(false);
					userShippingRepository.save(userShipping);


					
				}
			}
		
		
	}

}
