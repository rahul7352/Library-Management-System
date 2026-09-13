package com.airtribe.lms.entity;

import java.util.Objects;

public class Book {

    private final String isbn;
    private String author;
    private String title;
    private int publicationYear;


    public Book(String title, String author, String isbn, int publicationYear) {
        this.title = Objects.requireNonNull(title, "title cannot be null or empty");
        this.author = Objects.requireNonNull(author, "author cannot be null or empty");;
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null or empty");;
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String describe() {
        return String.format("%s by %s (%d)", title, author, publicationYear);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book book)) return false;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

    @Override
    public String toString() {
        return describe();
    }
}
