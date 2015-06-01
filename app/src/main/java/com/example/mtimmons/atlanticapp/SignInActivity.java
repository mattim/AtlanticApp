package com.example.mtimmons.atlanticapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignInActivity extends ActionBarActivity {
    private Button mLogInButton, mBackButton;
    private EditText mUserName,mPassword,mConfirmPassword;
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private boolean loggedIn,check;
    private String mUsername,mPass;
    private static final String SPFILE = "MySharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mLogInButton = (Button)findViewById(R.id.logIn);
        mBackButton = (Button)findViewById(R.id.back);
        mUserName = (EditText)findViewById(R.id.userName);
        mPassword = (EditText)findViewById(R.id.password);
        mConfirmPassword = (EditText)findViewById(R.id.confirm);

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password,confirm;
                username = mUserName.getText().toString();
                password = mPassword.getText().toString();
                confirm = mConfirmPassword.getText().toString();
                if(username.equals("")||password.equals("")||confirm.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Please complete the log in form",
                            Toast.LENGTH_LONG).show();
                }
                else if(!password.equals(confirm)){
                    Toast.makeText(getApplicationContext(),
                            "The passwords do not match.",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    preferenceSettings = getSharedPreferences(SPFILE,PREFERENCE_MODE_PRIVATE);
                    preferenceEditor = preferenceSettings.edit();
                    preferenceEditor.putBoolean("loggedIn", true);
                    preferenceEditor.putString("username", username);
                    preferenceEditor.putString("password", password);
                    check= preferenceEditor.commit();
                    /*mUsername = preferenceSettings.getString("username","");
                    loggedIn = preferenceSettings.getBoolean("loggedIn", false);//defaults to false
                    Log.d("SignInActivity", "loggedIn is " + loggedIn);
                    Log.d("SignInActivity", "Username is " + mUsername);*/
                    Intent i = new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceSettings = getSharedPreferences(SPFILE, PREFERENCE_MODE_PRIVATE);
                preferenceEditor = preferenceSettings.edit();
                String username,password;
                if( preferenceSettings.getString("username","").equals("")||preferenceSettings.getString("password","").equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Please complete the log in form",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Intent i = new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
