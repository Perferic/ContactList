package com.mouffronjoachim.contactlist;
// Gérer les imports de dépendences
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
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

    // Forcer les permissions durant l'exécution
    // cf. https://stackoverflow.com/a/37808706/4698318
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    // Tag pour le debug via Log.v()
    private static final String TAG = "MainActivity";

    /**
     * Ce qu'il se passe à la création de l'activité
     *
     * @param savedInstanceState
     */
    
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
    
        // On lance le process de récupération des contacts
        showContacts();

        // Création de l'adapter qui va convertir l'array en view
        final CustomContactsAdapter adapter = new CustomContactsAdapter(this, arrayOfContacts);
        // Je lie l'adapter à ma Listview
        final ListView listView = (ListView) findViewById(R.id.lvContacts);
        listView.setAdapter(adapter);

        // On place un "listener" pour écouter l'évènement click sur un élément de la ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // au clic sur un item, on modifie l'état de la
                // proriété is_checked du contact courant
                adapter.getItem(position).changeIsChecked();   // Récupération de l'objet
            }
        });


        // On crée le bouton "Valider"
        Button bouton_1 = (Button) findViewById(R.id.Bouton_1);
        // On y place un "listener" sur l'évènement click
        bouton_1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // au clic, on vérifie si on
                // peut passer à l'étape suivante
                test_checked(v);
            }
        });

        // On crée le bouton "Selectionner Tout"
        final Button selectall = (Button) findViewById(R.id.selectall);
        // On y place un "listener" sur l'évènement click
        selectall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // On coche toutes les checkboxes
                for (int i = 0; i < adapter.getCount(); i++) {
                    listView.setItemChecked(i, true);
                }
                // On met la propriété is_checked de tous les contacts à true
                for (int i = 0; i < arrayOfContacts.size(); i++) { //On modifie la variable des contacts
                    arrayOfContacts.get(i).setIsChecked(true);
                }

                //Récupère toutes les checkboxes de la listview qui sont cochées
                SparseBooleanArray checked = listView.getCheckedItemPositions();

            }
        });

    }

    /**
     * Afficher les contacts dans la ListView
     */
    private void showContacts() {
        // Contrôle de la version du SDK et si j'ai la permission d'accéder aux Contacts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Si je n'ai pas la permission, je la demande...
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            // ...Puis je reprend l'activité
            onRefresh();
        } else {
            // J'ai la permission adéquate, je cherche et récupère mes contacts

            // J'initialise l'objet getContentResolver() pour pouvoir interroger Android
            ContentResolver cr = getContentResolver();
            // J'interroge Android pour récupérer la globalité des contacts,
            // les résultats sont stockées dans un curseur qu'il va falloir parcourir
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            // On commence à parcourir le curseur à partir de la première position
            if (cursor.moveToFirst()) {
                // Effectuer la recherches des informations...
                do {
                    // J'instancie un nouveau contact
                    Contact contact = new Contact();
                    String phone;
                    // je récupère le "DISPLAY_NAME"
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // je récupère son ID également afin de pouvoir chercher le numéro de téléphone par défaut
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    // je donne un nom au contact courant
                    contact.setNom(name);
                    // si un numéro de téléphone existe
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        // je fais une nouvelle requête afin de récupéré le premier numéro de téléphone disponible
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        // et cela passe par un nouveau curseur
                        while (pCur.moveToNext()) {
                            phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // on donne au contact le numéro de téléphone
                            contact.setTel(phone);
                        }
                        // on ferme le curseur (numéro de tél)
                        pCur.close();
                    }
                    // on ajoute ce nouveau contact à l'ArrayList de contacts
                    arrayOfContacts.add(contact);
                    //... tant que le curseur peut passer à la position suivante
                } while (cursor.moveToNext());
            }
            // on ferme le curseur (contacts du téléphone)
            cursor.close();
        }
    }

    /**
     * Si nous n'avons pas les permissions nécesaire on les demande,
     * et alors on peut passer à la suite
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // si le numéro de request est égal à l'int qu'on a définit dans cette classe
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            // et si la permission à été donnée par l'user
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // alors on retente la recherche des contacts
                showContacts();
            } else {
                // sinon, on explique pourquoi
                Toast.makeText(this, "Autoriser l'acces à vos contact", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Si au moins un contact a été coché, on peut passer à l'activité suivante
     *
     * @param view
     */
    public void test_checked(View view) {
        int checkedcontrol = 0;
        // on parcourt la liste ds contacts
        for (int i = 0; i < arrayOfContacts.size(); i++) {
            // si le contact courant est is_checked == true
            if (arrayOfContacts.get(i).getIsChecked()) {
                // alors on incrémente notre var de contrôle
                checkedcontrol++;
            }
        }
        // si la var de contrôle est différente de 0
        if (checkedcontrol != 0) {
            // on passe à l'activité suivante
            nouvelleActivite(view);
        } else {
            // sinon on en informe l'user
            ToastNoChecked(view);
        }
    }

    /**
     * Si aucune contact n'a été coché, on en informe l'user avec un Toast
     * @param view
     */
    public void ToastNoChecked(View view) {
        Toast.makeText(getApplicationContext(), "Aucun contact n'est sélectionné.", Toast.LENGTH_SHORT).show();
    }

    /**
     * On crée l'Intent qui permet de passer à l'activité suivante
     * @param view
     */
    public void nouvelleActivite(View view) {
        Intent startNewActivity = new Intent(this, SecondActivity.class);
        // On instancie une nouvelle ArrayList qui va contenir les contacts cochés
        ArrayList<Contact> checkedcontact = new ArrayList<Contact>();
        // on parcours notre ArrayList de contacts complets
        for (int i = 0; i < arrayOfContacts.size(); i++) {
            // si le contact courant est coché
            if (arrayOfContacts.get(i).getIsChecked()) {
                // on l'ajoute au nouvel arrayList
                checkedcontact.add(arrayOfContacts.get(i));
            }
        }
        // on envoie l'arrayList des contacts cochés à la seconde activité
        startNewActivity.putParcelableArrayListExtra("Contacts", checkedcontact);
        // et on appelle la seconde activité
        startActivity(startNewActivity);
    }

    /**
     * Après check des permissions, on continue l'activité en cours
     */
    protected void onRefresh() {
        super.onResume();
    }
}

