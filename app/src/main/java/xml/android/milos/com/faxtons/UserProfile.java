package xml.android.milos.com.faxtons;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Milo on 24/11/2016.
 */

public class UserProfile extends AppCompatActivity {

    //Declare variables
    public EditText textEditUsername, textEditPassword, textEditConfirmPassword;
    String username, password,editUsername,editPassword;
    int userId;
    //Create instance name myDb
    DatabaseHelper myDb;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myDb = new DatabaseHelper(this);

        //Set night mode for Profile layout
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            ScrollView pLayout = (ScrollView) findViewById(R.id.profileLayout);
            pLayout.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }

        //Set variables from entered fields
        textEditUsername = (EditText) findViewById(R.id.input_editUsername);
        textEditPassword = (EditText) findViewById(R.id.input_editPassword);
        textEditConfirmPassword = (EditText) findViewById(R.id.input_editConfirmPassword);

        //Get all data from users table
        Cursor cursor = myDb.getLoggedUser();
        if (cursor.getColumnCount() == 0) {
            return;
        }
            cursor.moveToFirst();
            //Retrieve user`s  data from database
            username = cursor.getString(cursor.getColumnIndex("USERNAME"));
            password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
            userId = cursor.getInt(cursor.getColumnIndex("USER_ID"));

            //Show users details on the screen
            textEditUsername.setText(username);
            textEditPassword.setText(password);

            //When button pressed, register user
            Button btnSave = (Button) findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveUser();
                }
            });

    }

    //Save user`s data if are entered correctly
    public void saveUser() {

        if (!validate()) {
            return;
        }
        //Progress dialog to show user that application is running
        final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        //Update users data in the database
        editUsername = textEditUsername.getText().toString();
        editPassword = textEditPassword.getText().toString();
        myDb.updateData(editUsername,editPassword,userId);

        //Close progress dialog
        progressDialog.dismiss();

        //After details updated successfully, go back to MainActivity
        Intent i = new Intent(UserProfile.this, MainActivity.class);
        startActivity(i);

    }

    //Validate username and password that are in correct format
    public boolean validate() {

        //Username
        if (textEditUsername.length() < 4 || textEditUsername.length() > 8) {
            textEditUsername.setError("between 4 and 8 alphanumeric characters");
            return false;
        }
        //Password
        if (textEditPassword.length() < 4 || textEditPassword.length() > 8) {
            textEditPassword.setError("between 4 and 8 characters");
            return false;
        }
        //Confirm Password
        if (textEditConfirmPassword.length() < 4 || textEditConfirmPassword.length() > 8) {
            textEditConfirmPassword.setError("between 4 and 8 characters");
            return false;
        }
        //Compare Passwords
        if (!textEditConfirmPassword.getText().toString().equals(textEditPassword.getText().toString())) {
            textEditConfirmPassword.setError("incorrect password");
            textEditPassword.setError("incorrect password");
            return false;
        }
        return true;
    }

}