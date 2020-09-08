package com.vaibhav.pohastore.service;

import java.util.List;

import com.vaibhav.pohastore.domain.Book;
import com.vaibhav.pohastore.domain.CartItem;
import com.vaibhav.pohastore.domain.ShoppingCart;
import com.vaibhav.pohastore.domain.User;

public interface CartItemService {
public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

public CartItem updateCartItem(CartItem cartItem);

public CartItem addBookToCartItem(Book book, User user, int parseInt);

public CartItem findById(Long cartItemId);

public void removeCartItem(CartItem findById);
}
