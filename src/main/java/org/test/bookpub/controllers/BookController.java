package org.test.bookpub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

/**
 * Created by Noel on 12/18/16.
 */
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value = "")
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "/{isbn}")
    public Book getBook(@PathVariable String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

}
