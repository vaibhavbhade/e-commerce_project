package com.vaibhav.pohastore.serviceimpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaibhav.pohastore.domain.CartItem;
import com.vaibhav.pohastore.domain.ShoppingCart;
import com.vaibhav.pohastore.repo.ShoppingCartRepository;
import com.vaibhav.pohastore.service.CartItemService;
import com.vaibhav.pohastore.service.ShoppingCartService;
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	@Autowired
    private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Override
	public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		BigDecimal cartTotal=new BigDecimal(0);
		
		List<CartItem> cartItenList=cartItemService.findByShoppingCart(shoppingCart);
		
		for (CartItem cartItem : cartItenList) {
			if(cartItem.getBook().getInStockNumber()>0) {
				cartItemService.updateCartItem(cartItem);
				cartTotal=cartTotal.add(cartItem.getSubtotal());
				
			}
		}
		shoppingCart.setGrandTotal(cartTotal);
		
		shoppingCartRepository.save(shoppingCart);
		return shoppingCart;
	}

}
