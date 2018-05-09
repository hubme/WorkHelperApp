package com.king.app.workhelper.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author VanceKing
 * @since 2018/5/9.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "book_provider.db";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";

    private static final int DB_VERSION = 1;

    private final String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS "
            + BOOK_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT)";

    private final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + USER_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT,"
            + "sex INT)";


    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
