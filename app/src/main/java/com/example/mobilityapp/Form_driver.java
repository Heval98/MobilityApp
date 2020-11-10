package com.example.mobilityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobilityapp.model.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Form_driver extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_INTENT = 1;
    public String uuid_customer;
    private static final String TAG = "";

    private FirebaseAuth mAuth;
    EditText name_driver, last_name_driver, document_driver,
            email_driver, address_driver, property_card,
            bank_account, password_driver;

    Button button_register_driver, button_up_soat, button_up_tecno,
            button_up_car, button_up_inside_the_car, button_up_motor, button_up_placa;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        uuid_customer = UUID.randomUUID().toString();
        setContentView(R.layout.activity_form_driver);

        name_driver = (EditText)findViewById(R.id.txt_nombrePersona);
        last_name_driver = (EditText)findViewById(R.id.txt_apellidoPersona);
        document_driver = (EditText)findViewById(R.id.txt_cedulaPersona);
        email_driver = (EditText)findViewById(R.id.txt_emailPersona);
        address_driver = (EditText)findViewById(R.id.txt_direccionPersona);
        property_card = (EditText)findViewById(R.id.txt_tarjetapropiedad);
        bank_account = (EditText)findViewById(R.id.txt_cuenta_bancaria);
        password_driver = (EditText)findViewById(R.id.txt_contraseñaPerson);

        button_register_driver = (Button)findViewById(R.id.button_register_driver);
        button_up_soat = (Button)findViewById(R.id.button_up_soat);
        button_up_tecno = (Button)findViewById(R.id.button_up_tecno);
        button_up_car = (Button)findViewById(R.id.button_up_car);
        button_up_inside_the_car = (Button)findViewById(R.id.button_up_inside_the_car);
        button_up_motor = (Button)findViewById(R.id.button_up_motor);
        button_up_placa = (Button)findViewById(R.id.button_up_placa);


        button_register_driver.setOnClickListener(this);
        button_up_soat.setOnClickListener(this);
        button_up_tecno.setOnClickListener(this);
        button_up_car.setOnClickListener(this);
        button_up_inside_the_car.setOnClickListener(this);
        button_up_motor.setOnClickListener(this);
        button_up_placa.setOnClickListener(this);

        initialize_firebase();
    }

    @Override
    public void onClick(View view){
        int buttonID = view.getId();
        String name = name_driver.getText().toString();
        String last_name = last_name_driver.getText().toString();
        String document = document_driver.getText().toString();
        String email = email_driver.getText().toString();
        String address = address_driver.getText().toString();
        String property = property_card.getText().toString();
        String bank = bank_account.getText().toString();
        String password = password_driver.getText().toString();

        if (buttonID == R.id.button_register_driver){
            if (name.equals("")|| last_name.equals("")||
                    document.equals("")|| email.equals("")
                    ||address.equals("")||property.equals("")||
                    bank.equals("")||password.equals("")){
                validation();
            }else {
                Driver d = new Driver();
                d.setUid(uuid_customer);
                d.setName_driver(name);
                d.setLast_name_driver(last_name);
                d.setDocument_person(document);
                d.setEmail(email);
                d.setAddress_person(address);
                d.setProperty_card(property);
                d.setBank_account(bank);
                d.setPassword_person(password);
                databaseReference.child("Driver").child(d.getUid()).setValue(d);
                Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                clean_boxes();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Form_driver.this, "Registration Success.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Form_driver.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
                AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
                myBuild.setMessage("Tu respuesta se dara en 5 dias si fuiste admintido");
                myBuild.setTitle("Espera de confirmación");
                myBuild.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Form_driver.this, MainMap.class);
                        startActivity(intent);
                    }
                });

                AlertDialog dialog = myBuild.create();
                dialog.show();
            }

        }
        else if (buttonID == R.id.button_up_soat){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        }
        else if (buttonID == R.id.button_up_tecno){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        }
        else if (buttonID == R.id.button_up_car){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        }
        else if (buttonID == R.id.button_up_inside_the_car){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        }
        else if (buttonID == R.id.button_up_motor){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        }
        else if (buttonID == R.id.button_up_placa){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        }
    }

    private void initialize_firebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }


    private void clean_boxes() {
        name_driver.setText("");
        last_name_driver.setText("");
        document_driver.setText("");
        email_driver.setText("");
        address_driver.setText("");
        property_card.setText("");
        bank_account.setText("");
        password_driver.setText("");
    }

    private void validation() {
        String name = name_driver.getText().toString();
        String last_name = last_name_driver.getText().toString();
        String document = document_driver.getText().toString();
        String email = email_driver.getText().toString();
        String address = address_driver.getText().toString();
        String property = property_card.getText().toString();
        String bank = bank_account.getText().toString();
        String password = password_driver.getText().toString();

        if (name.equals("")){
            name_driver.setError("Required");
        }
        else if(last_name.equals("")){
            last_name_driver.setError("Required");
        }
        else if(document.equals("")){
            document_driver.setError("Required");
        }
        else if(email.equals("")){
            email_driver.setError("Required");
        }
        else if(address.equals("")){
            address_driver.setError("Required");
        }
        else if(property.equals("")){
            property_card.setError("Required");
        }
        else if(bank.equals("")){
            bank_account.setError("Required");
        }
        else if(password.equals("")){
            password_driver.setError("Required");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            Uri uri = data.getData();

            StorageReference filePath = storageReference.child(uuid_customer).child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Form_driver.this, "Se subio exitosamente la foto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}