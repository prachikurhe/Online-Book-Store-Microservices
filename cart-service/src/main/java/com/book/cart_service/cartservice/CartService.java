package com.book.cart_service.cartservice;

import com.book.cart_service.dto.CartItemDTO;
import com.book.cart_service.model.CartItem;

import java.util.List;

public interface CartService {
    CartItem addToCart(CartItemDTO cartItemDTO);
    List<CartItem> getUserCart(Long userId);
    void removeFromCart(Long id);
}
