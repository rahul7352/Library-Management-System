package com.airtribe.lms.entity;

import com.airtribe.lms.enums.BookItemStatus;
import com.airtribe.lms.exception.BookNotAvailableException;
import com.airtribe.lms.pattern.state.AvailableState;
import com.airtribe.lms.pattern.state.ItemState;

import java.util.Objects;

public class BookItem {

    private final String bookItemId;
    private final Book book;
    private Branch branch;
    private ItemState state;


    public BookItem(String bookItemId, Book book, Branch branch) {
        this.bookItemId = bookItemId;
        this.book = book;
        this.branch = branch;
        this.state = AvailableState.INSTANCE;
    }

    public String getBookItemId() {
        return bookItemId;
    }

    public Book getBook() {
        return book;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BookItemStatus getBookItemStatus() {
        return state.getBookItemStatus();
    }

    // --- Lifecycle actions, delegated to the current state (State pattern) ---
    public void checkout() throws BookNotAvailableException {
        state = state.checkout(this);
    }

    public void returnItem() throws BookNotAvailableException {
        state = state.returnItem(this);
    }
    public void reserve() throws BookNotAvailableException {
        state = state.reserve(this);
    }

    public void startTransfer() throws BookNotAvailableException {
        state = state.startTransfer(this);
    }

    public void completeTransfer(Branch destination) throws BookNotAvailableException {
        state = state.completeTransfer(this, destination);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookItem bookItem)) return false;
        return Objects.equals(bookItemId, bookItem.bookItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookItemId);
    }

    @Override
    public String toString() {
        return String.format("Item[%s, %s @ %s, %s]", bookItemId, book.getTitle(), branch.getBranchName(), getBookItemStatus());
    }
}
