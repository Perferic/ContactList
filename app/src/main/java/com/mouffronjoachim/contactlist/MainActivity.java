package com.mouffronjoachim.contactlist;
// Gérer les imports de dépendences
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // ArrayList qui contiendra les contacts lus
    private ArrayList<Contact> arrayOfContacts = new ArrayList<>();
    //Constante pour les permissions de lecture
    final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    // On instancie une nouvelle ArrayList qui va contenir les contacts cochés
    ArrayList<Contact> checkedContacts = new ArrayList<>();
    ListView listView;

    private static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On lance le process de récupération des contacts
        showContacts();

        // Création de l'adapter qui va convertir l'array en view
        final CustomContactsAdapter adapter = new CustomContactsAdapter(this, arrayOfContacts);
        // On lie l'adapter à ma Listview
        listView = findViewById(R.id.lvContacts);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        // On place un "listener" pour écouter l'évènement click sur un élément de la ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // au clic sur un item, on modifie l'état de la propriété is_checked du contact courant
                Log.d(TAG, checkedContacts.toString());
                Contact contact = (Contact) parent.getItemAtPosition(position);
                // Récupération du contact
                if (contact.getChecked()){
                    listView.setItemChecked(position, true);
                    arrayOfContacts.get(position).switchChecked(true);
                    checkedContacts.add(contact);
                }else{
                    listView.setItemChecked(position, false);
                    arrayOfContacts.get(position).switchChecked(false);
                    checkedContacts.remove(contact);
                }
            }
        });

        // On crée le bouton pour valider les contacts
        Button boutonValiderMain = findViewById(R.id.valider);
        // On y place un "listener" sur l'évènement click
        boutonValiderMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // au clic, on vérifie si on peut passer à l'étape suivante
                Log.d(TAG, checkedContacts.toString());
                test_checked(v);
            }
        });

        // On crée le bouton "Selectionner Tout"
        final Button boutonSelectAll = findViewById(R.id.selectall);

        // On y place un "listener" sur l'évènement click
        boutonSelectAll.setOnClickListener(new OnClickListener() {
            boolean count = false;
            @Override
            public void onClick(View v) {
                // On coche toutes les checkboxes
                count = !count;
                if(count) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        // On met la propriété is_checked de tous les contacts à true
                        //On modifie la variable des contacts
                        listView.setItemChecked(i, true);
                        arrayOfContacts.get(i).switchChecked(true);
                        checkedContacts.add(arrayOfContacts.get(i));
                    }
                }else{
                    for (int i = 0; i < adapter.getCount(); i++) {
                        // On met la propriété is_checked de tous les contacts à true
                        //On modifie la variable des contacts
                        listView.setItemChecked(i, false);
                        arrayOfContacts.get(i).switchChecked(true);
                        checkedContacts.remove(arrayOfContacts.get(i));
                    }
                }
            }
        });

    }

    private void showContacts() {
        // Contrôle de la version du SDK et si j'ai la permission d'accéder aux Contacts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Si je n'ai pas la permission, je la demande...
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }else {
            // J'ai la permission adéquate, je cherche et récupère mes contacts
            // J'initialise l'objet getContentResolver() pour pouvoir interroger Android
            ContentResolver cr = getContentResolver();
            // J'interroge Android pour récupérer la globalité des contacts,
            // les résultats sont stockées dans un curseur qu'il va falloir parcourir
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            // On commence à parcourir le curseur à partir de la première position
            if (cursor.moveToFirst()) {
                // Effectuer la recherche des informations...

                do {
                    String phone;
                    // je récupère le "DISPLAY_NAME"
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // je récupère son ID également afin de pouvoir chercher le numéro de téléphone par défaut
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    // si un numéro de téléphone existe
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        // je fais une nouvelle requête afin de récupérer le premier numéro de téléphone disponible
                        // et cela passe par un nouveau curseur
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            // on donne au contact le numéro de téléphone
                            String tel = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Contact contact = new Contact(name, tel);
                            // on ajoute ce nouveau contact à l'ArrayList de contacts
                            arrayOfContacts.add(contact);
                        }
                        // on ferme le curseur (numéro de tél)
                        pCur.close();
                    }
                    //... tant que le curseur peut passer à la position suivante
                } while (cursor.moveToNext());
            }
            // on ferme le curseur (contacts du téléphone)
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // si le numéro de request est égal à l'int qu'on a défini dans cette classe
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            // et si la permission à été donnée par l'user
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // alors on retente la recherche des contacts
                showContacts();
            } else {
                // sinon, on explique pourquoi
                Toast.makeText(this, "Pour utiliser l'application il faut autoriser l'acces à vos contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void test_checked(View view) {
        // On initialise un compteur
        int checkControl = 0;
        // on parcourt la liste ds contacts
        for (int i = 0; i < arrayOfContacts.size(); i++) {
            // si le contact courant est coché
            if (arrayOfContacts.get(i).getChecked()) {
                // alors on incrémente notre compteur
                checkControl++;
            }
        }
        // si le compteur est différent de 0
        if (checkControl != 0) {
            // on peut passer à l'activité suivante
            nouvelleActivite(view);
        } else {
            // sinon on en informe l'utilisateur
            ToastNoChecked(view);
        }
    }

    public void ToastNoChecked(View view) {
        Toast.makeText(getApplicationContext(), "Aucun contact n'est sélectionné.", Toast.LENGTH_SHORT).show();
    }

    public void nouvelleActivite(View view) {
        Intent startNewActivity = new Intent(this, ValidationActivity.class);
        Log.d(TAG, checkedContacts.toString());
        for(Contact contact:arrayOfContacts){
            if(contact.getChecked()){
                checkedContacts.add(contact);
            }
        }
        // on envoie l'arrayList des contacts cochés à la seconde activité
        startNewActivity.putExtra("contacts", checkedContacts);
        // et on appelle la seconde activité
        startActivity(startNewActivity);
    }
}

