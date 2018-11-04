package com.ue236.deust.ue236;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Creat
    ListView mListView;
    Button mButton;
    String[] prenoms = new String[]{
            "Joachim", "Yawuanda", "Robert", "Quentin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //pointeur vers list_layout
        mListView = (ListView) findViewById(R.id.list_layout);

        //création d'un ArrayAdapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, prenoms);

        //Intégration dans liste
        mListView.setAdapter(adapter);

        //TODO: Système de checkbox pour importer dans ValidationActivity

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création  Intent vers l'activité ValidationActivity
                Intent intent = new Intent(getApplicationContext(), ValidationActivity.class);
                //Creation  payload pour mettre la liste des contacts sélectionnés
                intent.putExtra("contacts", (Serializable)selectedContacts);
                startActivity(intent);
            }
        });
    }
}

/*
class Contacts {
    public String nom;
    public int numTel;

    public Contacts(String nom, int numTel){
        this.nom = nom;
        this.numTel = numTel;
    }
}

*/

