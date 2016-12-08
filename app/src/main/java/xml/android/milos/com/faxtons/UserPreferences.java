package xml.android.milos.com.faxtons;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.Toast;

import static xml.android.milos.com.faxtons.R.xml.preferences;

/**
 * Created by Milo on 06/12/2016.
 */

public class UserPreferences extends AppCompatActivity {

    //Create instance name DB
    DatabaseHelper myDb;
    Integer userId;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        checkValues();
        myDb = new DatabaseHelper(this);

        //Get all data from users table
        Cursor cursor = myDb.getLoggedUser();
        if (cursor.getColumnCount() == 0) {
            return;
        }
        cursor.moveToFirst();
        //Retrieve username and userID from user database
        username = cursor.getString(cursor.getColumnIndex("USERNAME"));
        userId = cursor.getInt(cursor.getColumnIndex("USER_ID"));
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(preferences);

        }
    }


    private void checkValues()
    {   //Outputs for selected items
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean splashscreen = sharedPrefs.getBoolean("splashscreen",false);
        boolean splashscreenSound = sharedPrefs.getBoolean("splashscreenSound",false);
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        String soundType = sharedPrefs.getString("soundType", "1");
    }
}
