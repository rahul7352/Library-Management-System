package com.airtribe.lms.service;

import com.airtribe.lms.pattern.observer.BookAvailabilityObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationService {
    private static final Logger logger = Logger.getLogger(ReservationService.class.getName());
    private final Map<String, List<BookAvailabilityObserver>> reservationQueue = new ConcurrentHashMap<>();

    public void reserveBook(BookAvailabilityObserver bookAvailabilityObserver, String isbn) {
        reservationQueue.computeIfAbsent(isbn, e -> new ArrayList<>()).add(bookAvailabilityObserver);
        logger.log(Level.INFO, "Reservation queued for ISBN {0}", isbn);
    }

    public Map<String, List<BookAvailabilityObserver>> getReservationQueue() {
        return reservationQueue;
    }
}
