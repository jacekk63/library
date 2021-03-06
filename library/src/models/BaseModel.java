package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public abstract class BaseModel {
    private int id;
    private Date date;

    public abstract void serialize(ObjectOutputStream outputStream) throws IOException;
    public abstract void deserialize(ObjectInputStream inputStream) throws IOException, ClassNotFoundException;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return getClass().getName() +
                "\nid: " + id +
                "\ndate: " + date
                ;
    }
}
