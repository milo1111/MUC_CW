package xml.android.milos.com.faxtons;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Milo on 28/11/2016.
 * This class is used to show map and appropriate markers
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Declared variables for location on the map
    public String distanceMil;
    public String distanceKM;
    public String cityLocation = "Glasgow";
    private GoogleMap mMap;
    private String location;
    private Double latitude;
    private Double longitude;
    private Double cityLatitude = 55.8642;
    private Double cityLongitude = -4.2518;

    //Calculate distance in miles and in kilometers
    private static double distance(double latitude1, double longitude1, double latitude2, double longitude2, String unit) {
        double theta = longitude1 - longitude2;
        double dist = Math.sin(deg2rad(latitude1)) * Math.sin(deg2rad(latitude2)) + Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        }
        return (dist);
    }

    //converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Get Location
        location = (String) getIntent().getSerializableExtra("Location");
        //Get latitude of the property and convert string to double
        String sLatitude = (String) getIntent().getSerializableExtra("Latitude");
        latitude = Double.parseDouble(sLatitude);
        //Get longitude of the property and convert string to double
        String sLongitude = (String) getIntent().getSerializableExtra("Longitude");
        longitude = Double.parseDouble(sLongitude);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker for city
        LatLng city = new LatLng(cityLatitude, cityLongitude);
        mMap.addMarker(new MarkerOptions().position(city).title(cityLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(city));

        // Add a marker for selected property
        LatLng property = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(property).title(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(property));

        //Calculated distance
        distanceMil = (distance(cityLatitude, cityLongitude, latitude, longitude, "M") + " Miles\n");
        distanceKM = (distance(cityLatitude, cityLongitude, latitude, longitude, "K") + " Kilometers\n");

        //Reformat distance
        String newMile = distanceMil.substring(0, 6) + " Miles\n";
        String newKM = distanceKM.substring(0, 6) + " Kilometers";

        //Show distance on the screen
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Distance");
        alertDialogBuilder.setMessage("          " + newMile + "          " + newKM);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //Show alert dialog
        alertDialogBuilder.create().show();
    }
}

/*
//Dialog to ask user to enter City to calculate distance
cityEditText = new EditText(this);
        customCity = "";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
        alertDialogBuilder.setTitle("Calculate distance");
        alertDialogBuilder.setView(cityEditText);
        alertDialogBuilder.setNegativeButton("CANCEL", null);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface arg0, int arg1) {
        customCity = cityEditText.getText().toString();
        }
        });

        alertDialogBuilder.create().show();

        if (customCity.equals("Glasgow")) {
        cityLocation = customCity;
        cityLatitude = 55.8642;
        cityLongitude = -4.2518;
        }
        if (customCity.equals("London")) {
        cityLocation = customCity;
        cityLatitude = 55.8642;
        cityLongitude = -4.2518;
        }
        */