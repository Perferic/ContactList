package com.mouffronjoachim.contactlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomContactsAdapter extends ArrayAdapter<Contact>{

    private Context customContext;
    private ArrayList<Contact> contactList;

    //Faire de context non null pour éviter des problèmes d'affichage grace à un décorateur
    //Spécifier avec un décorateur que la liste est créée avec un layout
    public CustomContactsAdapter(@NonNull Context context, ArrayList<Contact> list){
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
                contact.switchChecked();
            }
        });

        TextView name = convertView.findViewById(R.id.itemText);
        name.setText(contact.getName());

        TextView tel = convertView.findViewById(R.id.itemTel);
        tel.setText(contact.getTel());

        return convertView;
    }
}