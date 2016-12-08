package xml.android.milos.com.faxtons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.name;

/**
 * Created by Milo on 23/11/2016.
 * This class is used to create database for users
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COLUMN_1 = "USER_ID";
    public static final String COLUMN_2 = "USERNAME";
    public static final String COLUMN_3 = "PASSWORD";
    public static final String COLUMN_4 = "LOGGED_IN";
    public String loggUserId;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    //Creating Users table with appropriate columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT, PASSWORD TEXT,LOGGED_IN TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Insert values into table from Registration  class
    public boolean insertData(String username, String password, String loggedIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COLUMN_2, username);
        contentValue.put(COLUMN_3, password);
        contentValue.put(COLUMN_4, loggedIn);
        long result = db.insert(TABLE_NAME, null, contentValue);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getLoggedUser1() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME +" where USER_ID = " + loggUserId, null);
        return res;
    }

    // Retrieve all values from Users table
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    //Update users username and password from registration class
    public void updateData(String username, String password, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_NAME + " set USERNAME =? , PASSWORD =? where USER_ID =" + id, new String[]{username,password});

    }

    //Make user logged in after app is restarted
    public void logUserIn(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_NAME + " set LOGGED_IN = 1 where USER_ID =" + id);
    }

    //Log out user
    public void logOutUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_NAME + " set LOGGED_IN = 0 where USER_ID");
    }

    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"USER_ID = ?", new String[] {id});
    }

    //Find user which is logged in and continue without registration
    public Cursor getLoggedUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor user = db.rawQuery("select * from "+ TABLE_NAME + " where LOGGED_IN = 1",null);
        return user;
    }

    //Find user which is logged in using integer value
    public boolean checkIfLogged(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor users = db.rawQuery("select * from "+ TABLE_NAME ,null);
        while (users.moveToNext()){
            String loggedInt = users.getString(users.getColumnIndex("LOGGED_IN"));
            if(Integer.valueOf(loggedInt)==1){
                return true;
            }
        }
        return false;
    }
}
