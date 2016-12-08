package xml.android.milos.com.faxtons;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The main class is used to show information about properties from RSS feed
 */
public class MainActivity extends AppCompatActivity {

    // Insert image URL
    //private String link = "http://www.foxtons.co.uk/feeds";
    private String link = "https://www.foxtons.co.uk/feeds?price_from=1000000&price_to=&location_ids=392&search_type=SS&result_view=rss";
    ProgressDialog dialog;
    Toolbar toolbar;
    ListView list;

    //Declared buttons for new feeds and database
    Button btnNewFeed;
    Button btnDatabaseItem;
    ArrayList<ItemRss> arrayList;

    //Declared variables for properties
    private Bitmap[] pic;
    private String[] location, price, date, description, contact, latitude, longitude;

    //Declared variables for Array list
    private ArrayList<Bitmap> images = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> contacts = new ArrayList<>();
    private ArrayList<String> latitudes = new ArrayList<>();
    private ArrayList<String> longitudes = new ArrayList<>();

    //Create instance name myDB
    DatabaseHelper myDb;
    //Create instance name myDBs
    SQLiteHelper myDbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        myDbs = new SQLiteHelper(this);

        //If user is not logged in, redirect to Login screen
        if(!myDb.checkIfLogged()){
            Intent i = new Intent(MainActivity.this,LoginRss.class);
            startActivity(i);
        }

        //Get username from users table
        Cursor c = myDb.getLoggedUser();
        if (c.getColumnCount() == 0) {
            return;
        }
        c.moveToFirst();
        //Retrieve user`s  data from database
        String username = c.getString(c.getColumnIndex("USERNAME"));

            //Show toolbar on the main screen
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Logged: "+ username + "        New Feeds");
            toolbar.setBackgroundColor(Color.BLACK);
            setSupportActionBar(toolbar);


        //assigned buttons to variables
        btnNewFeed = (Button) findViewById(R.id.btnNewFeed);
        btnDatabaseItem = (Button) findViewById(R.id.btnDatabaseItems);
        //Find the ListView by id and assign to variable
        list = (ListView) findViewById(R.id.listView);

        //Set night mode for list view and toolbar when user select night mode in preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            list.setBackgroundColor(Color.LTGRAY);
        }

        //Set button to retrieve information about new feeds
        btnNewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //Set button to retrieve information saved in the database
        btnDatabaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PropertyDatabase.class);
                startActivity(intent);
            }
        });


        // Execute DownloadXML AsyncTask
        new DownloadXML().execute(link);
    }

    // DownloadXML AsyncTask
    private class DownloadXML extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            dialog = new ProgressDialog(MainActivity.this);
            // Set progressbar title
            dialog.setTitle("Please wait");
            // Set progressbar message
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            // Show progressbar
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... Url) {

            RssParser rss = new RssParser();
            LinkedList<ItemRss> list = rss.getXML(link);

            arrayList = new ArrayList<ItemRss>();

            for (ItemRss i : list) {

                //Get location from XML
                String parseLocation = new String(i.getTitle());
                String[] slocation = parseLocation.split("  ");
                String location = (slocation[1]);
                locations.add(location);

                //Get price from XML
                String parsePrice = new String(i.getTitle());
                String[] sprice = parsePrice.split(" ");
                String price = (sprice[0]);
                prices.add(price);

                //Get date from XML
                String parseDate = new String(i.getDate());
                String newDate = parseDate.substring(0, 16);
                dates.add(newDate);

                //Get description from XML
                String parseDescription = new String(i.getDescription());
                int firstPosition = i.getDescription().indexOf("/a>");
                int lastPosition = i.getDescription().indexOf(" Contact");
                String newDescription = parseDescription.substring(firstPosition, lastPosition);
                newDescription = newDescription.replace("/a>", "");
                descriptions.add(newDescription);

                //Get image from XML
                String parseImage = new String(i.getDescription());
                int firstPos = i.getDescription().indexOf("<img src=\"");
                int lastPos = i.getDescription().indexOf("\" width=\"");
                String img = parseImage.substring(firstPos, lastPos);
                img = img.replace("<img src=\"", "");

                try {
                    URL myUrl = new URL(img);
                    Bitmap image = BitmapFactory.decodeStream(myUrl.openConnection().getInputStream());
                    images.add(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Get contact details from XML
                String parseContact = new String(i.getDescription());
                int firstP = i.getDescription().indexOf("html\">");
                String contact = parseContact.substring(firstP);
                contact = contact.replace("html\">", "");
                contact = contact.substring(0, 26) + contact.substring(30);
                contacts.add(contact);

                //Get geoPoints seperate from XML
                String parseGeoPoint = new String(i.getGeoPoint());
                String geoPointA = parseGeoPoint.substring(0, 9);
                String geoPointB = parseGeoPoint.substring(9);
                latitudes.add(geoPointA);
                longitudes.add(geoPointB);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

                // Close progressbar
                dialog.dismiss();

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
                CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, location, pic, price, date);
                list.setAdapter(adapter);

                //Count number of properties in the list
                int numberOfProperties = list.getAdapter().getCount();
                Toast.makeText(getApplicationContext(), "There are " + numberOfProperties + " properties at this time", Toast.LENGTH_LONG).show();
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, PropertyDetailRss.class);
                        i.putExtra("Location", location[position]);
                        i.putExtra("Picture", pic[position]);
                        i.putExtra("Price", price[position]);
                        i.putExtra("Date", date[position]);
                        i.putExtra("Description", description[position]);
                        i.putExtra("Contact", contact[position]);
                        i.putExtra("Latitude", latitude[position]);
                        i.putExtra("Longitude", longitude[position]);
                        i.putExtra("InvisibleButton", "invisibleDeleteButton");
                        startActivity(i);
                    }
                });
        }
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
            Intent is = new Intent(MainActivity.this,PropertyChart.class);
            startActivity(is);
            return true;
        }
        if (id == R.id.action_profile) {

            //Go to UserProfile class
            Intent i = new Intent(MainActivity.this,UserProfile.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_preferences) {
            //Switch to class UserPreferences
            Intent i = new Intent(MainActivity.this,UserPreferences.class);
            startActivity(i);
            return true;

        }
        if (id == R.id.action_about) {

            //Switch to class About Faxtons
            Intent i = new Intent(MainActivity.this,AboutFaxtons.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_logout) {

            //Logout user
            myDb.logOutUser();
            //Navigate user to login screen
            Intent i = new Intent(MainActivity.this,LoginRss.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


