package com.example.mobilityapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Lugares extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnAñadir;
    private ListView mListview;
    private EditText mEditText;
    private List<String> mLista = new ArrayList<>();
    public ArrayAdapter<String> adapter;



@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lugares_favoritos);

        btnAñadir = findViewById(R.id.btnAgregar);
        btnAñadir.setOnClickListener(this);
        mListview = findViewById(R.id.listView);
        mListview.setOnItemClickListener(this);
        mEditText = findViewById(R.id.etLista);


    }

    @Override
    public void onClick(View view) {

    switch (view.getId()){
        case R.id.btnAgregar: String texto = mEditText.getText().toString().trim();
            mLista.add(texto);
        mEditText.getText().clear();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mLista);
            mListview.setAdapter(adapter);
    }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas eliminar este sitio?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mLista.remove(position);
                        mListview.setAdapter(adapter);
                    }

                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
