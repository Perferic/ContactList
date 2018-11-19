package com.mouffronjoachim.contactlist;

//Gérer les dépendances
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class ValidationActivity extends AppCompatActivity {
    //Créer les éléments : 2 boutons, une zone de texte/une liste
    //et un message à envoyer aux contacts sélectionnés, un input pour le message
    Button sendButton, cancelButton, modifyButton;
    TextView contactsText;
    ArrayList<String> messageCustom = new ArrayList<>();
    RecyclerView scrollCustom;
    String messageToSend = "";

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
        messageCustom.add("<Prenom>, tu me manques ...");
        messageCustom.add("Commment Ca va <Prenom>?Ca fait un bail!");
        messageCustom.add("<Prenom>, il faut qu'on se parle ...");
        messageCustom.add("Si tu n'envoie pas ce message à 3 autres contact <Prenom>, tu perdras la vie au bout de deux jours !");
        messageCustom.add("Go Fortnite <Prenom>!");
        messageCustom.add("T'as pas liké mon insta <Prenom> !Dépêche toi avant que je perde tout mon clout !");

        //"Binder" les boutons de validation et d'anulation
        sendButton = findViewById(R.id.bouton_validation);
        //Cacher le bouton d'envoi
        cancelButton = findViewById(R.id.bouton_annulation);
        contactsText = findViewById(R.id.wishList);
        modifyButton = findViewById(R.id.modifierPrenom);

        //Récupérer les contacts de l'intent de MainActivity
        final ArrayList<Contact> sentContacts = (ArrayList<Contact>) getIntent().getSerializableExtra("contacts");

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

        //Gerer les evenements de toucher de la scrollview
        scrollCustom.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                //On récupère l'evenement de detection de mouvement et ses coordonnées
                View ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    //on récupère la position et on définit le message choisi
                    int position = recyclerView.getChildAdapterPosition(ChildView);
                    messageToSend = messageCustom.get(position);
                    sendButton.setVisibility(View.VISIBLE);
                }
                //On renvoie faux pour suivre l'implémentation originale
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
        //Montrer le bouton d'envoi

        //Gérer les écouteurs/listeners des boutons
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Créer un boite de dialogue pour valider l'envoi
                AlertDialog.Builder builder = new AlertDialog.Builder(ValidationActivity.this, R.style.dialog);
                builder
                    .setMessage(R.string.dialog_message + " " + messageToSend)
                    .setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //On charge le texte choisi et on l'envoi dans l'intent pour envoyer des sms
                            for(Contact contact: sentContacts) {
                                String message = messageToSend.replaceAll("<Prenom>", contact.getName() );
                                launchActivity(message, contact.getTel());
                            }
                        }
                    });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Gerer le bouton pour changer le prenom
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ModifyActivity.class);
                intent.putExtra("contacts", sentContacts);
                startActivity(intent);
            }
        });

        //Gerer le bouton pour revenir a l'activité principale
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Créer un Intent ramenant à la MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void launchActivity(String message, String num){
        /*
        SmsManager smsManager = SmsManager.getDefault();

        try {
            smsManager.sendTextMessage
                    (num, null, message, null, null);
        }catch(Exception e) {
            Log.d("Nima" , e.getMessage());
        }

        */
        /* PArtager avec choix d'applis
        ShareCompat.IntentBuilder
                .from(this)
                .setType("vnd.android-dir/mms-sms")
                .setChooserTitle("Envoyer SMS avec:")
                .setText(message)
                .startChooser();
        */
        String tel = "sms:" + num;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.setData(Uri.parse(tel));
        intent.putExtra("sms_body", message);
        if(intent.resolveActivity(getPackageManager()) != null){
            try {
                startActivity(Intent.createChooser(intent,"Envoyer SMS"));
            } catch (Exception anfe) {
                Toast.makeText(getApplicationContext(),"Le sms n'a pas été lançé", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Le sms n'a pas été lançé", Toast.LENGTH_SHORT).show();
        }

    }
}