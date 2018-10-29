package com.mouffronjoachim.contactlist;
// Gérer les dépendences
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        // Créer les éléments en java : 2 boutons, une zone de texte/une liste
        // "Binder" les boutons de validation et d'anulation
        // Gérer les écouteurs/listeners des boutons
        // Créer un toast annonçant un clic
        // Créer un Intent ramenant à la MainActivity
        // Créer une zone de texte pour afficher les contacts sélectionnés
    }
}
