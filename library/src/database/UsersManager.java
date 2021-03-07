/**
 * UsersManager
 * @author: Jacek Kulesz
 * @date: 2021.03.07
 */
package database;

import models.UserModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class UsersManager extends BaseManager<UserModel> {

    private final static String USERS_FILE_PATH = "d:\\tmp\\users.txt";

    /**
     * keeps all users in library
     * key: userId, val: UserModel
     */
    private Map<Integer, UserModel> allUsersMap;

    /**
     * keeps all users in library
     * key: full_user_name, value: UserModel
     * to simplicity pinpoint that in database will stored users with unique full name [name + surname]
     */
    private final Map<String, UserModel> allFullNameMap;

    /**
     * constructor
     * @param argsMap command-line arguments map, just in case any of managers will process its arguments
     * key: argument value, value: argument parameters after '=' character (can be empty)
     */
    protected UsersManager(Map<String, String> argsMap) {
        super(argsMap);
        allUsersMap = new HashMap<>();
        allFullNameMap = new HashMap<>();
        initDB(USERS_FILE_PATH);
        initAutoCounter(allUsersMap.size());
    }

    /**
     * method add user to database (internal caches)
     * @param registration registration date
     * @param name user name
     * @param surname user surname
     * @return UserModel
     */
    protected synchronized UserModel registerUser(Date registration, String name, String surname) {
        UserModel userModel = new UserModel(registration, name, surname);
        userModel.setId(getAndIncrement());
        allUsersMap.put(userModel.getId(), userModel);
        allFullNameMap.put(UserModel.getFullName(name, surname), userModel);
        return userModel;
    }

    /**
     * get user model
     * @param userId user id
     * @return UserModel
     * note: if user name is not in cache can return null value; could be replaced by Optional<UserName> like in BooksManager
     */
    protected synchronized UserModel getUser(int userId) {
        return allUsersMap.get(userId);
    }

    /**
     * get user name
     * @param name user name
     * @param surname user surname
     * @return UserModel,
     * note: if user name is not in cache can return null value; could be replaced by Optional<UserName> like in BooksManager
     */
    protected UserModel getUser(String name, String surname) {
        return allFullNameMap.get(UserModel.getFullName(name, surname));
    }

    /**
     * get all defined users
     * @return user cache
     */
    protected Map<Integer, UserModel> getAllUsers() {
        return allUsersMap;
    }

    /**
     * display all users
     */
    protected void displayContent() {
        System.out.println();
        System.out.println("Number of registered users: " + allUsersMap.size());
        System.out.println();
        for (UserModel um : allUsersMap.values()) {
            System.out.println(um.toString());
        }
        System.out.println();
    }

    /**
     * get full user name
     * @param userId user id
     * @return full user name
     * if there is no user wit specified id in database, method returns empty string (not null)
     */
    protected String getUserFullName(int userId) {
        UserModel um = allUsersMap.get(userId);
        return (um != null) ? UserModel.getFullName(um.getName(), um.getsurname()) : "";
    }

    /*
     * methods related to serialization
     */
    protected Map<Integer, UserModel> readContent() {
        return readContent(USERS_FILE_PATH);
    }

    @Override
    protected Map<Integer, UserModel> readContent(String file) {
        Map<Integer, UserModel> map = super.readContent(file);
        allUsersMap = map;
        return map;
    }

    protected void storeContent() {
        storeContent(USERS_FILE_PATH);
    }

    protected void storeContent(String file) {
        super.storeContent(allUsersMap, file);
    }

    /*
     * setters and getters
     */

    @Override
    protected UserModel getBaseModel() {
        return new UserModel();
    }

    @Override
    protected void deserialize(UserModel model, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        model.deserialize(inputStream);
    }

    @Override
    protected void serialize(UserModel baseModel, ObjectOutputStream outputStream) throws IOException {
        baseModel.serialize(outputStream);
    }

    @Override
    protected Integer getId(UserModel model) {
        return model.getId();
    }
}
