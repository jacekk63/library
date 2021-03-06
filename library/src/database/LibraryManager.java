package database;

import models.*;

import java.util.*;

public class LibraryManager {

    /*
     * keeps all managers and provides interface to LibraryService
     */

    /**
     * books manager
     */
    private BooksManager booksManager;

    /**
     * books manager
     */
    private UsersManager usersManager;

    /**
     * lent manager
     */
    private LentManager lentManager;

    /**
     * program arguments
     */
    private final Map<String, String> argsMap;

    public LibraryManager(Map<String, String> argsMap) {
        this.argsMap = argsMap;
    }

    /**
     * method initiate library
     */
    public void startLibrary() {
        booksManager = new BooksManager(argsMap);
        usersManager = new UsersManager(argsMap);
        lentManager = new LentManager(argsMap, booksManager, usersManager);

        //update data from database
        if (BaseManager.makeSerialization(argsMap)) {
            // read database status from serialized files
            booksManager.readContent();
            usersManager.readContent();
            lentManager.readContent();
       }
    }

    /**
     * method adds library hard-coded content
     */
    public void initLibraryContent() {
        InitialContent.initLibrary(this);
    }


    /*
     * ***********************************************************************************************************************
     * main methods
     */

    /**
     * 5. Should be possible to add new book to the library. ID should not be passed as argument. ID should be generated inside Library.
     * @param bookType type/kind of book defined in BookType (in future can be used for keeping data in different cache slots to faster access)
     * @param year book release year
     * @param title book title
     * @param authors book authors
     * @return BookModel
     */
    public BookModel addBook(BookType bookType, int year, String title, List<String> authors) {
        return booksManager.addBook(bookType, year, title, authors);
    }

    /**
     * 6. Should be possible to remove given book from the library (by ID) (such action should be possible only if the book with such ID exists and it is not currently lent).
     * method removes book if book exists and it is not currently lent
     * @param id book id
     * @return true if success, false otherwise
     */
    public boolean removeBook(int id) {
        return booksManager.removeBook(id);
    }

    /**
     * 7. Should allow to list all books in the library (distinctly).
     * Returned information should contain information how many is available or lent.
     * You can use simply System.out.println
     */
    public void presentBookStatus() {
        booksManager.presentBookStatus();
    }

    /**
     * 8. Should allow to search book by title, author, year (also other combinations like title AND author).
     */
    public Optional<List<BookModel>> getBooks(boolean fullTitle, String title, String author, int year) {
        return booksManager.getBooks(fullTitle, title, author, year);
    }

    /**
     * 9. Should allow to lent a book by ID (should be forbidden if copy with given ID is already lent).
     *    Should allow to pass the name of the person who lend the book.
     * Method lent specified book to specified user.
     * @param userId user id
     * @param bookId book id
     * @return empty String when book is lent properly, otherwise name of User who lent book
     */
    public Optional<String> lentBook(int userId, int bookId) {
        Optional<LentBooksModel> optLbm = lentManager.lentBook(userId, bookId);
        if (optLbm.isPresent() && optLbm.get().getId() == 0) {
            return Optional.of(optLbm.get().getTmpUserName());
        }
        return Optional.empty();
    }

    /**
     * 10. Should allow to see all book's details by ID (title, author, year, information if it is available or lent together with person name).
     * @param bookId
     */
    public Optional<String> getBookDetails(int bookId) {
        BookModel bm = booksManager.getBook(bookId);
        if (bm != null) {
            StringBuilder sb = new StringBuilder(bm.toString());
            sb.append(bm.getLentUserId() >0 ? " by " + usersManager.getUserFullName(bm.getLentUserId()) : "");
            return Optional.of(sb.toString());
        }
        return Optional.empty();
    }


    /**
     * find all books written by author
     * @param author author
     * @return Optional<List<BookModel>>
     */
    public Optional<List<BookModel>> findBooksByAuthor(final String author) {
        return booksManager.findBooksByAuthor(author);
    }

    /**
     * finds all book released in specific year
     * note: as we haven't specific cache for this operation, its execution in real environment can be longer
     * @param year publication year
     * @return list books released in specific year
     */
    public Optional<List<BookModel>> findBooksByYear(final int year) {
        return booksManager.findBooksByYear(year);
    }

    /**
     * method add user to database (internal caches)
     * @param date registration date
     * @param name user name
     * @param surname user surname
     * @return UserModel
     */
    public UserModel registerUser(Date date, String name, String surname) {
        return usersManager.registerUser(date, name, surname);
    }

    /**
     * get user name
     * @param name user name
     * @param surname user surname
     * @return UserModel,
     * note: if user name is not in cache can return null value; could be replaced by Optional<UserName> like in BooksManager
     */
    protected UserModel getUser(String name, String surname) {
        return usersManager.getUser(name, surname);
    }

    /**
     * finds books in cache for specific begin of title
     * @param titleShape title's beginning
     * @return Optional<List<BookModel>> list books with specific beginning of title
     */
    public Optional<List<BookModel>> findBooksWithBeginOfBookTittle(String titleShape) {
        return booksManager.findBooksWithBeginOfBookTittle(titleShape);
    }

    /**
     * finds books in cache for specific full title
     * @param title full book title
     * @return Optional<List<BookModel>>  list books with specific title
     */
    public Optional<List<BookModel>> findBooksWithFullTittle(String title) {
        return booksManager.findBooksWithFullTittle(title);
    }

    /**
     * finds books in cache with specific full title
     * @param title full book title
     * @return Optional<List<BookModel>>  list books with specific title
     */
    public Optional<BookModel> findAvailableBookWithFullTittle(String title) {
        Optional<List<BookModel>> opt = booksManager.findBooksWithFullTittle(title);
        if (opt.isPresent()) {
            return opt.get().stream().filter(a -> (!a.isBookLent())).findFirst();
        }
        return Optional.empty();
    }

    public Optional<LentBooksModel> lentBook(UserModel user, BookModel bookModel) {
        return lentManager.addLentBooksModel(new Date(), user, bookModel);
    }

    /*
     * auxiliary methods ==============================================================================
     */
    public void storeContent() {
        booksManager.storeContent();
        usersManager.storeContent();
        lentManager.storeContent();
    }

    public void displayContent() {
        System.out.println("Library content will be printed:");
        booksManager.displayContent();
        usersManager.displayContent();
        lentManager.displayContent();
    }

    /*
    not used methods
     */
    public Map<Integer, UserModel> getAllUsers() {
        return usersManager.getAllUsers();
    }
}
