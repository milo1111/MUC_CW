package xml.android.milos.com.faxtons;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Milo on 23/11/2016.
 * This is class for Login
 */

public class LoginRss extends AppCompatActivity {

    //Declared variables
    public EditText txtUsername;
    public EditText txtPassword;
    public int userId;
    public String username;
    public String password;
    //Create instance name myDB
    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDb = new DatabaseHelper(this);

        //Save entered details in variables
        txtUsername = (EditText) findViewById(R.id.input_signUsername);
        txtPassword = (EditText) findViewById(R.id.input_signPassword);

        //Set night mode for login layout
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            ScrollView sLayout = (ScrollView) findViewById(R.id.loginLayout);
            sLayout.setBackgroundColor(Color.parseColor("#C0C0C0"));

        }

        //Button to login user
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });

        //Set Create account TextViev clickable
        TextView linkCreateAccount = (TextView) findViewById(R.id.linkCreateAccount);
        linkCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Navigate user to create an account
                Intent intent = new Intent(LoginRss.this, RegisterRss.class);
                startActivity(intent);
            }
        });
    }

    //Login user if username and password fields match in database
    public void login() {
        if (!validate()) {
            return;
        }

        //Progress dialog to show user that application is running
        final ProgressDialog progressDialog = new ProgressDialog(LoginRss.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        //Get all data from users table
        Cursor cursor = myDb.getAllData();
        if (cursor.getColumnCount() == 0) {
            return;
        }
        //Set entered data for username and password in variables
        String enteredUsername = txtUsername.getText().toString();
        String enteredPassword = txtPassword.getText().toString();
        while (cursor.moveToNext()) {

            //Retrieve data from database
            userId = cursor.getInt(cursor.getColumnIndex("USER_ID"));
            username = cursor.getString(cursor.getColumnIndex("USERNAME"));
            password = cursor.getString(cursor.getColumnIndex("PASSWORD"));

            //Compare entered data and data in database
            if (username.equals(enteredUsername) && password.equals(enteredPassword)) {

                //Show message Login successful
                Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();

                //Set user to be logged
                myDb.logUserIn(userId);

                //Switch to main class and show properties
                Intent intent = new Intent(LoginRss.this, MainActivity.class);
                //Send user`s id and username to main class
                intent.putExtra("id", userId);
                startActivity(intent);

            }
            progressDialog.dismiss();
        }
    }

    //Validate username and password that are in correct length
    public boolean validate() {

        //Username
        if (txtUsername.length() < 4 || txtUsername.length() > 8) {
            txtUsername.setError("between 4 and 8 alphanumeric characters");
            return false;
        }
        //Password
        if (txtPassword.length() < 4 || txtPassword.length() > 8) {
            txtPassword.setError("between 4 and 8 characters");
            return false;
        }
        return true;
    }
}



