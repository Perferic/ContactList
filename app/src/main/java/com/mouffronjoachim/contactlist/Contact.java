package com.mouffronjoachim.contactlist;

import java.io.Serializable;

public class Contact implements Serializable {
    private Boolean active, changeNom;
    private String name, tel;

    public Contact(String name, String tel){
        this.active = false;
        this.name = name;
        this.tel = tel;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name){ this.name = name; }
    public String getTel() {
        return this.tel;
    }

    public boolean getChecked() {
        return this.active;
    }

    public void switchChecked() {
        this.active = !this.active;
    }
    public void switchChecked(Boolean bool) {
        this.active = bool;
    }

    public boolean isChangeNom(){
        return this.changeNom;
    }


    @Override
    public String toString() {
        return this.name+' '+this.tel;
    }
}

