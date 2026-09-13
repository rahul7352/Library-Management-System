package com.airtribe.lms.pattern.observer;

import com.airtribe.lms.entity.Book;

public interface BookAvailabilityObserver {

    void onBookAvailable(Book book);
}
