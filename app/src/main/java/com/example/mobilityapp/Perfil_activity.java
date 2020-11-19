package com.example.mobilityapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Perfil_activity extends AppCompatActivity implements View.OnClickListener {

    public Button metodo;
    public Button lugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        lugares = (Button) findViewById(R.id.btnLugares);
        lugares.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int buttonID = view.getId();
        switch (buttonID) {
            case R.id.btnLugares:
                startActivity(new Intent(Perfil_activity.this, Lugares.class));
                break;
        }
    }
}
