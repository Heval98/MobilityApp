package com.example.mobilityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //layout variables
    EditText tUser, tPassword;
    Button bLogin, bRegister_user, bRegister_driver;

    //Authentication variables
    private FirebaseAuth mAuth;
    private static final String TAG = "";

    CheckBox remember;

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

        remember = (CheckBox) findViewById(R.id.rememberMe);
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkBoxString = preferences.getString("remember", "");
        SharedPreferences preferences2 = getSharedPreferences("UserAuth", MODE_PRIVATE);
        String UserAuth = preferences2.getString("Logged", "");
        if(checkBoxString.equals("true") && UserAuth.equals("true")){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }else if(checkBoxString.equals("false")){
            //
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Checked", Toast.LENGTH_SHORT).show();

                }else if(!compoundButton.isChecked()){

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();

                    SharedPreferences preferences2 = getSharedPreferences("UserAuth", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = preferences2.edit();
                    editor2.putString("Logged", "false");
                    editor2.apply();
                    Toast.makeText(MainActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //Biometric
        TextView fingerText = findViewById(R.id.fingerText);
        Button biometricButton = findViewById(R.id.figerprint);

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                fingerText.setText("Now you can put your finger");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                fingerText.setText("Your device doesn't have fingerprint sensor");
                biometricButton.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                fingerText.setText("The biometric sensors is currently unavailable");
                biometricButton.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                fingerText.setText("Your device doesn't have any fingerprint saved");
                biometricButton.setVisibility(View.GONE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override // this method is called when there is an error while the authentication
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override // this method is called when the authentication is a success
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);SharedPreferences preferences = getSharedPreferences("UserAuth", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Logged", "true");
                editor.apply();
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
            }

            @Override // this method is called if we have failed the authentication
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promtInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();

        biometricButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promtInfo);
            }
        });

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
                            SharedPreferences preferences = getSharedPreferences("UserAuth", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Logged", "true");
                            editor.apply();
                            SharedPreferences preferences2 = getSharedPreferences("UserAuthName", MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = preferences2.edit();
                            editor2.putString("User", tUser.getText().toString());
                            editor2.apply();
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