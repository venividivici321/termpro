package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignInActivity extends AppCompatActivity {

    EditText usernameText, passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usernameText = findViewById(R.id.user_name_signup_activity);
        passwordText = findViewById(R.id.password_text_signup_activity);
        ParseUser parseUser = ParseUser.getCurrentUser();
        try {
            if (parseUser != null) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        }catch (Exception e){

        }

    }
    public void signUp(View view) {

        ParseUser user = new ParseUser();
        String username = usernameText.getText().toString();
        String userpass = passwordText.getText().toString();

        user.setUsername(username);
        user.setPassword(userpass);

        if (username == "" || userpass == "") {
            Toast.makeText(getApplicationContext(), "Please fill blanks", Toast.LENGTH_LONG).show();
        } else {
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "User Signed Up!!!", Toast.LENGTH_LONG).show();
                        //intent
                        Intent intent = new Intent(getApplicationContext(), LocationsActivity.class);
                        startActivity(intent);

                    }
                }
            });
        }
    }
    public void signIn(View view) {

        ParseUser.logInInBackground(usernameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e!=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Welcome: " + user.getUsername(),Toast.LENGTH_LONG).show();
                    //intent

                    Intent intent = new Intent(getApplicationContext(),LocationsActivity.class);
                    startActivity(intent);

                }
            }
        });
    }
}
