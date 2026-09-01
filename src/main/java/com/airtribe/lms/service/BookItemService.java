package com.airtribe.lms.service;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.entity.Branch;
import com.airtribe.lms.enums.BookItemStatus;
import com.airtribe.lms.exception.BookNotFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BookItemService {

    private static final Logger logger = Logger.getLogger(BookItemService.class.getName());
    private final AtomicInteger sequence = new AtomicInteger(1);
    private final Map<String, BookItem> bookItems = new LinkedHashMap<>();

    public BookItem addBookItem(Book book, Branch branch) {
        String itemId = String.format("%03d", sequence.getAndIncrement());
        BookItem item = new BookItem(itemId, book, branch);
        bookItems.put(itemId, item);
        logger.log(Level.INFO,"Added book item {0} of ''{1}'' at {2}", new Object[] {itemId, book.getTitle(), branch.getBranchName()});
        return item;
    }

    public BookItem getBookItemById(String itemId) throws BookNotFoundException {
        BookItem bookItem = bookItems.get(itemId);
        if(bookItem == null) {
            throw new BookNotFoundException("No item with id " + itemId + " found.");
        }
        return bookItem;
    }
    /** All copies, regardless of status — useful for a full inventory report. */
    public List<BookItem> getAllBookItems() {
        return List.copyOf(bookItems.values());
    }

    public List<BookItem> findCopiesOf(String isbn) {
        return bookItems.values().stream()
                .filter(e -> e.getBook().getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }

    public List<BookItem> findCopiesAtBranch(String branchId) {
        return bookItems.values().stream()
                .filter(e -> e.getBranch().getBranchId().equals(branchId))
                .collect(Collectors.toList());
    }

    /** First available copy of a given ISBN at a given branch, if any. */
    public BookItem findBookItemAvailableAtGivenBranch(String isbn, String branchId)
            throws BookNotFoundException {
        return bookItems.values().stream()
                .filter(e -> e.getBook().getIsbn().equals(isbn))
                .filter(e -> e.getBranch().getBranchId().equals(branchId))
                .filter(e -> e.getBookItemStatus() == BookItemStatus.AVAILABLE)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(
                        "No available copy of " + isbn + " at branch " + branchId));
    }

    // --- Inventory Management: track available vs. borrowed copies ---
    /** All copies, across all branches, currently AVAILABLE. */
    public List<BookItem> trackAvailableCopies() {
        return getBookCopiesByStatus(BookItemStatus.AVAILABLE);
    }

    /** All copies, across all branches, currently BORROWED. */
    public List<BookItem> trackBorrowedCopies() {
        return getBookCopiesByStatus(BookItemStatus.BORROWED);
    }

    private List<BookItem> getBookCopiesByStatus(BookItemStatus status) {
        return bookItems.values().stream()
                .filter(e -> e.getBookItemStatus().equals(status))
                .collect(Collectors.toList());
    }
}
