package com.mouffronjoachim.contactlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class PrenomActivity extends AppCompatActivity {

    ArrayList<Contact> contacts = new ArrayList<>();
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenom);
        //On recupere l'intent avec le contact et la liste de contact à modifier
        contact = (Contact) getIntent().getSerializableExtra("contact");
        contacts = (ArrayList<Contact>) getIntent().getSerializableExtra("contacts");

        //Bind des UI du layout
        Button changeName = findViewById(R.id.buttonvalid_edittext);
        Button annuler = findViewById(R.id.bouton_terminer);
        final EditText getChangedName = findViewById(R.id.getchangename);

        //Créer un listener pour le bouton qui active le changement de nom
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = getChangedName.getText().toString();
                if(newName !=""){
                    int index = contacts.indexOf(contact);
                    contacts.get(index).setName(newName);
                    returnActivity();
                }else{
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas rentré de nouveau prénom!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Le Bouton de changement de nom
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartActivity();
            }
        });
    }

    public void restartActivity() {
        Intent startNewActivity = new Intent(this, ModifyActivity.class);
        startNewActivity.putExtra("contacts", contacts);
        startActivity(startNewActivity);
    }

    public void returnActivity() {
        Intent startNewActivity = new Intent(this, ValidationActivity.class);
        startNewActivity.putExtra("contacts", contacts);
        startActivity(startNewActivity);
    }
}
