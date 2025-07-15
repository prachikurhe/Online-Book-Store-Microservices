package com.book.cart_service.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "cart_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long bookId;
    private String title;
    private double price;
    private int quantity;



}
