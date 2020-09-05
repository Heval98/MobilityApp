package com.example.mobilityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobilityapp.model.Driver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Form_driver extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_INTENT = 1;

    EditText name_driver, last_name_driver, document_driver,
            email_driver, address_driver, property_card,
            bank_account, password_driver;

    Button button_register_driver, button_up_image;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        button_up_image = (Button)findViewById(R.id.button_up);
        button_register_driver.setOnClickListener(this);
        button_up_image.setOnClickListener(this);

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
                d.setUid(UUID.randomUUID().toString());
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
            }
        }
        else if (buttonID == R.id.button_up){
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

            StorageReference filePath = storageReference.child("fotos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Form_driver.this, "Se subio exitosamente la foto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}