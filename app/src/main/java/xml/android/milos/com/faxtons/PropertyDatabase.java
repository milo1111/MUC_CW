package xml.android.milos.com.faxtons;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PropertyDatabase extends AppCompatActivity {

    //Declared buttons
    Button buttonNewFeed;
    Button buttonDatabaseItem;
    ListView list;
    //Declared properties variables
    private Bitmap[] pic;
    private String[] location, price, date, description, contact, latitude, longitude;
    //Declared variables for an Array list
    private ArrayList<Bitmap> images = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> contacts = new ArrayList<>();
    private ArrayList<String> latitudes = new ArrayList<>();
    private ArrayList<String> longitudes = new ArrayList<>();
    private ArrayList<Integer> dbPropertyId = new ArrayList<>();

    //Create instance name myDBs
    SQLiteHelper myDbs;
    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_database);
        myDbs = new SQLiteHelper(this);
        myDb = new DatabaseHelper(this);

        //Get username from users table
        Cursor c = myDb.getLoggedUser();
        if (c.getColumnCount() == 0) {
            return;
        }
        c.moveToFirst();
        //Retrieve user`s  data from database
        String username = c.getString(c.getColumnIndex("USERNAME"));

        //Show toolbar on the main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Logged: "+ username + "      Database items");
        toolbar.setBackgroundColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //assigned buttons to variables
        buttonNewFeed = (Button) findViewById(R.id.buttonNewFeed);
        buttonDatabaseItem = (Button) findViewById(R.id.buttonDatabaseItems);
        //Find the ListView by id and assign to variable
        list = (ListView) findViewById(R.id.listViewProperty);

        //Set night mode for list view and toolbar when user select night mode in preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            list.setBackgroundColor(Color.LTGRAY);
        }
        //Set button to retrieve information about new feeds
        buttonNewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //Set button to retrieve information saved in the database
        buttonDatabaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PropertyDatabase.class);
                startActivity(intent);
            }
        });

        //Get all data from property table
        Cursor cursor = myDbs.getAllData();
        if (cursor.getColumnCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {

            //Retrieve data from property database
            dbPropertyId.add(cursor.getInt(cursor.getColumnIndex("PROPERTY_ID")));
            locations.add(cursor.getString(cursor.getColumnIndex("LOCATION")));
            prices.add(cursor.getString(cursor.getColumnIndex("PRICE")));
            dates.add(cursor.getString(cursor.getColumnIndex("DATE")));
            byte[] img = (cursor.getBlob(cursor.getColumnIndex("IMAGE")));
            images.add(BitmapFactory.decodeByteArray(img, 0, img.length));
            descriptions.add(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
            contacts.add(cursor.getString(cursor.getColumnIndex("CONTACT")));
            latitudes.add(cursor.getString(cursor.getColumnIndex("LATITUDE")));
            longitudes.add(cursor.getString(cursor.getColumnIndex("LONGITUDE")));
        }

        //Set picture in arraylist
        pic = new Bitmap[images.size()];
        pic = images.toArray(pic);

        //Set location in arraylist
        location = new String[locations.size()];
        location = locations.toArray(location);

        //Set price in arraylist
        price = new String[prices.size()];
        price = prices.toArray(price);

        //Set date in arraylist
        date = new String[dates.size()];
        date = dates.toArray(date);

        //Set description in arraylist
        description = new String[descriptions.size()];
        description = descriptions.toArray(description);

        //Set contact in arraylist
        contact = new String[contacts.size()];
        contact = contacts.toArray(contact);

        //Set latitude in arraylist
        latitude = new String[latitudes.size()];
        latitude = latitudes.toArray(latitude);

        //Set longitude in arraylist
        longitude = new String[longitudes.size()];
        longitude = longitudes.toArray(longitude);

        //Show information in List View
        CustomListAdapter adapter = new CustomListAdapter(PropertyDatabase.this, location, pic, price, date);
        list.setAdapter(adapter);

        //Count number of properties in the list
        int numberOfProperties = list.getAdapter().getCount();
        Toast.makeText(getApplicationContext(), "There are " + numberOfProperties + " properties at this time", Toast.LENGTH_LONG).show();

        //Switch to PropertyDetail class and send data about properties
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PropertyDatabase.this, PropertyDetailRss.class);
                i.putExtra("propertyID", dbPropertyId.get(position));
                i.putExtra("Location", location[position]);
                i.putExtra("Picture", pic[position]);
                i.putExtra("Price", price[position]);
                i.putExtra("Date", date[position]);
                i.putExtra("Description", description[position]);
                i.putExtra("Contact", contact[position]);
                i.putExtra("Latitude", latitude[position]);
                i.putExtra("Longitude", longitude[position]);
                i.putExtra("InvisibleButton", "invisibleSaveButton");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chart) {
            //Go to Property chart class
            Intent is = new Intent(PropertyDatabase.this,PropertyChart.class);
            startActivity(is);
            return true;
        }
        if (id == R.id.action_profile) {

            //Go to UserProfile class
            Intent i = new Intent(PropertyDatabase.this,UserProfile.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_preferences) {
            //Switch to class UserPreferences
            Intent i = new Intent(PropertyDatabase.this,UserPreferences.class);
            startActivity(i);
            return true;

        }
        if (id == R.id.action_about) {

            //Switch to class About Faxtons
            Intent i = new Intent(PropertyDatabase.this,AboutFaxtons.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_logout) {

            //Logout user
            myDb.logOutUser();
            //Navigate user to login screen
            Intent i = new Intent(PropertyDatabase.this,LoginRss.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}