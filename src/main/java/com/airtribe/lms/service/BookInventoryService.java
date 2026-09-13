package com.airtribe.lms.service;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.exception.BookNotFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BookInventoryService {

    private static final Logger logger = Logger.getLogger(BookInventoryService.class.getName());
    private final Map<String, Book> catalog = new LinkedHashMap<>();

    //add new book
    public void addBook(Book book) {
        if(book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        catalog.put(book.getIsbn(), book);
        logger.log(Level.INFO,"Added book: {0}", book.getIsbn());
    }

    //update book information
    public void updateBookInfo(String isbn, String title, String author, Integer publicationYear) {
        Book book = catalog.get(isbn);
        if(author != null && !author.isEmpty()) {
            book.setAuthor(author);
        }
        if(title != null && !title.isEmpty()) {
            book.setTitle(title);
        }
        if(publicationYear != null) {
            book.setPublicationYear(publicationYear);
        }
    }

    public void removeBook(String isbn) throws BookNotFoundException {
        if(catalog.remove(isbn) == null) {
            throw new BookNotFoundException("No book with ISBN " + isbn);
        }
        logger.log(Level.INFO,"Removed book: {0}", isbn);
    }

    public Book findBookByIsbn(String isbn) throws BookNotFoundException {
        Book book = catalog.get(isbn);
        if(book == null) {
            throw new BookNotFoundException("No book with ISBN " + isbn);
        }
        return book;
    }

    public List<Book> searchBookByTitle(String title) {
        return catalog.values().stream()
                .filter(e -> e.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());

    }

    public List<Book> searchBookByAuthor(String author) {
        return catalog.values().stream()
                .filter(e -> e.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());

    }

    public List<Book> getAllBooks() {
        return List.copyOf(catalog.values());
    }
}
