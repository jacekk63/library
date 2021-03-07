/**
 * BaseManager
 * @author: Jacek Kulesz
 * @date: 2020.03.07
 */
package database;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * base method for each manager (Book, Users and Lent)
 * @param <T> BaseModel
 */
public abstract class BaseManager<T> {

    /**
     * ids generator
     */
    private AtomicInteger atomicInteger;

    /**
     * command-line arguments map, just in case any of managers will process its arguments
     * key: argument value, value: argument parameters after '=' character (can be empty)
     */
    private final Map<String, String> argsMap;

    /**
     * auxiliary methods implemented in managers to provide suitable functionality
     * @return BaseModel
     */
    protected abstract T getBaseModel();
    protected abstract void deserialize(T model, ObjectInputStream inputStream) throws IOException, ClassNotFoundException;
    protected abstract void serialize(T baseModel, ObjectOutputStream outputStream) throws IOException;
    protected abstract Integer getId(T model);

    /**
     * constructor
     * @param argsMap command-line arguments map, just in case any of managers will process its arguments
     * key: argument value, value: argument parameters after '=' character (can be empty)
     */
    public BaseManager(Map<String, String> argsMap) {
        this.argsMap = argsMap;
    }

    /**
     * auto counter initialization
     * @param size initial counter number (will be used this value +1)
     */
    protected void initAutoCounter(int size) {
        atomicInteger = new AtomicInteger(size + 1);
    }

    /**
     * get id and increment counter
     * @return id of element
     */
    protected int getAndIncrement() {
        return atomicInteger.getAndIncrement();
    }

    /**
     * deserialization
     * @param file file with serialize data
     * @return map of deserialized objects
     */
    protected Map<Integer, T> readContent(String file) {
        Map<Integer, T> allElements = new HashMap<>();
        if (makeSerialization(argsMap)) {
            //make library databases serialization
            File fileAccess = new File(file);
            if (fileAccess.exists()) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                    Integer number = (Integer) inputStream.readObject();
                    for (int i = 0; i < number; i++) {
                        T model = getBaseModel();
                        deserialize(model, inputStream);
                        allElements.put(getId(model), model);
                    }
                } catch (FileNotFoundException e) {
                    //TODO - for purpose this project it will just print exception stacktrace
                    e.printStackTrace();
                } catch (IOException e) {
                    //TODO - for purpose this project it will just print exception stacktrace
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    //TODO - for purpose this project it will just print exception stacktrace
                    e.printStackTrace();
                }
            }
        }
        return allElements;
    }

    /**
     * serialization
     * @param map map of objects to serialize
     * @param file file to save serialized objects
     */
    protected void storeContent(Map<Integer, T> map, String file) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(map.size());
            for (T baseModel : map.values()) {
                serialize(baseModel, outputStream);
            }
        } catch (FileNotFoundException e) {
            //TODO - for purpose this project it will just print exception stacktrace
            e.printStackTrace();
        } catch (IOException e) {
            //TODO - for purpose this project it will just print exception stacktrace
            e.printStackTrace();
        }
    }

    /**
     *  For purpose of this task program will not store library databases into file by default.
     *
     *  When doNotSerialize = true; then each time when program will be started all library databases will be recreated.
     *  This behavior is default.
     *
     *  When doNotSerialize = false; then program will read all library databases from stored files.
     *  If is need to change this functionality, then add command-line "serialize" argument
     *  or simply return true value from isSerialize() method.
     */
    public static boolean makeSerialization(Map<String, String> argsMap) {
        return argsMap.get("serialize") != null;
    }

    /**
     * initialization database
     * @param file file with serialized objects
     */
    public void initDB(String file) {
        readContent(file);
    }
}
