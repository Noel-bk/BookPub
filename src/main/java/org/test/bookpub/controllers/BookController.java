package org.test.bookpub.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Reviewer;
import org.test.bookpub.repository.BookRepository;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.util.List;

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
    public Book getBook(@PathVariable Isbn isbn) {
        return bookRepository.findBookByIsbn(isbn.getIsbn());
    }

    @GetMapping(value = "/{isbn}/reviewers")
    public List<Reviewer> getReviewers(@PathVariable("isbn") Book book) {
        return book.getReviewers();
    }

    @GetMapping(value = "/session")
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @Getter
    @AllArgsConstructor
    public class Isbn {
        private String isbn;
    }

    public class IsbnEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (StringUtils.hasText(text)) {
                setValue(new Isbn(text.trim()));
            } else {
                setValue(null);
            }
        }

        @Override
        public String getAsText() {
            Isbn isbn = (Isbn) getValue();
            if (isbn != null) {
                return isbn.getIsbn();
            } else {
                return "";
            }
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Isbn.class, new IsbnEditor());
    }

}
