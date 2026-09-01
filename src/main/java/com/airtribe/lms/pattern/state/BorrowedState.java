package com.airtribe.lms.pattern.state;

import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.enums.BookItemStatus;
import com.airtribe.lms.exception.BookNotAvailableException;

public class BorrowedState implements ItemState {

    public static BorrowedState INSTANCE = new BorrowedState();

    private BorrowedState() {
    }

    @Override
    public BookItemStatus getBookItemStatus() {
        return BookItemStatus.BORROWED;
    }

    @Override
    public ItemState returnItem(BookItem item) throws BookNotAvailableException {
        return AvailableState.INSTANCE;
    }
}
