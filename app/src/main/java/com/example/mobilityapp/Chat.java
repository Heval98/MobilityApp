package com.example.mobilityapp;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
//import com.hn.universoandroid.androidchatfirebase.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    // Declarar variables
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    SimpleDateFormat sdf;
    // URL del servicio firebase
    String URL_FIREBASE = "https://mobility-app-93ea3.firebaseio.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Crear formato de fecha y hora
        sdf = new SimpleDateFormat("EEE, MMM d 'AT' HH:mm a");
        // Obtener referencia de controles
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        // ASiganar context a la variable Firebase
        Firebase.setAndroidContext(this);
        // Asignar ruta de las URL
        reference1 = new Firebase(URL_FIREBASE + "/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase(URL_FIREBASE + "/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener Mensaje del EditText
                String messageText = messageArea.getText().toString();
                // Validar que el mensaje no sea nullo
                if (!messageText.equals("")) {
                    Map map = new HashMap();
                    String currentDateandTime = sdf.format(new Date());
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("time", currentDateandTime);
                    // Enviar mensajes
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });
        // Referencia en espera de recibir nuevos mensajes
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Obtener mensajes
                Map map = dataSnapshot.getValue(Map.class);
                // Asignar mensajes a las variables
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String time = map.get("time").toString();
                // Si el usuario es igual al usuario logeado añadira la etiqueta "You"
                if (userName.equals(UserDetails.username)) {
                    addMessageBox("You ", message, time, 1);
                } else {
                    addMessageBox(UserDetails.chatWith, message, time, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Añadir mensajes
    public void addMessageBox(String name, String message, String time, int type) {
        // Crear controles de forma dinamica
        TextView textmsg = new TextView(Chat.this);
        TextView textname = new TextView(Chat.this);
        TextView texttime = new TextView(Chat.this);
        //  Añadire valores a los TextView
        textname.setText(name);
        textname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        textmsg.setText(message);
        texttime.setText(time);
        texttime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        // Crear controles LinearLayout
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;
        // Tipo de mensaje
        if (type == 1) {
            // Alinear a la derecha
            lp1.gravity = Gravity.RIGHT;
            lp2.gravity = Gravity.RIGHT;
            lp3.gravity = Gravity.RIGHT;
            // Añadir textview derecha
            textmsg.setBackgroundResource(R.drawable.text_in);
        } else {
            // Alinear el mensaje a la izquierda
            lp1.gravity = Gravity.LEFT;
            lp2.gravity = Gravity.LEFT;
            lp3.gravity = Gravity.LEFT;
            // Añadir textview izquierda
            textmsg.setBackgroundResource(R.drawable.text_out);
        }
        // Añadir parametros
        textname.setLayoutParams(lp1);
        textmsg.setLayoutParams(lp2);
        texttime.setLayoutParams(lp3);
        // Añadir controles al Layaout
        layout.addView(textname);
        layout.addView(textmsg);
        layout.addView(texttime);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}