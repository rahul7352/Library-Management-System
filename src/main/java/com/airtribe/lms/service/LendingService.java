package com.airtribe.lms.service;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.entity.Patron;
import com.airtribe.lms.enums.BookItemStatus;
import com.airtribe.lms.exception.BookNotAvailableException;
import com.airtribe.lms.exception.BookNotFoundException;
import com.airtribe.lms.pattern.observer.BookAvailabilityObserver;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LendingService {

    private static final Logger logger = Logger.getLogger(LendingService.class.getName());
    private final BookItemService bookItemService;
    private final ReservationService reservationService;

    public LendingService(BookItemService bookItemService, ReservationService reservationService) {
        this.bookItemService = bookItemService;
        this.reservationService = reservationService;
    }


    public void checkout(Patron patron, String itemId) throws BookNotFoundException, BookNotAvailableException {
        BookItem bookItem = bookItemService.getBookItemById(itemId);
        bookItem.checkout();
        patron.recordBorrow(bookItem.getBook());

        logger.log(Level.INFO, "{0} checked out book item {1} (''{2}'') from {3}", new Object[]{patron.getPatronId(), itemId,
                bookItem.getBook().getTitle(), bookItem.getBranch().getBranchName()});
    }

    public void returnBook(Patron patron, String itemId) throws BookNotFoundException, BookNotAvailableException {
        BookItem bookItem = bookItemService.getBookItemById(itemId);
        patron.recordReturn(bookItem.getBook());
        bookItem.returnItem();
        logger.log(Level.INFO, "{0} returned book item {1} (''{2}'') to {3}", new Object[]{patron.getPatronId(), itemId,
                bookItem.getBook().getTitle(), bookItem.getBranch().getBranchName()});
        notifyNextInQueue(bookItem);
    }

    public void notifyNextInQueue(BookItem bookItem) throws BookNotAvailableException {
        Book book = bookItem.getBook();
        List<BookAvailabilityObserver> observers = reservationService.getReservationQueue().get(book.getIsbn());
        if(observers == null || observers.isEmpty()) {
            return;
        }
        BookAvailabilityObserver observer = observers.remove(0);
        bookItem.reserve();
        observer.onBookAvailable(book);
    }
}
