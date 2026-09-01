package com.airtribe.lms.service;

import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.entity.Branch;
import com.airtribe.lms.exception.BookNotAvailableException;
import com.airtribe.lms.exception.BookNotFoundException;
import com.airtribe.lms.exception.BranchNotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiBranchTransferService {
    private static final Logger logger = Logger.getLogger(MultiBranchTransferService.class.getName());
    private final BookItemService bookItemService;
    private final BranchService branchService;

    public MultiBranchTransferService(BookItemService bookItemService, BranchService branchService) {
        this.bookItemService = bookItemService;
        this.branchService = branchService;
    }

    public void initiateTransfer(String itemId, String destinationBranchId)
            throws BookNotFoundException, BranchNotFoundException, BookNotAvailableException {
        BookItem bookItem = bookItemService.getBookItemById(itemId);
        Branch destinationBranch = branchService.findBranchById(destinationBranchId);
        String originBranchName = bookItem.getBranch().getBranchName();
        bookItem.startTransfer();
        logger.log(Level.INFO, "Transfer initiated: item {0} from {1} to {2}", new Object[]{itemId, originBranchName, destinationBranch.getBranchName()});

        bookItem.completeTransfer(destinationBranch);
        logger.log(Level.INFO, "Transfer complete: item {0} now at {1}",new Object[] {itemId, destinationBranch.getBranchName()});
    }
}
