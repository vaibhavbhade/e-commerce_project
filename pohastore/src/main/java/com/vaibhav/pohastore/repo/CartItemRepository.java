package com.vaibhav.pohastore.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.vaibhav.pohastore.domain.CartItem;
import com.vaibhav.pohastore.domain.ShoppingCart;
@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
	
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart );

}
