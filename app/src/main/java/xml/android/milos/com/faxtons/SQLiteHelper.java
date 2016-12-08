package xml.android.milos.com.faxtons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Milo on 27/11/2016.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    //Declared variables for property database
    public static final String DATABASE_NAME = "Property.db";
    public static final String TABLE_NAME = "Property_table";
    public static final String COLUMN_PROPERTY_ID = "PROPERTY_ID";
    public static final String COLUMN_LOCATION = "LOCATION";
    public static final String COLUMN_PRICE = "PRICE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_CONTACT = "CONTACT";
    public static final String COLUMN_LATITUDE = "LATITUDE";
    public static final String COLUMN_LONGITUDE = "LONGITUDE";



    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //SQL statement to create table in the database
        db.execSQL("create table " + TABLE_NAME + " (PROPERTY_ID INTEGER PRIMARY KEY AUTOINCREMENT, LOCATION TEXT, PRICE TEXT, DATE TEXT, IMAGE BLOB, DESCRIPTION TEXT, CONTACT TEXT, LATITUDE TEXT, LONGITUDE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Keeps updating table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    //Insert values into table from Registration  class
    public boolean insertData(String location, String price, String date, byte[] image, String description, String contact, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COLUMN_LOCATION, location);
        contentValue.put(COLUMN_PRICE, price);
        contentValue.put(COLUMN_DATE, date);
        contentValue.put(COLUMN_IMAGE, image);
        contentValue.put(COLUMN_DESCRIPTION, description);
        contentValue.put(COLUMN_CONTACT, contact);
        contentValue.put(COLUMN_LATITUDE, latitude);
        contentValue.put(COLUMN_LONGITUDE, longitude);
        long result = db.insert(TABLE_NAME, null, contentValue);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Retrieve all values from Property table
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    public void deleteData (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME + " where property_id =" + id );
    }
}
