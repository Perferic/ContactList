package com.mouffronjoachim.contactlist;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ModifyActivity extends AppCompatActivity {
    //La liste des contacts
    ArrayList<Contact> contacts = new ArrayList<>();
    //Le contact a envoyer à modifier dans PrenomActivity
    Contact contact;
    //Affichage du contact choisi
    TextView contactSelected;
    //Les boutons
    Button bouton_modifier, bouton_annuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        //On binde les boutons
        bouton_modifier = findViewById(R.id.bouton_validation);
        bouton_annuler = findViewById(R.id.bouton_annuler);

        //Récupération des contacts et affichage des noms
        contacts = (ArrayList<Contact>) getIntent().getSerializableExtra("contacts");

        // Création de l'adapter qui va convertir l'array en view
        final CustomContactsAdapter adapter = new CustomContactsAdapter(this, contacts);

        // Je link l'adapter à ma Listview
        final ListView listView = (ListView) findViewById(R.id.lvContactName);
        listView.setAdapter(adapter);

        //On récupère le contact en le touchant
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                contact = (Contact) parent.getItemAtPosition(position);
                //On affiche le nom du contact
                contactSelected.setText("Le contact selectionné est\n"+contact.toString());
            }
        });

        //On passe à l'activité Prenom
        bouton_modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact != null) {
                    modifyActivity();
                }else{
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas choisi de contact à modifier!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //On revient à la validation de l'envoi
        bouton_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivity();
            }
        });
    }

    public void returnActivity() {
        Intent startNewActivity = new Intent(this, ValidationActivity.class);
        startNewActivity.putExtra("contacts", contacts);
        startActivity(startNewActivity);
    }

    public void modifyActivity() {
        Intent startNewActivity = new Intent(this, ModifyActivity.class);
        startNewActivity.putExtra("contacts", contacts);
        startNewActivity.putExtra("contact", contact);
        startActivity(startNewActivity);
    }
}