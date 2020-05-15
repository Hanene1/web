/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.fournisseur;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Fournisseur;
import com.mycompany.myapp.services.ServiceFournisseur;
import java.util.ArrayList;

/**
 *
 * @author Asus
 */
public class ListFournisseursForm extends Form {

    Form current;

    public ListFournisseursForm(Form previous) {
        current = this;
        setTitle("Liste des fournissuers");

        SpanLabel sp = new SpanLabel();
        ArrayList<Fournisseur> fournisseurs;
        System.out.println("hello hanene");
        fournisseurs = ServiceFournisseur.getInstance().getAllFournisseurs();
        System.out.println(fournisseurs);

        for (Fournisseur fer : fournisseurs) {
            Container c1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            Label id = new Label("" + fer.getId());
            Label email = new Label("" + fer.getEmail());
            Label adresse = new Label("" + fer.getAdresse());
            Label tel = new Label("" + fer.getTelephone());
            Label nomSociete = new Label("" + fer.getNomSociete());
            Label fax = new Label("" + fer.getFax());
            c1.add(id);
            c1.add(email);
            c1.add(adresse);
            c1.add(nomSociete);
            c1.add(fax);
            add(c1);
            id.addPointerPressedListener(e -> new UpdateFournisseurForm(current, Integer.parseInt(id.getText()), Integer.parseInt(fax.getText()), email.getText(), adresse.getText(), Integer.parseInt(tel.getText()), nomSociete.getText()).show());

        }
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }

}
