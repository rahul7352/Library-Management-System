package com.airtribe.lms.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Patron {

    private final String patronId;
    private String name;
    private String email;
    private final List<Book> borrowingHistory = new ArrayList<>();
    private final List<Book> currentlyBorrowedBooks = new ArrayList<>();


    public Patron(String id, String name, String email) {
        this.patronId = Objects.requireNonNull(id, "Patron Id cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
    }

    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    public List<Book> getCurrentlyBorrowedBooks() {
        return Collections.unmodifiableList(currentlyBorrowedBooks);
    }

    public void recordBorrow(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        currentlyBorrowedBooks.add(book);
        borrowingHistory.add(book);
    }

    public void recordReturn(Book book) {
        currentlyBorrowedBooks.remove(book);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Patron patron)) return false;
        return Objects.equals(patronId, patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(patronId);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId='" + patronId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
