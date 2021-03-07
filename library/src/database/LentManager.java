/**
 * LentManager
 * @author: Jacek Kulesz
 * @date: 2021.03.07
 */
package database;

import models.BookModel;
import models.LentBooksModel;
import models.UserModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class LentManager extends BaseManager<LentBooksModel> {

    private final static String LENDS_FILE_PATH = "d:\\tmp\\lends.txt";

    /**
     * keeps all users in library
     * key: userId; value: LentBooksModel
     */
    private Map<Integer, LentBooksModel> allLents;

    /**
     * books manager
     */
    private final BooksManager booksManager;
    /**
     * users manager
     */
    private final UsersManager usersManager;

    /**
     * constructor
     * @param argsMap command-line arguments map, just in case any of managers will process its arguments
     * key: argument value, value: argument parameters after '=' character (can be empty)
     * @param booksManager booksManager will be necessary to execute operations on books set
     * @param usersManager usersManager will be necessary to execute operations on users set
     */
    protected LentManager(Map<String, String> argsMap, BooksManager booksManager, UsersManager usersManager) {
        super(argsMap);
        this.booksManager = booksManager;
        this.usersManager = usersManager;
        allLents = new HashMap<>();
        initDB(LENDS_FILE_PATH);
        initAutoCounter(allLents.size());
    }

    /**
     * method lent specified book to specified user
     * @param date time of book lent
     * @param user user to whom book will be lent
     * @param book book to lent
     * @return Optional<LentBooksModel>
     */
    protected synchronized Optional<LentBooksModel> addLentBooksModel(Date date, UserModel user, BookModel book) {
        //check if user exists
        if (this.usersManager.getUser(user.getId()) == null) {
            return Optional.empty();
        }

        //check if book exists
        if (this.booksManager.getBook(book.getId()) == null) {
            return Optional.empty();
        }

        LentBooksModel lentBooksModel = allLents.get(user.getId());
        if (lentBooksModel == null) {
            lentBooksModel = new LentBooksModel(user.getId());
            lentBooksModel.setId(user.getId());
        }
        lentBooksModel.addLentBook(date, book.getId());

        this.allLents.put(user.getId(), lentBooksModel);
        book.lentBook(user.getId());
        return Optional.of(lentBooksModel);
    }

    /**
     * 9. Should allow to lent a book by ID (should be forbidden if copy with given ID is already lent).
     *    Should allow to pass the name of the person who lend the book.
     * Method lent specified book to specified user.
     * @param userId user id
     * @param bookId book id
     * @return Optional<LentBooksModel> lent book model
     */
    protected synchronized Optional<LentBooksModel> lentBook(int userId, int bookId) {
        //check if user exists
        UserModel user = usersManager.getUser(userId);
        if (user == null) {
            return Optional.empty();
        }

        //check if book exists
        BookModel bm = booksManager.getBook(bookId);
        if (bm == null) {
            return Optional.empty();
        }

        if (bm.isBookLent()) {
            LentBooksModel lentBooksModel = new LentBooksModel();
            lentBooksModel.setTmpUserName(usersManager.getUserFullName(bm.getLentUserId()));
            return Optional.of(lentBooksModel);
        }

        bm.lentBook(userId);
        return addLentBooksModel(new Date(), user, bm);
    }

    /**
     * get empty lent book model
     * @return empty lent book model
     */
    @Override
    protected LentBooksModel getBaseModel() {
        return new LentBooksModel();
    }

    /**
     * displays lent status
     */
    protected void displayContent() {
        System.out.println();
        System.out.println("Number of users who lent book: " + allLents.size());
        System.out.println();
        for (LentBooksModel lbm : allLents.values()) {
            String fullUserName = this.usersManager.getUserFullName(lbm.getId());
            String titles = this.booksManager.getBooksShortDescription(lbm.getLentBooks());
            System.out.println(fullUserName +
                    "\n"+
                    lbm.toString() +
                    titles+
                    "\n"
            );
        }
        System.out.println();
    }

    /*
    Serialization methods ====================================================================================
     */
    protected Map<Integer, LentBooksModel> readContent() {
        return readContent(LENDS_FILE_PATH);
    }

    @Override
    protected Map<Integer, LentBooksModel> readContent(String file) {
        Map<Integer, LentBooksModel> map = super.readContent(file);
        allLents = map;
        return map;
    }

    protected void storeContent(String file) {
        super.storeContent(allLents, file);
    }

    protected void storeContent() {
        super.storeContent(allLents, LENDS_FILE_PATH);
    }

    @Override
    protected void deserialize(LentBooksModel model, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        model.deserialize(inputStream);
    }

    @Override
    protected void serialize(LentBooksModel baseModel, ObjectOutputStream outputStream) throws IOException {
        baseModel.serialize(outputStream);
    }

    /*
    getter
     */
    @Override
    protected Integer getId(LentBooksModel model) {
        return model.getId();
    }
}
