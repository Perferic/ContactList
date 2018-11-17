package com.mouffronjoachim.contactlist;

//Gérer les dépendances
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ValidationActivity extends AppCompatActivity {
    //Créer les éléments : 2 boutons, une zone de texte/une liste
    //et un message à envoyer aux contacts sélectionnés, un input pour le message
    Button sendButton, cancelButton;
    TextView contactsText;
    ArrayList<String> messageCustom = new ArrayList<>();
    RecyclerView scrollCustom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        //Remplir messageCustom les possibles messages à envoyer
        messageCustom.add("<Prenom>, tu me manques ...");
        messageCustom.add("Commment Ca va <Prenom>?Ca fait un bail!");
        messageCustom.add("<Prenom>, il faut qu'on se parle ...");
        messageCustom.add("Si tu n'envoie pas ce message à 3 autres contact <Prenom>, tu perdras la vie au bout de deux jours !");
        messageCustom.add("Go Fortnite <Prenom>!");
        messageCustom.add("T'as pas liké mon insta <Prenom> !Dépêche toi avant que je perde tout mon clout !");

        //"Binder" les boutons de validation et d'anulation
        sendButton = findViewById(R.id.bouton_validation);
        cancelButton = findViewById(R.id.bouton_annulation);
        contactsText = findViewById(R.id.wishList);

        //Récupérer les contacts de l'intent de MainActivity
        Intent intentResponse = getIntent();
        ArrayList<Contact> sentContacts = (ArrayList<Contact>) intentResponse.getSerializableExtra("contacts");
        //Remplir contactsText des contacts
        StringBuilder contactMessage = new StringBuilder();
        for(Contact contact: sentContacts){
            contactMessage.append(contact);
            contactMessage.append("\n");
        }
        contactsText.setText(contactMessage);

        //Remplir la scrollCustom des customMessages
        scrollCustom = this.findViewById(R.id.scrollCustom);
        scrollCustom.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter adapter = new RecyclerAdapter(this, messageCustom);
        scrollCustom.setAdapter(adapter);

        //Montrer le bouton d'envoi


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