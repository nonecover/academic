package com.jakomulski.datacollector.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jakomulski.datacollector.AndroidLogger;
import com.jakomulski.datacollector.models.Photo;
import com.jakomulski.datacollector.models.User;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DbDataSource {
    private Logger logger = AndroidLogger.getLogger(this.getClass());

    interface ActionReference<T> {
        T run();
    }

    private SQLiteDatabase database;
    private Cursor cursor;
    private MySQLiteHelper dbHelper;
    private String[] allUserColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_LASTNAME,
            MySQLiteHelper.COLUMN_BIRTH_DATE};

    private String[] allPhotosColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_URI,
            MySQLiteHelper.COLUMN_USER_ID};

    public DbDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public User createUser(String name, String lastname, String birthDate) {
        return maintainDBAction(() -> {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_NAME, name);
            values.put(MySQLiteHelper.COLUMN_LASTNAME, lastname);
            values.put(MySQLiteHelper.COLUMN_BIRTH_DATE, birthDate);
            long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null,
                    values);
            Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS,
                    allUserColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            User newUser = cursorToUser(cursor);
            logger.info("create "+ newUser.toLongString());
            return newUser;
        });
    }

    public void deleteUser(User user) {
        logger.info("delete "+ user.toLongString());
        maintainDBAction(() -> {
            long id = user.getId();
            System.out.println("User deleted with id: " + id);
            database.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_ID
                    + " = " + id, null);
        });
    }

    public void deletePhoto(Photo photo) {
        logger.info("delete "+ photo.toString());
        maintainDBAction(() -> {
            long id = photo.getId();
            System.out.println("Photo deleted with id: " + id);
            database.delete(MySQLiteHelper.TABLE_PHOTOS, MySQLiteHelper.COLUMN_ID
                    + " = " + id, null);
        });
    }

    public List<User> getAllUsers() {
        return maintainDBAction(() -> {
            List<User> users = new ArrayList<User>();
            Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS,
                    allUserColumns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                User user = cursorToUser(cursor);
                users.add(user);
                cursor.moveToNext();
            }
            return users;
        });
    }

    public List<Photo> getAllUserPhotos(long userId) {
        return maintainDBAction(() -> {
            List<Photo> photos = new ArrayList<Photo>();
            String whereClause = MySQLiteHelper.COLUMN_USER_ID + " = " + userId;
            Cursor cursor = database.query(MySQLiteHelper.TABLE_PHOTOS,
                    allPhotosColumns, whereClause, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Photo photo = cursorToPhoto(cursor);
                photos.add(photo);
                cursor.moveToNext();
            }
            return photos;
        });
    }

    public Photo createPhoto(long userId, String uri) {
        return maintainDBAction(() -> {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_USER_ID, userId);
            values.put(MySQLiteHelper.COLUMN_URI, uri);
            long insertId = database.insert(MySQLiteHelper.TABLE_PHOTOS, null,
                    values);
            Cursor cursor = database.query(MySQLiteHelper.TABLE_PHOTOS,
                    allPhotosColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Photo newPhoto = cursorToPhoto(cursor);
            logger.info("create "+ newPhoto.toString());
            return newPhoto;
        });
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        user.setLastname(cursor.getString(2));
        user.setBirthDate(cursor.getString(3));
        return user;
    }

    private Photo cursorToPhoto(Cursor cursor) {
        Photo photo = new Photo();
        photo.setId(cursor.getLong(0));
        photo.setUri(cursor.getString(1));
        photo.setUser_id(cursor.getLong(2));
        return photo;
    }

    private void maintainDBAction(Runnable action) {
        maintainDBAction(() -> {
            action.run();
            return null;
        });
    }

    private <T> T maintainDBAction(ActionReference<T> action) {
        database = dbHelper.getWritableDatabase();
        T result = action.run();
        dbHelper.close();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}
