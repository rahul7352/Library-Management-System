package com.airtribe.lms.pattern.state;

import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.enums.BookItemStatus;
import com.airtribe.lms.exception.BookNotAvailableException;

public class ReservedState implements ItemState {

    public static final ReservedState INSTANCE = new ReservedState();

    private ReservedState() {}

    @Override
    public BookItemStatus getBookItemStatus() {
        return BookItemStatus.RESERVED;
    }

    @Override
    public ItemState checkout(BookItem item) throws BookNotAvailableException {
        return BorrowedState.INSTANCE;
    }
}
