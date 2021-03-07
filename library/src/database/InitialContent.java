/**
 * InitialContent
 * @author: Jacek Kulesz
 * @date: 2020.03.07
 */
package database;

import models.BookModel;
import models.BookType;
import models.LentBooksModel;
import models.UserModel;

import java.util.*;

public class InitialContent {

    /**
     * finals to tests purpose, tests are defined in TestLibraryMethods class
     */
    private static final String NOTHING_OR_MORE = "Nothing or more";
    public static final String NOTHING_TO_LOSE = "Nothing to lose";
    public static final String NOTHING = "Nothing";
    private static final String JACK_REACHER = "Jack Reacher";
    public static final int NOTHING_TO_LOSE_COUNT = 20;
    public static final int NOTHING_OR_MORE_COUNT = 30;
    private static final String JOE_DOE = "Joe Doe";
    private static final String YET_ANOTHER_JOE_DOE = "Yet Another Joe Doe";
    public static final String GREENLIGHTS = "Greenlights";
    public static final int GREENLIGHTS_NUMBER_OF_BOOKS = 11;
    public static final String OPTIMIZING_JAVA_PRACTICAL = "Optimizing Java: Practical techniques for improving JVM application performance";
    public static final int OPTIMIZING_JAVA_PRACTICAL_CNT = 1;
    public static final String EFFECTIVE_JAVA = "Effective Java";
    public static final int EFFECTIVE_JAVA_CNT = 10;
    public static final String BLUE_MOON = "Blue Moon";

    /**
     * method adds initial hardcoded library content
     */
    public static void initLibrary(LibraryManager libraryManager) {
        initBooksContent(libraryManager);
        initUsersContent(libraryManager);
        initLentManagerContent(libraryManager);
    }

    /**
     * initialize library by hardcoded content , here is initialized books content
     * @param libraryManager
     */
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

    private static void initUsersContent(LibraryManager libraryManager) {
        libraryManager.registerUser(new Date(), "Jacek", "Placek"); //userId = 1
        libraryManager.registerUser(new Date(), "Joe", "Doe");      //userId = 2
        libraryManager.registerUser(new Date(), "Joe", "Alex");     //userId = 3
    }

    private static  void initLentManagerContent(LibraryManager libraryManager) {
        //user Jacek Placek will lent two books
        UserModel user = libraryManager.getUser("Jacek", "Placek");
        if (user != null) {
            lentBook(libraryManager, user, OPTIMIZING_JAVA_PRACTICAL);  // 1st lent book
            lentBook(libraryManager, user, EFFECTIVE_JAVA);             // 2nd lent book
        } else {
            System.err.println("User Jacek Placek not found!");
        }

        //user Joe Alex will lent three books
        user = libraryManager.getUser("Joe", "Alex");
        if (user != null) {
            lentBook(libraryManager, user, NOTHING_TO_LOSE);  // 1st lent book
            lentBook(libraryManager, user, EFFECTIVE_JAVA);   // 2nd lent book
            lentBook(libraryManager, user, BLUE_MOON);        // 3rd lent book
        } else {
            System.err.println("User Joe Alex not found!");
        }
    }

    private static void lentBook(LibraryManager libraryManager, UserModel user, String title) {
        Optional<BookModel> opt = libraryManager.findAvailableBookWithFullTittle(title);
        if (opt.isPresent()) {
            Optional<LentBooksModel> optBook = libraryManager.lentBook(user, opt.get());
            if (!optBook.isPresent()) {
                System.err.println("Problem when lent book: " + title + " by user: " + user.getFullUserName());
            }
        } else {
            System.err.println("Book: " + title + " should be available.");
        }
    }
}
