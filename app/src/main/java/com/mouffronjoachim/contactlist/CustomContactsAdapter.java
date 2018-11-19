package com.mouffronjoachim.contactlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomContactsAdapter extends ArrayAdapter<Contact>{

    private Context customContext;
    private ArrayList<Contact> contactList = new ArrayList<>();

    //Faire de context non null pour éviter des problèmes d'affichage grace à un décorateur
    //Spécifier avec un décorateur que la liste est créée avec un layout
    public CustomContactsAdapter(@NonNull Context context, @NonNull ArrayList<Contact> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(customContext).inflate(R.layout.custom_view_list,parent,false);
        }
        final Contact contact = contactList.get(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox check = view.findViewById(R.id.item_checkbox);
                //TODO: Gérer les checkbox sélectionnés
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        if(isChecked) {
                            buttonView.setChecked(true);
                        }else{
                            buttonView.setChecked(false);
                        }
                    }
                });
                contact.switchChecked();
                contactList.get(position).getChecked();
            }
        });

        TextView name = convertView.findViewById(R.id.itemText);
        name.setText(contact.getName());

        TextView tel = convertView.findViewById(R.id.itemTel);
        tel.setText(contact.getTel());

        return convertView;
    }
}