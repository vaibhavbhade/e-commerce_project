package com.vaibhav.pohastore.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaibhav.pohastore.domain.UserShipping;
import com.vaibhav.pohastore.repo.UserShippingRepository;
import com.vaibhav.pohastore.service.UserShippingService;

@Service
public class UserShippingServiceImpl  implements UserShippingService{

	
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	@Override
	public UserShipping findById(Long id) {
		// TODO Auto-generated method stub
		return userShippingRepository.findById(id).orElse(null);
	}

	@Override
	public void removeById(Long userShippingId) {
		// TODO Auto-generated method stub
		userShippingRepository.deleteById(userShippingId);
	}

}
