package com.vaibhav.pohastore.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaibhav.pohastore.domain.UserPayment;
import com.vaibhav.pohastore.repo.UserPaymentRepository;
import com.vaibhav.pohastore.service.UserPaymentService;
@Service
public class UserPaymentServiceImpl implements UserPaymentService {

	
	@Autowired
   private UserPaymentRepository userPaymentRepository; 
	
	
	@Override
	public UserPayment findById(Long id) {
		
		return userPaymentRepository.findById(id).orElse(null);
	}


	@Override
	public void removeById(Long id) {
		userPaymentRepository.deleteById(id);
		
	}

}
