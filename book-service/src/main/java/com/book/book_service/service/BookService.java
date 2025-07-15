package com.book.book_service.service;


import com.book.book_service.model.Book;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long bookId);
    Book updateBook(Long bookId, Book book);
    void deleteBook(Long bookId);

    List<Book> saveAllBooks(List<Book> books);
}