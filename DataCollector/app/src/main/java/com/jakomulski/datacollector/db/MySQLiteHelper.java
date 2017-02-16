package com.jakomulski.datacollector.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TEXT_TYPE = " text not null";


    public static final String TABLE_USERS = "users";
    public static final String TABLE_PHOTOS = "photos";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_BIRTH_DATE = "birth_date";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_USER_ID = "user_id";

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USERS_TABLE_CREATE = "create table "
            + TABLE_USERS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " +
            COLUMN_NAME + TEXT_TYPE + ", " +
            COLUMN_LASTNAME + TEXT_TYPE + ", " +
            COLUMN_BIRTH_DATE + TEXT_TYPE +
            ");";

    private static final String PHOTOS_TABLE_CREATE = "create table "
            + TABLE_PHOTOS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " +
            COLUMN_URI + TEXT_TYPE + ", " +
            COLUMN_USER_ID + " integer, " +
            " FOREIGN KEY ("+COLUMN_USER_ID+") REFERENCES "+TABLE_USERS+"("+COLUMN_ID+"));";
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(USERS_TABLE_CREATE);
        database.execSQL(PHOTOS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }
}
