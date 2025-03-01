package com.example.ereztictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "users.db";
    private static final int DB_VERSION = 1;
    private static final String USERS_TABLE = "users";
    private static final String USER_USERNAME = "user_username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_ID = "user_id";

    private static final String STATS_TABLE = "stats_table";
    private static final String STATS_USER_ID = "stats_user_id";
    private static final String STATS_WINS = "stats_wins";
    private static final String STATS_LOSSES = "stats_losses";
    private static final String STATS_DRAWS = "stats_draws";

    public DBhelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (" +  USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_USERNAME + " TEXT," + USER_PASSWORD + " TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + STATS_TABLE + " (" + STATS_USER_ID + " INTEGER, " + STATS_WINS + " INTEGER, " + STATS_LOSSES + " INTEGER, " + STATS_DRAWS + " INTEGER, FOREIGN KEY( " + STATS_USER_ID + " ) REFERENCES " + USERS_TABLE + "( " + USER_ID + " ))");
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
        cv = new ContentValues();
        cv.put(STATS_WINS,0);
        cv.put(STATS_DRAWS,0);
        cv.put(STATS_LOSSES,0);
        cv.put(STATS_USER_ID,insert);
        db.insert(STATS_TABLE, null, cv);
        db.close();
        return u;
    }

    public User getUser(long id) {
        User u = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USER_ID + " = " + id, null);
        if (cursor.moveToFirst()) u = new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        db.close();
        return u;
    }

    public User getUser(String un, String pw) {
        User u = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USER_USERNAME + " = ? AND " + USER_PASSWORD + " = ?", new String[]{un, pw});
        if (cursor.moveToFirst()) u = new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        db.close();
        return u;
    }

//    public boolean removeUser(long id) {
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = db.rawQuery("DELETE FROM " + USERS_TABLE + " WHERE " + USER_ID + " = " + id, null);
//        boolean b = cursor.moveToFirst();
//        db.close();
//        cursor.close();
//        return b;
//    }

    public boolean updateUser(User u, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_PASSWORD, newPassword);
        int num = db.update(USERS_TABLE, cv, USER_ID + " = ?", new String[]{ String.valueOf(u.getId()) } );
        db.close();
        return num==1;
    }

    public void updateStats(long id, String res) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        int score = getStat(id, res) + 1;
        cv.put(res, score);
        db.update(STATS_TABLE , cv, STATS_USER_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getStat(long id, String res) {
        SQLiteDatabase db = getReadableDatabase();
        int r = -1;
        Cursor c = db.rawQuery("SELECT * FROM " + STATS_TABLE + " WHERE " + STATS_USER_ID + " = " + id, null);
        if ( c.moveToFirst() ) {
            r = c.getInt(res.equals(STATS_WINS)?1:res.equals(STATS_LOSSES)?2:3);
        }
        c.close();
        return r;
    }
}