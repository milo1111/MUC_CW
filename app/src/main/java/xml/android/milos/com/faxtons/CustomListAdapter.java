package xml.android.milos.com.faxtons;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Milo on 19/11/2016.
 * This class is used to set custom adapter
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[]  locations;
    private final Bitmap[]  images;
    private final String[]  prices;
    private final String[]  newDates;


    public CustomListAdapter(Activity context,  String[] location,Bitmap[] image, String[]  price, String[]  newDate) {
        super(context, R.layout.row_main_layout, location);

        this.context = context;
        images = image;
        locations = location;
        prices = price;
        newDates = newDate;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_main_layout, null,true);

        // Image:
        ImageView image=(ImageView)rowView.findViewById(R.id.image);
        image.setImageBitmap(images [position]);

        // Location:
        TextView location = (TextView)rowView.findViewById(R.id.item_location);
        location.setText(locations[position]);

        // Price:
        TextView price = (TextView)rowView.findViewById(R.id.item_price);
        price.setText(prices[position]);

        // Date:
        TextView date = (TextView)rowView.findViewById(R.id.item_date);
        date.setText(newDates[position]);

        return rowView;
    };
}
