package com.airtribe.lms;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.BookItem;
import com.airtribe.lms.entity.Branch;
import com.airtribe.lms.entity.Patron;
import com.airtribe.lms.exception.BookNotAvailableException;
import com.airtribe.lms.exception.BookNotFoundException;
import com.airtribe.lms.exception.BranchNotFoundException;
import com.airtribe.lms.pattern.factory.BranchFactory;
import com.airtribe.lms.pattern.factory.PatronFactory;
import com.airtribe.lms.pattern.observer.PatronObserver;
import com.airtribe.lms.pattern.strategy.SameAuthorRecommendStrategy;
import com.airtribe.lms.service.*;

public class LibraryManagementSystemApplication {

    static void main() throws BookNotFoundException, BranchNotFoundException, BookNotAvailableException {

        BookInventoryService bookInventoryService = new BookInventoryService();
        BranchService branchService = new BranchService(new BranchFactory());
        BookItemService bookItemService = new BookItemService();
        MultiBranchTransferService transferService = new MultiBranchTransferService(bookItemService, branchService);
        ReservationService reservationService = new ReservationService();
        PatronService patronService = new PatronService(new PatronFactory());
        LendingService lending = new LendingService(bookItemService, reservationService);
        RecommendationService recommender = new RecommendationService(new SameAuthorRecommendStrategy());

        // Catalog entries
        Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);
        Book book2 = new Book("978-0596007126", "Head First Design Patterns", "Freeman & Robson", 2004);
        bookInventoryService.addBook(book1);
        bookInventoryService.addBook(book2);

        // Branches (Factory pattern hides ID generation)
        Branch downtown = branchService.createBranch("Downtown Branch", "12 Main St");
        Branch uptown = branchService.createBranch("Uptown Branch", "88 Park Ave");

        // Physical items — same title, different branches
        BookItem downtownItem = bookItemService.addBookItem(book1, downtown);
        bookItemService.addBookItem(book1, uptown);
        bookItemService.addBookItem(book2, downtown);

        // Patrons (Factory pattern hides ID generation, same as branches)
        Patron alice = patronService.addPatron("Alice", "alice@example.com");
        Patron bob = patronService.addPatron("Bob", "bob@example.com");

        // Alice checks out the Downtown item of Effective Java
        lending.checkout(alice, downtownItem.getBookItemId());

        // Bob wants Effective Java too — reserves by ISBN (queues across branches)
        reservationService.reserveBook(new PatronObserver(bob), book1.getIsbn());

        // Alice returns it -> Bob is notified via the Observer pattern
        lending.returnBook(alice, downtownItem.getBookItemId());

        // Multi-branch: transfer a copy of book2 from Downtown to Uptown
        BookItem toTransfer = bookItemService.addBookItem(book2, downtown);
        transferService.initiateTransfer(toTransfer.getBookItemId(), uptown.getBranchId());

        // Demonstrate updating a catalog entry (only fields you pass change)
        bookInventoryService.updateBookInfo(book2.getIsbn(), "Head First Design Patterns, 2nd Ed.", null, 2021);

        System.out.println("Available items: " + bookItemService.trackAvailableCopies());
        System.out.println("Borrowed items: " + bookItemService.trackBorrowedCopies());
        System.out.println("All branches: " + branchService.findAllBranches());
        System.out.println("Items of Effective Java: " + bookItemService.findCopiesOf(book1.getIsbn()));
        System.out.println("Items now at Uptown: " + bookItemService.findCopiesAtBranch(uptown.getBranchId()));
        System.out.println("Recommendations for Alice: " + recommender.recommendFor(alice, bookInventoryService.getAllBooks(), 3));

    }
}
