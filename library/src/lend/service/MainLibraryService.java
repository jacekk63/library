package lend.service;

import database.InitialContent;
import database.LibraryManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Library requirements
 * 1. Library contains books to lend (at the beginning there are no books in the library)
 * 2. Book consist of title, year and author.
 * 4. Each book should have unique identifier (ID) across application.
 * 5. Should be possible to add new book to the library. ID should not be passed as argument. ID should be
 * generated inside Library.
 * 6. Should be possible to remove given book from the library (by ID) (such action should be possible only
 * if the book with such ID exists and it is not currently lent).
 * 7. Should allow to list all books in the library (distinctly). Returned information should contain
 * information how many is available or lent. You can use simply System.out.println
 * 8. Should allow to search book by title, author, year (also other combinations like title AND author).
 * 9. Should allow to lent a book by ID ( should be forbidden if copy with given ID is already lent). Should
 * allow to pass the name of the person who lend the book.
 * 10. Should allow to see all book's details by ID (title, author, year, information if it is available or lent
 * together with person name).
 */
public class MainLibraryService {

    /**
     * main method
     * command line arguments:
     * skipInitLibraryContent - simulated database is not initialized
     * storeLibraryContent - store library content to files (serialization)
     *
     * to enable assertions for test add -ea parameter for Java VM
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        Map<String, String> argsMap = parseArgs(args);

        final LibraryManager library = new LibraryManager(argsMap);
        library.startLibrary();

        if (argsMap.get("skipInitLibraryContent") == null) {
            library.initLibraryContent();
        }

        if (argsMap.get("storeLibraryContent") != null) {
            library.storeContent();
        }

        library.displayContent();
        InitialContent.testLibraryMethods(library);
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> argsMap = new HashMap<>();
        for (String arg : args) {
            String second = "";
            String[] param = parseOption(arg);
            if (param.length == 2) {
                second = param[1];
            }
            argsMap.put(param[0], second);
        }
        return argsMap;
    }

    private static String[] parseOption(String arg) {
        return arg.split("=");
    }
}
