package com.vaibhav.pohastore.service;

import com.vaibhav.pohastore.domain.UserPayment;

public interface UserPaymentService {
	 UserPayment findById(Long id);
	 void removeById(Long id);
	 
}
