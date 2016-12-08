package xml.android.milos.com.faxtons;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import static xml.android.milos.com.faxtons.R.id.text;

/**
 * Created by Milo on 23/11/2016.
 */

public class RegisterRss extends AppCompatActivity {

    //Declare variables
    public EditText textUsername, textPassword, textConfirmPassword;
    public String regLog = "1";
    //Create instance name myDB
    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDb = new DatabaseHelper(this);

        //Set variables from entered fields
        textUsername = (EditText) findViewById(R.id.input_username);
        textPassword = (EditText) findViewById(R.id.input_password);
        textConfirmPassword = (EditText) findViewById(R.id.input_confirmPassword);

        //Set night mode for Register layout
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean nightMode = sharedPrefs.getBoolean("nightMode",false);
        if(nightMode) {
            ScrollView sLayout = (ScrollView) findViewById(R.id.registerLayout);
            sLayout.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }

        //When button pressed, register user
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });

        //When user click on link,show terms and conditions pop up menu
        TextView linkTermsAndConditions = (TextView) findViewById(R.id.linkTermsAndConditions);
        linkTermsAndConditions.setOnClickListener(new View.OnClickListener() {

            //Switch to Register class
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterRss.this);
                alertDialogBuilder.setTitle("Terms and conditions");
                alertDialogBuilder.setMessage("This licence to re-copy does not permit incorporation of the material or any part of it in any other work, publication, or website whether in hard copy or electronic or any other form. In particular (but without limitation) no part of Foxtons website, including but not limited, to photographs, property details, virtual tours and/or floorplans may be distributed or copied for any commercial purpose.");
                alertDialogBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(RegisterRss.this, "Thank you", Toast.LENGTH_LONG).show();
                    }
                });

                // Set dialog message text in the middle
                AlertDialog alertDialog = alertDialogBuilder.show();
                // Must call show() prior to fetching text view
                TextView messageView = (TextView) alertDialog.findViewById(android.R.id.message);
                messageView.setGravity(Gravity.CENTER);
                messageView.setTextSize(20);

            }
        });
    }

    //Login user if username and password fields match in database
    public void register() {

        if (!validate()) {
            return;
        }
            //Progress dialog to show user that application is running
            final ProgressDialog progressDialog = new ProgressDialog(RegisterRss.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            //Register a new user
            boolean isInserted = myDb.insertData(textUsername.getText().toString(), textPassword.getText().toString(), regLog.toString());
            if (isInserted == true) {

                //Show message that user is registered
                Toast.makeText(getBaseContext(), "Registration successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Registration unsuccessful, try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
            //Close progress dialog
              progressDialog.dismiss();

            //Go to main activity after registration successful
            Intent i = new Intent(RegisterRss.this, MainActivity.class);
            startActivity(i);

        }

    //Validate username and password that are in correct format
    public boolean validate() {

        //Username
        if (textUsername.length() < 4 || textUsername.length() > 8) {
            textUsername.setError("between 4 and 8 alphanumeric characters");
            return false;
        }
        //Password
        if (textPassword.length() < 4 || textPassword.length() > 8) {
            textPassword.setError("between 4 and 8 characters");
            return false;
        }
        //Confirm Password
        if (textConfirmPassword.length() < 4 || textConfirmPassword.length() > 8) {
            textConfirmPassword.setError("between 4 and 8 characters");
            return false;
        }
        //Compare Passwords
        if (!textConfirmPassword.getText().toString().equals(textPassword.getText().toString())) {
            textConfirmPassword.setError("incorrect password");
            textPassword.setError("incorrect password");
            return false;
        }
        return true;
    }
}




