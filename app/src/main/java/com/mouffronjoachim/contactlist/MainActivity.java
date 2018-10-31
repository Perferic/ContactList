package com.mouffronjoachim.contactlist;
//TODO: Gérer les imports de dépendances
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //TODO: Initialiser les éléments
    Button  selectionButton;
    ListView contactList;
    CustomArrayAdapter contactAdapter;
    ArrayList<Contact> contacts = new ArrayList<Contact>(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: Créer en java les élements: une liste, un adapteur de liste, un bouton
        contacts.add(new Contact("Quentin","0123456789"));
        contacts.add(new Contact("Joachim","0123456789"));
        contacts.add(new Contact("Lawawa","0123456789"));
        contacts.add(new Contact("Jacques","0123456789"));
        contacts.add(new Contact("Thomas","0123456789"));
        //TODO: "Binder le bouton de validation
        selectionButton = findViewById(R.id.selectionButton);
        //TODO: "Binder" la liste de contact
        contactList = findViewById(R.id.contactListFinal);
        contactAdapter = new CustomArrayAdapter(this, contacts);
        //TODO: "Binder" les checkbox de la liste
        final ArrayList<Contact> selectedContacts = new ArrayList<>();
        contactList.setAdapter(contactAdapter);

        //TODO: Créer l'écouteur/listener du bouton
        selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Créer un Intent vers l'activité ValidationActivity
                Intent intent = new Intent(getApplicationContext(), ValidationActivity.class);
                //TODO: Créer un payload pour mettre la liste des contacts sélectionnés
                intent.putExtra("contacts", (Serializable)selectedContacts);
                startActivity(intent);
            }
        });
        //TODO: Créer l'écouteur/listener des checkboxes
    }
}

//TODO: Créer une classe Contact
//On va créer des objets Contact pour remplir la ListView
class Contact{
    public boolean active;
    public String name;
    public String tel;

    public Contact(String name, String tel){
        this.name = name;
        this.tel = tel;
        this.active = false;
    }

    public String getName() {
        return this.name;
    }

    public String getTel() {
        return this.tel;
    }
    public boolean getActive(){
        return this.active;
    }

    public void switchActive() {
        this.active = !this.active;
    }
}

//TODO: Créer un Adapter spécifique pour inclure des Contacts
//On fait hériter notre adapter pour gérer les objets Contact
class CustomArrayAdapter extends ArrayAdapter<Contact>{

    private Context customContext;
    private ArrayList<Contact> contactList;

    //Faire de context non null pour éviter des problèmes d'affichage grace à un décorateur
    //Spécifier avec un décorateur que la liste est créée avec un layout
    public CustomArrayAdapter(@NonNull Context context, ArrayList<Contact> list){
        super(context, 0, list);
        this.customContext = context;
        this.contactList = list;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Contact getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(customContext).inflate(R.layout.custom_view_list,parent,false);
        }
        final Contact contact = contactList.get(position);

        CheckBox check = convertView.findViewById(R.id.item_checkbox);
        //TODO: Gérer les checkbox sélectionnés
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.switchActive();
            }
        });

        TextView name = convertView.findViewById(R.id.itemText);
        name.setText(contact.getName());

        TextView tel = convertView.findViewById(R.id.itemTel);
        tel.setText(contact.getTel());

        return convertView;
    }
}
