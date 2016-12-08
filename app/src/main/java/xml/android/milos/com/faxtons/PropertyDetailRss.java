package xml.android.milos.com.faxtons;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by Milo on 19/11/2016.
 */
public class PropertyDetailRss extends AppCompatActivity {

    //Declared variables for property
    String location, price, date, description, contact, latitude, longitude;
    Integer propertyID;
    Bitmap picture;
    byte[] buffer;
    String invisible = "";
    //Create instance name db
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_detail_rss);
        db = new SQLiteHelper(this);

        //Set night mode for layout for property detail
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.activity_property_rss_detail);
            rLayout.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }
            //Make button invisible
            if (getIntent().getExtras().getString("InvisibleButton") != null) {
                invisible = getIntent().getExtras().getString("InvisibleButton");
            }

            //Set text and size for save button
            Button saveButton = (Button) findViewById(R.id.btnSaveButton);
            //Make save button invisible when message comes from Main activity class
            if (invisible.equals("invisibleSaveButton")) saveButton.setVisibility(View.GONE);
            saveButton.setText("Save");
            saveButton.setTextSize(20);

            //Set text and size for delete button
            Button deleteButton = (Button) findViewById(R.id.btnDelete);
            //Make delete button invisible when message comes from Property Database class
            if (invisible.equals("invisibleDeleteButton")) deleteButton.setVisibility(View.GONE);
            deleteButton.setText("Delete");
            deleteButton.setTextSize(20);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Progress dialog to show user that application is running
                    final ProgressDialog progressDialog = new ProgressDialog(PropertyDetailRss.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                    //Set Dialog to ask user to delete property
                    // TODO Auto-generated method stub
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PropertyDetailRss.this);
                    alertDialogBuilder.setTitle("Delete");
                    alertDialogBuilder.setMessage("Delete Property?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Show message that property was deleted
                            Toast.makeText(PropertyDetailRss.this, "Deleted", Toast.LENGTH_LONG).show();
                            //Delete property details in the database
                            db.deleteData(propertyID);
                            //Go to PropertyDatabse class to show properties
                            Intent i = new Intent(getApplicationContext(), PropertyDatabase.class);
                            startActivity(i);
                        }
                    });
                    //Set negative button in alert Dialog
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    //Show alert dialog
                    alertDialogBuilder.create().show();
                    //Close progress dialog
                    progressDialog.dismiss();
                }
            });


            //Set map TextViev clickable and sent information about property into MapsActivity class
            TextView mapView = (TextView) findViewById(R.id.mapView);
            mapView.setPaintFlags(mapView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            mapView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(PropertyDetailRss.this, MapsActivity.class);
                    intent.putExtra("Location", location);
                    intent.putExtra("Latitude", latitude);
                    intent.putExtra("Longitude", longitude);
                    startActivity(intent);
                }
            });

            //Get Location
            location = (String) getIntent().getSerializableExtra("Location");
            TextView textLocation = (TextView) findViewById(R.id.txtLocation);
            textLocation.setText(location);

            //Get Image
            picture = getIntent().getParcelableExtra("Picture");
            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(picture);

            //Get Price
            price = (String) getIntent().getSerializableExtra("Price");
            TextView textPrice = (TextView) findViewById(R.id.txtPrice);
            textPrice.setText("Price: " + price);

            //Get Description
            description = (String) getIntent().getSerializableExtra("Description");
            TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
            txtDescription.setText(description);

            //Get Contact
            contact = (String) getIntent().getSerializableExtra("Contact");
            TextView txtContact = (TextView) findViewById(R.id.txtContact);
            txtContact.setText("Contact: " + contact);


            //Get property ID
            propertyID = (Integer) getIntent().getSerializableExtra("propertyID");
            //Get Date
            date = (String) getIntent().getSerializableExtra("Date");
            //Get latitude of the property
            latitude = (String) getIntent().getSerializableExtra("Latitude");
            //Get longitude of the property
            longitude = (String) getIntent().getSerializableExtra("Longitude");

            // Convert the image into byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.PNG, 100, out);
            buffer = out.toByteArray();

            //When button pressed, register user
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Progress dialog to show user that application is running
                    final ProgressDialog progressDialog = new ProgressDialog(PropertyDetailRss.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                    //Set Dialog to ask user to save property
                    // TODO Auto-generated method stub
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PropertyDetailRss.this);
                    alertDialogBuilder.setTitle("Save");
                    alertDialogBuilder.setMessage("Save Property?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Save properties into database
                            boolean isInserted = db.insertData(location, price, date, buffer, description, contact, latitude, longitude);
                            if (isInserted == true) {
                                //Show message that property was saved
                                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
                                //Go to PropertyDatabse class to show properties
                                Intent i = new Intent(getApplicationContext(), PropertyDatabase.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getBaseContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                return;
                            }
                        }
                    });
                    //Set negative button in alert Dialog
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    //Show alert dialog
                    alertDialogBuilder.create().show();
                    //Close progress dialog
                    progressDialog.dismiss();
                }
            });
        }
    }

