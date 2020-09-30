package com.example.mobilityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //layout variables
    EditText tUser, tPassword;
    Button bLogin, bRegister_user, bRegister_driver;

    //Authentication variables
    private FirebaseAuth mAuth;
    private static final String TAG = "";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tUser = (EditText)findViewById(R.id.User);
        tPassword = (EditText)findViewById(R.id.Password);
        bLogin = (Button)findViewById(R.id.Login);
        bRegister_user = (Button)findViewById(R.id.Register_user);
        bRegister_driver = (Button)findViewById(R.id.Register_driver);

        bLogin.setOnClickListener(this);
        bRegister_user.setOnClickListener(this);
        bRegister_driver.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View view) {
        int buttonID = view.getId();
        switch (buttonID){
            case R.id.Login:
                //Do the login action
                if(tPassword.getText().length() > 0 && tUser.getText().length() > 0){
                    validateUser();
                }else{
                    Toast.makeText(MainActivity.this, "User or Password is empty.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.Register_user:
                if(tPassword.getText().length() > 0 && tUser.getText().length() > 0){
                    RegisterUser();
                }else{
                    Toast.makeText(MainActivity.this, "User or Password is empty.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.Register_driver:
                startActivity(new Intent(MainActivity.this, Form_driver.class));
                break;
            default:
                break;
        }
    }

    private void validateUser(){
        String email = tUser.getText().toString();
        String password = tPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, MapsActivity.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void RegisterUser(){
        //New changes
        String email = tUser.getText().toString();
        String password = tPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Registration Success.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

}