package xml.android.milos.com.faxtons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Milo on 30/11/2016.
 */

public class PrepopulatedDatabase extends SQLiteOpenHelper {

    //Declared variables for prepopulated database
    public static final String DATABASE_FAXTONS_NAME = "Faxtons.db";
    public static final String TABLE_FAXTONS_NAME = "FAXTONS_TABLE";
    public static final String COLUMN_FAXTONS_ID = "FAXTONS_ID";
    public static final String COLUMN_FAXTONS_INFO = "FAXTONS_INFO";
    public static final String COLUMN_FAXTONS_LOCATION = "FAXTONS_LOCATION";
    public static final String COLUMN_FAXTONS_CONTACT = "FAXTONS_CONTACT";

    public PrepopulatedDatabase(Context context) {
        super(context, DATABASE_FAXTONS_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL statement to create table in the database
        db.execSQL("create table " + TABLE_FAXTONS_NAME + " (FAXTONS_ID INTEGER PRIMARY KEY AUTOINCREMENT, FAXTONS_INFO TEXT, FAXTONS_LOCATION TEXT, FAXTONS_CONTACT TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Keeps updating table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAXTONS_NAME);
        onCreate(db);
    }

    //Insert values into Faxton table
    public boolean insertData(String fInfo, String fLocation, String fContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COLUMN_FAXTONS_INFO, fInfo);
        contentValue.put(COLUMN_FAXTONS_LOCATION, fLocation);
        contentValue.put(COLUMN_FAXTONS_CONTACT, fContact);
        long result = db.insert(TABLE_FAXTONS_NAME, null, contentValue);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Retrieve all values from Faxtons table
    public Cursor getFaxtonData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu = db.rawQuery("select * from " + TABLE_FAXTONS_NAME, null);
        return cu;
    }
}
