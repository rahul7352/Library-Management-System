# Library Management System

A console-oriented Java skeleton demonstrating OOPS, SOLID, and design
patterns for the Airtribe Library Management System assignment.

## Package structure

```
com.airtribe.lms
├── LibraryManagementSystemApplication.java                  wiring / demo entry point
├── entity/                     Book, EBook, Patron, Branch, BookItem
├── enums/                     BookItemStatus
├── service/                   BookInventoryService, PatronService,
│                               LendingService, RecommendationService, ReservationService
│                               BranchService, BookItemService, MultiBranchTransferService
├── pattern/observer/          reservation notifications
├── pattern/strategy/          pluggable recommendation algorithms
├── pattern/factory/           branch creation (BranchFactory)
├── pattern/state/             BookItem lifecycle (ItemState + concrete states)
└── exception/                 domain-specific checked exceptions
```

## Multi-branch model

`Book` is a pure catalog entry (title/author/ISBN/year) with no notion of
availability. A `BookItem` is a specific physical/lendable copy of a
`Book` sitting at a specific `Branch`, and status (`AVAILABLE` /
`BORROWED` / `RESERVED` / `IN_TRANSIT`) lives on the item, not the
catalog entry. This is what lets one title have items simultaneously
borrowed at one branch and sitting available at another.

- `BookItemService` owns all items, indexed by item ID, with lookups by
  ISBN and by branch.
- `LendingService.checkout`/`returnBook` now take an **item ID**, not an
  ISBN — you're borrowing a specific physical item, not an abstract title.
- `TransferService.initiateTransfer(itemId, destinationBranchId)` moves an
  item between branches: it must be `AVAILABLE` to start, is marked
  `IN_TRANSIT` so it can't be checked out mid-move, then flips to
  `AVAILABLE` at the destination. There's no real courier/logistics
  system (out of scope per the assignment brief), so the transfer
  completes synchronously — logged as two steps to show the lifecycle.
- Reservations (`ReservationService.reserveBook`) are still queued **by ISBN**,
  not by item/branch — a patron wanting a title generally doesn't care
  which branch's item frees up first. Documented here as a deliberate
  simplification; branch-scoped reservations would be the natural next
  step if needed.
- **Inventory Management (available vs. borrowed)**: `BookItemService`
  exposes `trackAvailableCopies()`, `trackBorrowedCopies()`,
  `getBookCopiesByStatus(ItemStatus)`, and `getAllBookItems()` — direct queries over
  every item's status across all branches, satisfying the "keep track of
  available and borrowed books" requirement as a first-class feature
  rather than an implicit side effect of checkout/return.

## OOP concepts

- **Encapsulation**: all model fields are private with controlled
  accessors; `Patron`'s borrowing lists are exposed as unmodifiable views.
- **Inheritance**: `EBook extends Book` adds ebook-specific metadata
  without touching `Book`.
- **Polymorphism**: `Book.describe()` is overridden by `EBook`; callers
  (e.g. `Main`, `BookInventoryService`) never branch on type.
- **Abstraction**: `RecommendationStrategy` and `BookAvailabilityObserver`
  are interfaces; services depend on them, not concrete classes.

## SOLID

- **SRP**: catalog management, patron management, and lending are three
  separate services rather than one god class.
- **OCP**: new book types (`EBook`) and new recommendation algorithms
  (`RecommendationStrategy` implementations) can be added without
  modifying existing services.
- **LSP**: any `Book` subtype can be used wherever `Book` is expected.
- **ISP**: `BookAvailabilityObserver` and `RecommendationStrategy` are
  each a single-method interface — no fat interfaces to implement.
- **DIP**: `LendingService` depends on the `BookAvailabilityObserver`
  abstraction for reservation notifications, not on `Patron` or email/SMS
  directly.

## Design patterns

1. **Observer** — reservation system. `LendingService` keeps a
   per-book reservation queue of `BookAvailabilityObserver`s; on
   `returnBook`, the next queued observer is notified. `PatronObserver`
   adapts a `Patron` into an observer.
2. **Strategy** — recommendation system. `RecommendationService` holds a
   swappable `RecommendationStrategy` (`SameAuthorRecommendStrategy` provided, now
   ranked by author-affinity before truncating to the requested limit);
   add a `MostBorrowedStrategy` or `GenreStrategy` without changing the
   service.
3. **Factory** — ID-generating construction, applied twice:
    - `BranchFactory` centralizes `Branch` construction (including ID
      generation), so `BranchService` and callers never build branch IDs
      themselves.
    - `PatronFactory` does the same for `Patron` — this one closes a real
      gap: without it, callers would invent patron IDs themselves (e.g.
      `"P001"`), risking collisions and inconsistent formats. `PatronService`
      exposes `addPatron(name, email)` as the only way to add a patron;
      there's no longer a path that lets a caller supply their own ID.
    - Both extend `SequentialIdFactory`, which holds the shared
      counter/prefix/format logic. This is *not* a single factory that
      builds both `Branch` and `Patron` — that would mix two unrelated
      construction responsibilities into one class (SRP violation).
      Instead, the ID-generation mechanics are shared; what each ID means
      and which entity it builds stays in its own focused factory.
4. **State** — `BookItem` lifecycle (`AVAILABLE` → `BORROWED` /
   `RESERVED` / `IN_TRANSIT`). Each status is a class
   (`AvailableState`, `BorrowedState`, `ReservedState`, `InTransitState`)
   implementing `ItemState`, which knows which transitions are legal from
   itself; `BookItem` delegates every action (`checkout()`, `returnItem()`,
   `reserve()`, `startTransfer()`, `completeTransfer()`) to its current
   state and swaps in whatever state comes back.
   `BookNotAvailableException` if the transition wasn't legal. This also
   closes an encapsulation gap: Only way to change
   status is through a state-validated action.

## Data structures

- `Map<String, Book>` keyed by ISBN in `BookInventoryService` for O(1)
  add/remove/lookup (catalog).
- `Map<String, BookItem>` keyed by item ID in `BookItemService`, filtered
  by ISBN or branch ID for cross-branch queries.
- `List<BookAvailabilityObserver>` per ISBN in `LendingService` as a
  FIFO reservation queue.
- `List<Book>` for a patron's borrowing history/current loans.

## Running

```bash
mvn compile exec:java
```

## Status

Core lending/catalog/patron flow, reservations, recommendations,
multi-branch support (branches, per-item copies, inter-branch transfer),
and item lifecycle state transitions are implemented and verified —
compiled and run end-to-end against a local JDK, including a negative-path
check that illegal transitions (e.g. double-checkout, transferring a
borrowed item) are correctly rejected.
