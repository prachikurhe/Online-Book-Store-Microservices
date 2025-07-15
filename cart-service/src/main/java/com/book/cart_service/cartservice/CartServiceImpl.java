package com.book.cart_service.cartservice;


import com.book.cart_service.dto.BookDTO;
import com.book.cart_service.dto.CartItemDTO;
import com.book.cart_service.mapper.CartItemMapper;
import com.book.cart_service.model.CartItem;

import com.book.cart_service.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public CartItem addToCart(CartItemDTO dto) {
        // Call Book Service
        String bookServiceUrl = "http://book-service/api/books/" + dto.getBookId();
        CartItem bookInfo = restTemplate.getForObject(bookServiceUrl, CartItem.class);

        CartItem item = mapper.toEntity(dto);
        item.setUserId(dto.getUserId());
        item.setBookId(bookInfo.getBookId());
        item.setTitle(bookInfo.getTitle());
        item.setPrice(bookInfo.getPrice());
        item.setQuantity(dto.getQuantity());

        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getUserCart(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public void removeFromCart(Long id) {
        cartItemRepository.deleteById(id);
    }
}