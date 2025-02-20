package com.example.ereztictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "users.db";
    private static final int DB_VERSION = 1;
    private static final String USERS_TABLE = "users";
    private static final String USER_USERNAME = "user_username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_ID = "user_id";

    public DBhelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USERS_TABLE +  " (" +  USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_USERNAME + " TEXT," + USER_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public User addNewUser(User u) {
        User u1 = getUser(u.getUsername(), u.getPassword());
        if (u1 != null) return u1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_USERNAME, u.getUsername());
        cv.put(USER_PASSWORD, u.getPassword());
        long insert = db.insert(USERS_TABLE, null, cv);
        u.setId(insert);
        db.close();
        return u;
    }

    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USER_ID + " = " + id, null);
        if (cursor.moveToFirst()) return new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
        return new User();
    }

    public User getUser(String un, String pw) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USER_USERNAME + " = ? AND " + USER_PASSWORD + " = ?", new String[]{un, pw});
        if (cursor.moveToFirst()) return new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
        return null;
    }

    public boolean removeUser(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("DELETE FROM " + USERS_TABLE + " WHERE " + USER_ID + " = " + id, null).moveToFirst();
    }
}
