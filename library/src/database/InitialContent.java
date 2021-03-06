package database;

import models.BookModel;
import models.BookType;
import models.LentBooksModel;
import models.UserModel;

import java.util.*;

public class InitialContent {

    /**
     * finals to tests purpose
     */
    private static final String NOTHING_OR_MORE = "Nothing or more";
    private static final String NOTHING_TO_LOSE = "Nothing to Lose";
    private static final String NOTHING = "Nothing";
    private static final String JACK_REACHER = "Jack Reacher";
    private static final int NOTHING_TO_LOSE_COUNT = 20;
    private static final int NOTHING_OR_MORE_COUNT = 30;
    private static final String JOE_DOE = "Joe Doe";
    private static final String YET_ANOTHER_JOE_DOE = "Yet Another Joe Doe";
    private static final String JAVA = "Java";
    private static final String GREENLIGHTS = "Greenlights";
    private static final int GREENLIGHTS_NUMBER_OF_BOOKS = 11;
    private static final String OPTIMIZING_JAVA_PRACTICAL = "Optimizing Java: Practical techniques for improving JVM application performance";
    private static final int OPTIMIZING_JAVA_PRACTICAL_CNT = 1;
    private static final String EFFECTIVE_JAVA = "Effective Java";
    private static final int EFFECTIVE_JAVA_CNT = 10;
    public static final String BLUE_MOON = "Blue Moon";

    static {
        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (!assertsEnabled) {
            System.err.println("Asserts should be enabled!!! Use the -enableassertions, or -ea in command-line Java VM options.");
            System.out.println();
        } else {
            System.out.println("Asserts are enabled. Test library methods.");
            System.out.println();
        }
    }

    private static List<UserModel> usersList;

    /**
     * method adds initial hardcoded library content
     */
    public static void initLibrary(LibraryManager libraryManager) {
        initBooksContent(libraryManager);
        usersList = initUsersContent(libraryManager);
        initLentManagerContent(libraryManager);
    }

    private static void initBooksContent(LibraryManager libraryManager) {
        List<String> authors = new ArrayList<>();
        authors.add("von Kevlin Henney");
        authors.add("Trisha Gee");
        addBooks(libraryManager, 2017, authors, BookType.PROGRAMING,
                "97 Things Every Java Programmer Should Know: " +
                        "Collective Wisdom from the Experts " +
                        "(Englisch) Taschenbuch – Illustriert, 27. Januar 2017",
                10);

        authors = new ArrayList<>();
        authors.add("Scott Oaks");
        addBooks(libraryManager, 2020, authors, BookType.PROGRAMING,
                "Java Performance: In-depth Advice for Tuning " +
                        "and Programming Java 8, 11, and Beyond Paperback – 31 Mar. 2020",
                5);

        authors = new ArrayList<>();
        authors.add("Benjamin Evans");
        authors.add("James Gough");
        authors.add("Chris Newland");
        addBooks(libraryManager, 2020, authors, BookType.PROGRAMING,
                OPTIMIZING_JAVA_PRACTICAL,
                OPTIMIZING_JAVA_PRACTICAL_CNT);

        authors = new ArrayList<>();
        authors.add("Joshua Bloch");
        addBooks(libraryManager, 2020, authors, BookType.PROGRAMING,
                EFFECTIVE_JAVA,
                EFFECTIVE_JAVA_CNT);

        authors = new ArrayList<>();
        authors.add("Matthew McConaughey");
        authors.add("Random House");
        addBooks(libraryManager, 2019, authors, BookType.BIOGRAPHY,
                GREENLIGHTS,
                GREENLIGHTS_NUMBER_OF_BOOKS);

        authors = new ArrayList<>();
        authors.add(JACK_REACHER);
        addBooks(libraryManager, 2018, authors, BookType.NOVEL,
                NOTHING_TO_LOSE,
                NOTHING_TO_LOSE_COUNT);
        addBooks(libraryManager, 2019, authors, BookType.NOVEL,
                "Blue Moon",
                15);

        authors = new ArrayList<>();
        authors.add(JOE_DOE);
        authors.add(YET_ANOTHER_JOE_DOE);
        addBooks(libraryManager, 2019, authors, BookType.NOVEL,
                NOTHING_OR_MORE,
                NOTHING_OR_MORE_COUNT);

        // again add same books
        authors = new ArrayList<>();
        authors.add("Joshua Bloch");
        addBooks(libraryManager, 2021, authors, BookType.PROGRAMING,
                EFFECTIVE_JAVA,
                1);
    }

    private static void addBooks(LibraryManager libraryManager,
                                 int year, List<String> authors, BookType bookType, String title,
                                 int numberOfBooksToAdd) {
        for (int i=0; i<numberOfBooksToAdd; i++) {
            libraryManager.addBook(bookType, year, title, authors);
        }
    }

    private static List<UserModel> initUsersContent(LibraryManager libraryManager) {
        List<UserModel> list = new ArrayList<>();
        UserModel um = libraryManager.registerUser(new Date(), "Jacek", "Placek");
        list.add(um);
        assert um.getId() == 1;
        um = libraryManager.registerUser(new Date(), "Joe", "Doe");
        list.add(um);
        assert um.getId() == 2;
        um = libraryManager.registerUser(new Date(), "Joe", "Alex");
        list.add(um);
        assert um.getId() == 3;
        return list;
    }

    private static  void initLentManagerContent(LibraryManager libraryManager) {
        findBooksWithFullTittleTest(libraryManager);

        //user Jacek Placek will lent two books
        UserModel user = libraryManager.getUser("Jacek", "Placek");
        if (user != null) {
            lentBookTest(libraryManager, user, OPTIMIZING_JAVA_PRACTICAL, 1);
            lentBookTest(libraryManager, user, EFFECTIVE_JAVA, 2);
        } else {
            assert user != null
                    : getAssertionInfo("user ", "null", "Jacek" +" " + "Placek");
        }

        //user Joe Alex will lent three books
        user = libraryManager.getUser("Joe", "Alex");
        if (user != null) {
            lentBookTest(libraryManager, user, NOTHING_TO_LOSE, 1);
            lentBookTest(libraryManager, user, EFFECTIVE_JAVA, 2);
            lentBookTest(libraryManager, user, BLUE_MOON, 3);
        } else {
            assert user != null
                    : getAssertionInfo("user ", "null", "Joe" +" " + "Alex");
        }
    }

    private static void findBooksWithFullTittleTest(LibraryManager libraryManager) {
        Optional<List<BookModel>> listBook1 = libraryManager.findBooksWithFullTittle(OPTIMIZING_JAVA_PRACTICAL);
        if (listBook1.isPresent()) {
            assert listBook1.get().size() == OPTIMIZING_JAVA_PRACTICAL_CNT
                    : getAssertionInfo("number of books " + OPTIMIZING_JAVA_PRACTICAL + " ", listBook1.get().size(), OPTIMIZING_JAVA_PRACTICAL_CNT);
        }
        Optional<List<BookModel>> listBook2 = libraryManager.findBooksWithFullTittle(EFFECTIVE_JAVA);
        if (listBook2.isPresent()) {
            assert listBook2.get().size() == EFFECTIVE_JAVA_CNT + 1
                    : getAssertionInfo("number of books " + EFFECTIVE_JAVA + " ", listBook2.get().size(), EFFECTIVE_JAVA_CNT + 1);
        }
    }

    private static void lentBookTest(LibraryManager libraryManager, UserModel user, String fullTitle, int lentBookNumber) {
        Optional<BookModel> opt = libraryManager.findAvailableBookWithFullTittle(fullTitle);
        if (opt.isPresent()) {
            Optional<LentBooksModel> lentBooksModelOptional = libraryManager.lentBook(user, opt.get());
            assert lentBooksModelOptional.isPresent() && lentBooksModelOptional.get().getId() == user.getId()
                    : getAssertionInfo("user id", lentBooksModelOptional.get().getId(), user.getId());
            assert lentBooksModelOptional.isPresent() && lentBooksModelOptional.get().getLentBooks().size() == lentBookNumber
                    : getAssertionInfo("lent book number", lentBooksModelOptional.get().getLentBooks().size(), lentBookNumber);
            assert opt.get().isBookLent()
                    : getAssertionInfo("book should be lent but is not, userId ", 0, user.getId());
        } else {
            assert opt.isPresent()
                    : getAssertionInfo("number of available books " + fullTitle + " ", 0, 1);
        }
    }

    private static String getAssertionInfo(String beg, int badValue, int properValue) {
        return ("Improper " + beg + ": " + badValue + " instead of: " + properValue);
    }

    private static String getAssertionInfo(String beg, String badValue, String properValue) {
        return ("Improper " + beg + ": '" + badValue + "' instead of: '" + properValue + "'");
    }

    private static void testBookDetails(LibraryManager library) {
        //book should exists
        Optional<BookModel> optBook = library.findAvailableBookWithFullTittle(EFFECTIVE_JAVA);
        testDetails(library, optBook, EFFECTIVE_JAVA);

        // book is not available for lent
        optBook = library.findAvailableBookWithFullTittle(OPTIMIZING_JAVA_PRACTICAL);
        testDetails(library, optBook, OPTIMIZING_JAVA_PRACTICAL);

        // book is not available for lent
        Optional<List<BookModel>> list = library.findBooksWithFullTittle(OPTIMIZING_JAVA_PRACTICAL);
        if (list.isPresent()) {
            optBook = Optional.of(list.get().get(0));
            testDetails(library, optBook, OPTIMIZING_JAVA_PRACTICAL);
        }
    }

    private static void testDetails(LibraryManager library, Optional<BookModel> optBook, String title) {
        if (optBook.isPresent()) {
            Optional<String> opt = library.getBookDetails(optBook.get().getId());
            if (opt.isPresent()) {
                System.out.println(opt.get());
            } else {
                System.out.println("Cannot find book with id: " + optBook.get().getId());
            }
        } else {
            System.out.println("Book with title: " + title + " is not available");
        }
    }

    private static void testLentBooks(LibraryManager library) {
        // book is available
        Optional<BookModel> optBook = library.findAvailableBookWithFullTittle(EFFECTIVE_JAVA);
        if (optBook.isPresent()) {
            Optional<String> opt = library.lentBook(usersList.get(0).getId(), optBook.get().getId());
            if (opt.isPresent()) {
                assert opt.isPresent() : "Book " + EFFECTIVE_JAVA + " cannot be lent. It is already lent by " + opt.get();
            }
        }

        // book is not available
        optBook = library.findAvailableBookWithFullTittle(OPTIMIZING_JAVA_PRACTICAL);
        if (optBook.isPresent()) {
            Optional<String> opt = library.lentBook(usersList.get(0).getId(), optBook.get().getId());
            if (opt.isPresent()) {
                System.out.println("Book " + OPTIMIZING_JAVA_PRACTICAL + " cannot be lent. It is already lent by " + opt.get());
            }
        }
    }

    private static void removeBookTest(LibraryManager library) {
        // remove book - Should be possible to remove given book from the library (by ID) (such action should be possible only if the book with such ID exists and it is not currently lent).

        // book should be available because nobody lent it yet
        Optional<BookModel> opt = library.findAvailableBookWithFullTittle(GREENLIGHTS);
        System.out.println(opt.isPresent());
        assert opt.isPresent() == true
                : getAssertionInfo("books number available: ", 0, GREENLIGHTS_NUMBER_OF_BOOKS);

        // try to remove existing book, that is not lent
        boolean removedBm = library.removeBook(opt.get().getId());
        assert removedBm == true
                : "book " + opt.get().getTitle() + " cannot be removed, book id: " + opt.get().getId();

        // try to remove existing book, a few is lent but there are still available books
        opt = library.findAvailableBookWithFullTittle(EFFECTIVE_JAVA);
        if (opt.isPresent()) {
            removedBm = library.removeBook(opt.get().getId());
            assert removedBm == true
                    : "book cannot be removed, book id: " + opt.get().getId() + " title: " + opt.get().getTitle();
        } else {
            assert !opt.isPresent()
                    : getAssertionInfo("number of available books " + EFFECTIVE_JAVA + " ", 0, 1);
        }

        // try to remove non existing book
        removedBm = library.removeBook(-1);
        if (removedBm) {
            assert true : "book cannot be removed, book id: " + -1;
        }
    }

    private static void testComplexBooksSearch(LibraryManager library) {
        Optional<List<BookModel>> optList = library.getBooks (true, "", "", 0);
        assert !optList.isPresent() : "method should return empty list";
        optList = library.getBooks (true, "ABC not existed book", "", 0);
        assert !optList.isPresent() : "method should return empty list";
        optList = library.getBooks (true, "", "ABC not existed author", 0);
        assert !optList.isPresent() : "method should return empty list";
        optList = library.getBooks (true, "", null, 1900);
        assert !optList.isPresent() : "method should return empty list";
        optList = library.getBooks (true, "ABC not existed book", "ABC not existed author", 1234);
        assert !optList.isPresent() : "method should return empty list";

        optList = library.getBooks (true, " ", " ", 2020);
        assert optList.isPresent() : "method should return non empty list";

        optList = library.getBooks (true, EFFECTIVE_JAVA, " ", 0);
        assert optList.isPresent() : "method should return non empty list";

        optList = library.getBooks (true, EFFECTIVE_JAVA, " ", 2020);
        assert optList.isPresent() : "method should return non empty list";
        assert optList.get().size() == 9 : "method should return list of 9 elements";

        optList = library.getBooks (true, EFFECTIVE_JAVA, " ", 2021);
        assert optList.isPresent() : "method should return non empty list";
        assert optList.get().size() == 1 : "method should return one element list";

        optList = library.getBooks (true, EFFECTIVE_JAVA, "Non-Existing Author", 2021);
        assert !optList.isPresent() : "method should return empty list";

        optList = library.getBooks (true, GREENLIGHTS, "Random House", 2019);
        assert optList.isPresent() : "method should return non-empty list";
        assert optList.get().size() == 10 : "method should return list of " + 10 + " elements";

        optList = library.getBooks (true, GREENLIGHTS, "Matthew McConaughey", 2019);
        assert optList.isPresent() : "method should return non-empty list";
        assert optList.get().size() == 10 : "method should return list of " + 10 + " elements";
    }


    public static void testLibraryMethods(LibraryManager library) {
        System.out.println();
        Optional<List<BookModel>> optListBooks = library.findBooksWithBeginOfBookTittle(NOTHING);
        if (optListBooks.isPresent()) {
            assert optListBooks.get().size() == NOTHING_TO_LOSE_COUNT + NOTHING_OR_MORE_COUNT;
        }

        optListBooks = library.findBooksWithBeginOfBookTittle(NOTHING_TO_LOSE);
        if (optListBooks.isPresent()) {
            assert (optListBooks.get().size() == NOTHING_TO_LOSE_COUNT)
                    : getAssertionInfo("books number", optListBooks.get().size(), 1);
        }

        optListBooks = library.findBooksWithBeginOfBookTittle("ZZZZZZ");
        if (optListBooks.isPresent()) {
            assert (optListBooks.get().size() == 0)
                    : getAssertionInfo("books number", optListBooks.get().size(), 0);
        }


        removeBookTest(library);

        testLentBooks(library);

        library.presentBookStatus();

        testBookDetails(library);

        testComplexBooksSearch(library);

        // users
        // lent books
        // find lent only lent books

/*
5. Should be possible to add new book to the library. ID should not be passed as argument. ID should be generated inside Library.
6. Should be possible to remove given book from the library (by ID) (such action should be possible only if the book with such ID exists and it is not currently lent).
7. Should allow to list all books in the library (distinctly). Returned information should contain 7. Should allow to list all books in the library (distinctly). Returned information should contain information how many is available or lent. You can use simply System.out.println
8. Should allow to search book by title, author, year (also other combinations like title AND author).
9. Should allow to lent a book by ID ( should be forbidden if copy with given ID is already lent). Should allow to pass the name of the person who lend the book.
10. Should allow to see all book's details by ID (title, author, year, information if it is available or lent together with person name).
 */
        System.out.println("Test library methods passed.");
    }

}
