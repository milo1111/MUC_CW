package xml.android.milos.com.faxtons;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Created by Milo on 06/12/2016.
 */
public class SplashScreen extends AppCompatActivity {

    //Declared variables
    boolean splashscreen;
    boolean splashscreenSound;
    String soundType;
    MediaPlayer mySound;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Get data about preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        splashscreen = sharedPrefs.getBoolean("splashscreen", false);
        splashscreenSound = sharedPrefs.getBoolean("splashscreenSound", false);
        soundType = sharedPrefs.getString("soundType", "1");

        //Select sound from raw file and save it into variable (selected sound depends on users preference)
        if (soundType.toString().equals("Instrumental Theme")) {
            mySound = MediaPlayer.create(this, R.raw.instrumental_theme);
        }
        if (soundType.toString().equals("Orchestral Sunset")) {
            mySound = MediaPlayer.create(this, R.raw.orchestral_sunset);
        }


        //Set splash screen on thread
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    //Set timer when preference is set to use splash screen
                    if (splashscreen) {
                        //Play sound if selected
                        if (splashscreenSound) {
                            mySound.start();
                        }
                        //Set timer
                        sleep(7000);
                    }
                    //Set timer to zero when preference is set to not use splash screen
                    if (!splashscreen) {
                        sleep(0);
                    }
                    //Switch to main activity
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    //Stop thread
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //Start thread
        myThread.start();
    }
}