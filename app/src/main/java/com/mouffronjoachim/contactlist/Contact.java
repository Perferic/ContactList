package com.mouffronjoachim.contactlist;

import java.io.Serializable;

public class Contact implements Serializable {
    private Boolean active;
    private String name, tel;

    public Contact(String name, String tel){
        this.active = false;
        this.name = name;
        this.tel = tel;
    }

    public String getName() {
        return this.name;
    }
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


    @Override
    public String toString() {
        return super.toString();
    }
}

