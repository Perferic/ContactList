package com.mouffronjoachim.contactlist;
// Gérer les dépendences
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ValidationActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_SEND_SMS = 100 ;
    String phoneNo;
    String message;
    ArrayList<Contact> contacts = new ArrayList<Contact>();
    final Voeux voeux = new Voeux(); //Création de l'objet
    String[] list_voeux_perso = new String[contacts.size()];
    
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
        
        //Récupération des contacts et affichage des noms
        contacts = getIntent().getExtras().getParcelableArrayList("Contacts");
        TextView retourcontact = (TextView) findViewById(R.id.RecapContact);
        String ListNoms;
        if(contacts == null)
        {
            ListNoms = "Rien de sélectionné.";
        }
        else {
            ListNoms = "Sélectionnés : ";
            for (int i = 0; i < contacts.size(); i++) {
                ListNoms += contacts.get(i).getNom() + "; ";
            }
        }
        retourcontact.setText(ListNoms);


        //Préparation de la listview pour les phrases de voeux
        final ListView listViewWish = (ListView) findViewById(R.id.wishlist);

        //Initialisation de l'objet Voeux, la fonction getResources() ne fonctionne pas dans la classe Voeux.java malgré l'import donc on est obligé de le faire manuellement ici
        Resources res = getResources();
        String[] originaltext = res.getStringArray(R.array.phrases_voeux_vierge);
        final String[] toformattext = res.getStringArray(R.array.phrases_voeux_a_remplir);
        voeux.setOriginaltext(originaltext); //Remplissage de l'objet avec les phrase prédéfinies vierges
        voeux.setFormatedtext(toformattext);
        final String[] listvoeux = voeux.getOriginaltext(); //Remplissage d'un array avec la liste des phrases

        //Affichage de l'array dans la listview
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listvoeux);
        listViewWish.setAdapter(adapter);

        listViewWish.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                if(position != (listvoeux.length -1)) {
                    long[] checkeditem = listViewWish.getCheckedItemIds(); //Fonction qui bloque le choix sur une seule phrase
                    if (checkeditem != null) {
                        for (int i = 0; i < checkeditem.length; i++) {
                            listViewWish.setItemChecked((int) checkeditem[i], false);
                        }
                    }
                    listViewWish.setItemChecked(position, true);
                    voeux.id = position;
                    voeux.selection = true;
                }
                else {
                    voeux.selection = false;
                }
            }
        });

        Button validation = (Button) findViewById (R.id.Boutton);
        Button changename = (Button) findViewById(R.id.changename);
        Button annulation = (Button) findViewById(R.id.bouton_annulation);

      // Le bouton envoi de message :
        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voeux.selection) {
                    String[] prenom = new String[contacts.size()]; //On récupère les noms de la liste de contact dans un tableau
                    for (int i = 0; i < contacts.size(); i++) {
                        prenom[i] = contacts.get(i).getNom();
                    }
                    voeux.setFormatedtext(toformattext);
                    voeux.Formattext(prenom); //On initialise la phrase choisie avec le(s) prénom(s)
                    list_voeux_perso = voeux.getFormatedtext(); //On récupère le tableau obtenu ci-dessus
                    confirmationEnvoi(v);
                }
                else {
                    Builder builder = new Builder(ValidationActivity.this);
                    // Titre de l'alertdialog
                    builder.setTitle("Attention");
                    // Message de l'alertdialog
                    builder.setMessage("Vous n'avez pas sélectionné de phrase.");
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),
                                    "Veuillez sélectionner une phrase",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();
                }
            }
        });

      //Le Bouton de changement de nom
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nouvelleActivite(v);
            }
        });

      // Le Bouton annulation :
        annulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourActivite();
            }
        });

    }

    // Méthodes bouton validation :
    public void confirmationEnvoi (final View view) {

        Builder builder = new Builder(ValidationActivity.this);
        // Titre de l'alertdialog
        builder.setTitle("Confirmation envoi");
        // Message de l'alertdialog
        builder.setMessage("Souhaitez-vous envoyer ce(s) message(s) ?");
        // Pour le bouton "non"
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),
                        "Message(s) non-envoyé(s)",
                        Toast.LENGTH_LONG).show();
            }
        });
        // Pour le bouton "oui"
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendSMSMessage();
            }
        });
        builder.show();
    }

    protected void sendSMSMessage() {
        // Contrôle de la version du SDK et si j'ai la permission d'envoi de sms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            for (int i = 0; i < contacts.size(); i++) {
                phoneNo = contacts.get(i).getTel();
                message = list_voeux_perso[i];
                String SMS_SENT = "SMS Envoyé";
                String SMS_DELIVERED = "SMS Reçu";

                PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
                PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);


                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(context, "Envoi du SMS réussi", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(context, "Erreur générique", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(context, "Service inaccessible", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(context, "Pas de PDU renseigné", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(context, "Réseau hors ligne", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SMS_SENT));


                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS envoyé", Toast.LENGTH_SHORT).show();
                                retourActivite();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getBaseContext(), "SMS non envoyé", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SMS_DELIVERED));


                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(phoneNo, null, message, sentPendingIntent, deliveredPendingIntent);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSMessage();
            } else {
                Toast.makeText(this, "Autoriser l'acces à l'envoi SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void nouvelleActivite(View view) {
        Intent startNewActivity = new Intent(this, ChangeNameActivity.class);
        startNewActivity.putParcelableArrayListExtra("Contacts", contacts);
        startActivity(startNewActivity);
    }

    // Méthode bouton annulation :
    public void retourActivite() {
        Intent backToOldActivity = new Intent(this, MainActivity.class);
        startActivity(backToOldActivity);
    }

}

