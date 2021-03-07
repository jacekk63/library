/**
 * TestLibraryMethods
 * @author: Jacek Kulesz
 * @date: 2020.03.07
 */
package tests;

import database.InitialContent;
import database.LibraryManager;
import models.BookModel;
import models.UserModel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TestLibraryMethods {

    private static void testFindBooksWithFullTittle(LibraryManager library) {
        Optional<List<BookModel>> listBook1 = library.findBooksWithFullTittle(InitialContent.OPTIMIZING_JAVA_PRACTICAL);
        if (listBook1.isPresent()) {
            assert listBook1.get().size() == InitialContent.OPTIMIZING_JAVA_PRACTICAL_CNT
                    : getAssertionInfo("number of books " + InitialContent.OPTIMIZING_JAVA_PRACTICAL
                    + " ", listBook1.get().size(), InitialContent.OPTIMIZING_JAVA_PRACTICAL_CNT);
        }

        // one book has been added later
        int counter = InitialContent.EFFECTIVE_JAVA_CNT + 1;
        Optional<List<BookModel>> listBook2 = library.findBooksWithFullTittle(InitialContent.EFFECTIVE_JAVA);
        if (listBook2.isPresent()) {
            assert listBook2.get().size() == counter
                    : getAssertionInfo("number of books " + InitialContent.EFFECTIVE_JAVA
                    + " ", listBook2.get().size(), counter);
        }
    }

    private static void testFindBooksWithBeginOfBookTittle(LibraryManager library) {
        /**
         * Library contains two titles beginning from InitialContent.NOTHING.
         * Number of books for each title is InitialContent.NOTHING_TO_LOSE_COUNT and InitialContent.NOTHING_OR_MORE_COUNT
         */
        Optional<List<BookModel>> optListBooks = library.findBooksWithBeginOfBookTittle(InitialContent.NOTHING);
        if (optListBooks.isPresent()) {
            assert optListBooks.get().size() == InitialContent.NOTHING_TO_LOSE_COUNT + InitialContent.NOTHING_OR_MORE_COUNT;
        }

        /**
         * Test using full book name instead od short one
         */
        optListBooks = library.findBooksWithBeginOfBookTittle(InitialContent.NOTHING_TO_LOSE);
        if (optListBooks.isPresent()) {
            assert (optListBooks.get().size() == InitialContent.NOTHING_TO_LOSE_COUNT)
                    : getAssertionInfo("books number", optListBooks.get().size(), 1);
        }

        /**
         * Trying to find non-existing title
         */
        optListBooks = library.findBooksWithBeginOfBookTittle("ZZZZZZ");
        assert !optListBooks.isPresent() || (optListBooks.get().size() == 0)
                : getAssertionInfo("books number", optListBooks.get().size(), 0);
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

        optList = library.getBooks (true, InitialContent.EFFECTIVE_JAVA, " ", 0);
        assert optList.isPresent() : "method should return non empty list";

        optList = library.getBooks (true, InitialContent.EFFECTIVE_JAVA, " ", 2020);
        assert optList.isPresent() : "method should return non empty list";
        assert optList.get().size() == 9 : "method should return list of 9 elements";

        optList = library.getBooks (true, InitialContent.EFFECTIVE_JAVA, " ", 2021);
        assert optList.isPresent() : "method should return non empty list";
        assert optList.get().size() == 1 : "method should return one element list";

        optList = library.getBooks (true, InitialContent.EFFECTIVE_JAVA, "Non-Existing Author", 2021);
        assert !optList.isPresent() : "method should return empty list";

        optList = library.getBooks (true, InitialContent.GREENLIGHTS, "Random House", 2019);
        assert optList.isPresent() : "method should return non-empty list";
        assert optList.get().size() == 10 : "method should return list of " + 10 + " elements";

        optList = library.getBooks (true, InitialContent.GREENLIGHTS, "Matthew McConaughey", 2019);
        assert optList.isPresent() : "method should return non-empty list";
        assert optList.get().size() == 10 : "method should return list of " + 10 + " elements";
    }

    private static void removeBookTest(LibraryManager library) {
        // remove book - Should be possible to remove given book from the library (by ID) (such action should be possible only if the book with such ID exists and it is not currently lent).

        // book should be available because nobody lent it yet
        Optional<BookModel> opt = library.findAvailableBookWithFullTittle(InitialContent.GREENLIGHTS);
        assert opt.isPresent()
                : getAssertionInfo("books number available: ", 0, InitialContent.GREENLIGHTS_NUMBER_OF_BOOKS);

        // try to remove existing book, that is not lent
        boolean removedBm = library.removeBook(opt.get().getId());
        assert removedBm
                : "book " + opt.get().getTitle() + " cannot be removed, book id: " + opt.get().getId();

        // try to remove existing book, a few is lent but there are still available books
        opt = library.findAvailableBookWithFullTittle(InitialContent.EFFECTIVE_JAVA);
        if (opt.isPresent()) {
            removedBm = library.removeBook(opt.get().getId());
            assert removedBm
                    : "book cannot be removed, book id: " + opt.get().getId() + " title: " + opt.get().getTitle();
        } else {
            assert true
                    : getAssertionInfo("number of available books " + InitialContent.EFFECTIVE_JAVA + " ", 0, 1);
        }

        // try to remove non existing book
        removedBm = library.removeBook(-1);
        assert !removedBm  : "book cannot be removed, book id: " + -1;
    }

    private static void testLentBooks(LibraryManager library) {
        // book is available
        Optional<BookModel> optBook = library.findAvailableBookWithFullTittle(InitialContent.EFFECTIVE_JAVA);
        if (optBook.isPresent()) {
            //User "Jacek", "Placek" => userId = 1
            UserModel um = library.getUser("Jacek", "Placek");
            Optional<String> opt = library.lentBook(um.getId(), optBook.get().getId());
            if (opt.isPresent()) {
                assert opt.isPresent() : "Book " + InitialContent.EFFECTIVE_JAVA + " cannot be lent. It is already lent by " + opt.get();
            }
        }

        // book is not available
        optBook = library.findAvailableBookWithFullTittle(InitialContent.OPTIMIZING_JAVA_PRACTICAL);
        assert !optBook.isPresent() : "Book " + InitialContent.OPTIMIZING_JAVA_PRACTICAL + " is available but shouldn't be.";
        if (! optBook.isPresent()) {
            Optional<List<BookModel>> optListBook = library.findBooksWithFullTittle(InitialContent.OPTIMIZING_JAVA_PRACTICAL);
            if (optListBook.isPresent()) {
                assert optListBook.get().size() == 1 : "Unexpected umber of books with title: "
                        + InitialContent.OPTIMIZING_JAVA_PRACTICAL + " found: " + optListBook.get().size()
                        + " but expected 1.";
                UserModel um = library.getUser("Joe", "Doe");
                Optional<String> opt = library.lentBook(um.getId(), optListBook.get().get(0).getId());
                if (opt.isPresent()) {
                    System.out.println("Book " + InitialContent.OPTIMIZING_JAVA_PRACTICAL + " cannot be lent. It is already lent by " + opt.get());
                }
            }
        }
    }

    private static String getAssertionInfo(String beg, int badValue, int properValue) {
        return ("Improper " + beg + ": " + badValue + " instead of: " + properValue);
    }

    private static String getAssertionInfo(String beg, String badValue, String properValue) {
        return ("Improper " + beg + ": '" + badValue + "' instead of: '" + properValue + "'");
    }

    private static void testSpecificBookDetails(LibraryManager library) {
        System.out.println();
        System.out.println("testSpecificBookDetails");
        //book should exists
        Optional<List<BookModel>> optBookList = library.findBooksWithFullTittle(InitialContent.EFFECTIVE_JAVA);
        if (optBookList.isPresent()) {
            testDetails(library, optBookList.get().get(0));
        } else {
            assert true : "Cannot find book with title: " + InitialContent.EFFECTIVE_JAVA;
        }

        // book is not available for lent
        optBookList = library.findBooksWithFullTittle(InitialContent.OPTIMIZING_JAVA_PRACTICAL);
        if (optBookList.isPresent()) {
            testDetails(library, optBookList.get().get(0));
        } else {
            assert true : "Cannot find book with title: " + InitialContent.OPTIMIZING_JAVA_PRACTICAL;
        }

        // book is available for lent
        optBookList = library.findBooksWithFullTittle(InitialContent.GREENLIGHTS);
        if (optBookList.isPresent()) {
            testDetails(library, optBookList.get().get(0));
        } else {
            assert true : "Cannot find book with title: " + InitialContent.GREENLIGHTS;
        }

        optBookList = library.findBooksWithFullTittle(InitialContent.NOTHING_TO_LOSE);
        if (optBookList.isPresent()) {
            testDetails(library, optBookList.get().get(0));
        } else {
            assert true : "Cannot find book with title: " + InitialContent.NOTHING_TO_LOSE;
        }
    }

    private static void testDetails(LibraryManager library, BookModel bookModel) {
        Optional<String> opt = library.getBookDetails(bookModel.getId());
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Cannot find book with id: " + bookModel.getId());
        }
    }

    private static void testAllBookDetailsById(final LibraryManager library) {
        System.out.println();
        System.out.println("testAllBookDetailsById");
        Collection<BookModel> collection = library.getAllBooks();
        collection.stream().map(a -> library.getBookDetails(a.getId()).get()).forEach(System.out::println);
    }

    private static void testPresentBookStatus(LibraryManager library, String str) {
        System.out.println(str);
        library.presentBookStatus();
    }

    public static void testLibraryMethods(LibraryManager library) {
        System.out.println("Note: asserts should be enabled!!! Use the -enableassertions, or -ea in command-line Java VM options.");
        System.out.println();

        /*
           1. Library contains books to lend (at the beginning there are no books in the library)
           2. Book consist of title, year and author.
           4. Each book should have unique identifier (ID) across application.
           5. Should be possible to add new book to the library. ID should not be passed as argument. ID should be generated inside Library.

            All conditions above are presented in InitialContent method initBooksContent()
         */


        /*
         * 7. Should allow to list all books in the library (distinctly).
         * Returned information should contain information how many is available or lent.
         * You can use simply System.out.println
         */
        testPresentBookStatus(library, "Initial library content");

        /*
         * tests of auxiliary methods
         */
        testFindBooksWithFullTittle(library);
        testFindBooksWithBeginOfBookTittle(library);

        /*
         * 6. Should be possible to remove given book from the library (by ID)
         * (such action should be possible only if the book with such ID exists and it is not currently lent).
         */
        removeBookTest(library);

        /*
         * 9. Should allow to lent a book by ID ( should be forbidden if copy with given ID is already lent). Should allow to pass the name of the person who lend the book.
         */
        testLentBooks(library);

        /*
         * 8. Should allow to search book by title, author, year (also other combinations like title AND author).
         */
        testComplexBooksSearch(library);

        /*
         * 10. Should allow to see all book's details by ID (title, author, year, information if it is available or lent together with person name).
         */
        testSpecificBookDetails(library);
        testAllBookDetailsById(library);
        System.out.println();

        /*
         * 7. Should allow to list all books in the library (distinctly).
         * Returned information should contain information how many is available or lent.
         * You can use simply System.out.println
         */
        testPresentBookStatus(library, "Final library content");

        System.out.println();
        System.out.println("Test library methods passed.");
    }
}
