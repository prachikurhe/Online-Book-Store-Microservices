package com.book.cart_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private Long userId;
    private Long bookId;     // ✅ Must include to fetch book details via RestTemplate
    private int quantity;

}
