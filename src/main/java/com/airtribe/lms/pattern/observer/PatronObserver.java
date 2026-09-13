package com.airtribe.lms.pattern.observer;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.Patron;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PatronObserver implements BookAvailabilityObserver {

    private static final Logger logger = Logger.getLogger(PatronObserver.class.getName());

    private Patron patron;

    public PatronObserver(Patron patron) {
        this.patron = patron;
    }

    @Override
    public void onBookAvailable(Book book) {
        logger.log(Level.INFO, "Notifying {0} that ''{1}'' is now available", new Object[] {patron.getName(), book.getTitle()});
    }
}
