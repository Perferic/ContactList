package com.mouffronjoachim.contactlist;
// Gérer les imports de dépendences
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Créer en java les élemnts: une liste, un adapteur de liste, un bouton
        // "Binder" la liste de contact
        // "Binder les bouton de validation
        // "Binder" les checkbox de la liste
        // Créer l'écouteur/listener du bouton
        // Gérer le cas où aucune checkbox n'est sélectionnée
        // Gérer les checkbox sélectionnés
        // Créer un Intent vers l'activité ValidationActivity
        // Créer un payload pour mettre la liste des contacts sélectionnés
    }
}
