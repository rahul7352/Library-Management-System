package com.airtribe.lms.pattern.state;

import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.enums.BookItemStatus;
import com.airtribe.lms.exception.BookNotAvailableException;

public class AvailableState implements ItemState {

    public static final AvailableState INSTANCE = new AvailableState();

    private AvailableState() {}

    @Override
    public BookItemStatus getBookItemStatus() {
        return BookItemStatus.AVAILABLE;
    }

    @Override
    public ItemState checkout(BookItem item) throws BookNotAvailableException {
        return BorrowedState.INSTANCE;
    }

    @Override
    public ItemState reserve(BookItem item) throws BookNotAvailableException {
        return ReservedState.INSTANCE;
    }

    @Override
    public ItemState startTransfer(BookItem item) throws BookNotAvailableException {
        return InTransitState.INSTANCE;
    }
}
