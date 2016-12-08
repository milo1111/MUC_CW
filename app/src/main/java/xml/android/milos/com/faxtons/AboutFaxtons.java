package xml.android.milos.com.faxtons;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Milo on 30/11/2016.
 * This class is used to show information about Faxtons Agency
 */
public class AboutFaxtons extends AppCompatActivity {

    //Create instance name db
    PrepopulatedDatabase dbs;

    //Declared variables about Faxtons agency
    String fInfo = "Foxtons has the advantage of having a unique insight into the property market, through the sheer volume of properties available for sale and to let. Foxtons expert valuers carry out thousands of valuations every month, giving them intimate and unparalleled knowledge of property values across London and Surrey. Foxtons.co.uk is regularly voted the best in the business. Every property, regardless of price, features floorplans, 360° virtual tours, numerous high-resolution photographs, slide shows, local information, location maps, aerial views and full colour brochures.";
    String fLocation = "73-79 Balham High Road, London, SW12 9AP";
    String fContact = "020 8772 8000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_faxtons);
        dbs = new PrepopulatedDatabase(this);

        //Set night mode for About Faxtons layout
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            RelativeLayout pLayout = (RelativeLayout) findViewById(R.id.activity_about_faxtons);
            pLayout.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }

/*********************************************************************************************************
     //This code has been used to save data to the database about Faxtons property agency

        //Save data to the Faxtons database
        boolean isInserted = dbs.insertData(fInfo.toString(), fLocation.toString(), fContact.toString());
        if (isInserted == true) {

            //Show message that data about Faxtons are saved
            Toast.makeText(getBaseContext(), "Data saved successfuly", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "Data doesn`t saved successfuly", Toast.LENGTH_LONG).show();
            return;
        }
*//**********************************************************************************************************/

        //Get all data from faxtons table
        Cursor cursor = dbs.getFaxtonData();
        if (cursor.getColumnCount() == 0) {
            return;
        }
        cursor.moveToFirst();
        //Retrieve data about Faxtons from prepopulated database
        String fInfo = cursor.getString(cursor.getColumnIndex("FAXTONS_INFO"));
        String fLocation = cursor.getString(cursor.getColumnIndex("FAXTONS_LOCATION"));
        String fContact = cursor.getString(cursor.getColumnIndex("FAXTONS_CONTACT"));

        //Get description about Faxtons properties
        TextView textAboutFaxtons = (TextView) findViewById(R.id.txtAboutFaxtons);
        textAboutFaxtons.setText("About: " + fInfo);

        //Set Faxtons logo in the imageView
        ImageView imageAbout = (ImageView) findViewById(R.id.imageAbout);
        imageAbout.setImageResource(R.drawable.foxtons_logo);


        //Get description about Faxtons properties
        TextView textFaxtonLocation = (TextView) findViewById(R.id.txtAboutLocation);
        textFaxtonLocation.setText("Location: " + fLocation);

        //Get contact number for Faxtons
        TextView textFaxtonContact = (TextView) findViewById(R.id.txtAboutContact);
        textFaxtonContact.setText("Contact: " + fContact);

    }
}
