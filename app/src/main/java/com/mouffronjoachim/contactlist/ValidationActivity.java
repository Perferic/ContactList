package com.mouffronjoachim.contactlist;
//Gérer les dépendances
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ValidationActivity extends AppCompatActivity {
    //Créer les éléments : 2 boutons, une zone de texte/une liste
    Button sendButton, cancelButton;
    TextView contactsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        //"Binder" les boutons de validation et d'anulation
        sendButton = findViewById(R.id.bouton_validation);
        cancelButton = findViewById(R.id.bouton_annulation);
        contactsText = findViewById(R.id.wishList);
        Intent intentResponse = getIntent();
        ArrayList<Contact> sentContacts = (ArrayList<Contact>) intentResponse.getSerializableExtra("contacts");
        StringBuilder contactMessage = new StringBuilder();
        for(Contact contact: sentContacts){
            contactMessage.append(contact);
        }
        contactsText.setText(contactMessage);

        //Créer un toast annonçant un clic
        final Toast toast = Toast.makeText(getApplicationContext(),"Il y a eu un clic",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);

        //Gérer les écouteurs/listeners des boutons
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast.show();
                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Créer un Intent ramenant à la MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}