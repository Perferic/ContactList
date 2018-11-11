package vu.yawanda.lawawa.yawanda_test;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

public class Voeux extends AppCompatActivity {
    //Variables d'instance
    public String[] originaltext;
    public String[] formatedtext;
    public int id;
    public boolean selection;

    //Créateur

    public Voeux() {
    }

    //Méthodes

    public void setOriginaltext(String[] text) {
        originaltext = text;
    }

    public void setFormatedtext(String[] text) {
        formatedtext = text;
    }

    public void Formattext(String[] prenom) {
        //Création d'une variable temporaire pour insérer la phrase personnalisée des prénoms choisis
        String[] temp = new String[prenom.length];
        for (int i = 0; i < prenom.length; i++) {
            temp[i] = String.format(formatedtext[id], prenom[i]);
        }
        formatedtext = temp;
    }

    public String[] getOriginaltext() {
        return originaltext;
    }

    public String[] getFormatedtext() {
        return formatedtext;
    }
}
