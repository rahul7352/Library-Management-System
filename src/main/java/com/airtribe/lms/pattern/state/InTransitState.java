package com.airtribe.lms.pattern.state;

import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.entity.Branch;
import com.airtribe.lms.enums.BookItemStatus;

public class InTransitState implements ItemState {

    public static final InTransitState INSTANCE = new InTransitState();

    private InTransitState() {}

    @Override
    public BookItemStatus getBookItemStatus() {
        return BookItemStatus.IN_TRANSIT;
    }

    @Override
    public ItemState completeTransfer(BookItem item, Branch destination) {
        item.setBranch(destination);
        return AvailableState.INSTANCE;
    }
}
