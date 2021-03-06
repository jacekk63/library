/**
 * LentBooksModel
 * @author: Jacek Kulesz
 * @date: 2021.03.07
 */
package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LentBooksModel extends BaseModel implements Serializable {

    //id will be generated by database in BaseModel class
    // id will contain user identifier

    /**
     * list of books lent by user
     */
    private List<Integer> lentBooks = new ArrayList<>();

    private transient String tmpUserName;

    public LentBooksModel(int userId) {
        setId(userId);
        tmpUserName = "";
     }

    public LentBooksModel() {
    }

    @Override
    public void serialize(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(getId());
        outputStream.writeObject(lentBooks.size());
        for (Integer bookId : lentBooks) {
            outputStream.writeObject(bookId);
        }
    }

    @Override
    public void deserialize(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        Integer number = (Integer) inputStream.readObject();
        setId(number);

        number = (Integer) inputStream.readObject();
        lentBooks = new ArrayList<>();
        for (int i=0; i<number; i++) {
            number = (Integer) inputStream.readObject();
            lentBooks.add(number);
        }
    }

    public List<Integer> getLentBooks() {
        return lentBooks;
    }

    public void setLentBooks(List<Integer> lentBooks) {
        this.lentBooks = lentBooks;
    }

    public void addLentBook(Date date, int bookId) {
        this.setDate(date);
        this.lentBooks.add(bookId);
    }

    public String getTmpUserName() {
        return tmpUserName;
    }

    public void setTmpUserName(String tmpUserName) {
        this.tmpUserName = tmpUserName;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nlent date: " + getDate() +
                "\nlent books Ids: " +
                lentBooks.toString() +
                "\n";
    }
}
