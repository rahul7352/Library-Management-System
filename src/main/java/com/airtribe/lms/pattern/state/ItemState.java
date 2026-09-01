package com.airtribe.lms.pattern.state;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.entity.Branch;
import com.airtribe.lms.enums.BookItemStatus;
import com.airtribe.lms.exception.BookNotAvailableException;

public interface ItemState {

    BookItemStatus getBookItemStatus();

    default ItemState checkout(BookItem item) throws BookNotAvailableException {
        throw invalidTransition(item, "checked out");
    }

    default ItemState returnItem(BookItem item) throws BookNotAvailableException {
        throw invalidTransition(item, "returned");
    }

    default ItemState reserve(BookItem item) throws BookNotAvailableException {
        throw invalidTransition(item, "reserved");
    }

    default ItemState startTransfer(BookItem item) throws BookNotAvailableException {
        throw invalidTransition(item, "transferred");
    }

    default ItemState completeTransfer(BookItem item, Branch destination) throws BookNotAvailableException {
        throw invalidTransition(item, "transfer-completed");
    }

    private BookNotAvailableException invalidTransition(BookItem item, String reason) {
        Book book = item.getBook();
        return new BookNotAvailableException(String.format(
                "Item %s ('%s') cannot be %s while %s",
                item.getBookItemId(), book.getTitle(), reason, getBookItemStatus()));
    }
}
