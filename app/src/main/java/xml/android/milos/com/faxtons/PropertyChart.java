package xml.android.milos.com.faxtons;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milo on 08/12/2016.
 * This class shows graphical information on the screen depended on the price. Check the properties prices and compare with other properties
 */

public class PropertyChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_chart);

        //Set night mode for layout for property detail
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.activity_car_mpgchart);
            rLayout.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }

        BarChart barChart = (BarChart) findViewById(R.id.barchartproperty);
        //Get database for property
        SQLiteHelper data = new SQLiteHelper(getApplicationContext());
        //Retrieve all properties
        Cursor property =  data.getAllData();
        //declare variables to arraylists
        ArrayList<String> label = new ArrayList<>();
        List<IBarDataSet> iBarDataSets = new ArrayList<>();
        //Find position of the property in the database
        int counter = 0;
        while(property.moveToNext()) {
            //Declared arraylist
            List<BarEntry> entry = new ArrayList<>();
            //Insert properties
            entry.add(new BarEntry(counter++, property.getInt(property.getColumnIndex("PROPERTY_ID"))));
            BarDataSet barDataSet = new BarDataSet(entry, property.getString(property.getColumnIndex("PRICE")));
            //Cheese different colours
            barDataSet.setColor(getRandColor());
            //set datefor the properties
            iBarDataSets.add(barDataSet);
            label.add(property.getString(property.getColumnIndex("DATE")));
        }

        //Set all data colected
        BarData barData = new BarData(iBarDataSets);

        // set description for barchart
        Description description = new Description();
        description.setText("PROPERTIES");
        description.setTextSize(16f);
        //Set all data into  bar chart
        barChart.setDescription(description);
        //Set required data
        barChart.setData(barData);
        //Restart bar chart
        barChart.invalidate();
    }
    // Chose diffrent colors
    private int getRandColor(){
        int r = (int)(Math.random()*256);
        int g = (int)(Math.random()*256);
        int b = (int)(Math.random()*256);
        return Color.rgb(r,g,b);
    }
}
