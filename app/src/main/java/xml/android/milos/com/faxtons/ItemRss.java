package xml.android.milos.com.faxtons;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Milo on 20/11/2016.
 * This class has constructor to hold information from RssParser class
 */

public class ItemRss implements Serializable{

    //Declared variables
    private String title;
    private String description;
    private String date;
    private String geoPoint;

    private String aLocation;
    private String aPrice;
    private String aDate;
    private String aDescription;
    private String aContact;
    private String aLatitude;
    private String alongitude;

/*
    public ItemRss( String aLocation, String aPrice, String aDate, String aDescription, String aContact, String aLatitude, String alongitude) {

        this.aLocation = aLocation;
        this.aPrice = aPrice;
        this.aDate = aDate;
        this.aDescription = aDescription;
        this.aContact = aContact;
        this.aLatitude = aLatitude;
        this.alongitude = alongitude;

    }
*/
     //Getter for title
    public String getTitle() {
        return title;
    }
   //Setter for title
    public void setTitle(String title) {
        this.title = title;
    }
    //Getter for Description
    public String getDescription() {
        return description;
    }
    //Setter for Description
    public void setDescription(String description) {
        this.description = description;
    }
    //Getter for date
    public String getDate() {
        return date;
    }
    //Setter for date
    public void setDate(String date) {
        this.date = date;
    }
    //Getter for latitude and longitude
    public String getGeoPoint() {
        return geoPoint;
    }
    //Setter for latitude and longitude
    public void setGeoPoint(String geoPoint) {
        this.geoPoint = geoPoint;
    }


    public String getAlocation() {
        return aLocation;
    }
    public String getAprice() {
        return aPrice;
    }
    public String getAdate() {
        return aDate;
    }
    public String getAdescription() {
        return aDescription;
    }
    public String getAcontact() {
        return aContact;
    }
    public String getAlatitude() {
        return aLatitude;
    }
    public String getAlongitude() {
        return alongitude;
    }


    public String toString() {
        String temp;

        temp = title + " " + description + " " + date + " " + geoPoint;

        return temp;
    }
} // End of class