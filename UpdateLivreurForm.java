/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.livreur;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Livreur;
import com.mycompany.myapp.services.ServiceLivreur;

/**
 *
 * @author Asus
 */
public class UpdateLivreurForm extends Form {

    Form current;

    public UpdateLivreurForm(Form previous, int id, String name, String pren, String vill, int tel) {
        current = this;
        System.out.println("nom: " + name);
        TextField nom = new TextField(name);
        TextField prenom = new TextField(pren);
        TextField ville = new TextField(vill);
        TextField telephone = new TextField(tel);

        Button btnUpadte = new Button("update");
        Container c1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        add(nom);
        add(prenom);
        add(ville);
        add(telephone);
        add(btnUpadte);

        btnUpadte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Livreur lv = new Livreur(id, name, pren, vill, tel);
                if (ServiceLivreur.getInstance().updateLivreur(lv)) {
                    Dialog.show("Success", "Modification avec succes", "OK", null);
                } else {
                    Dialog.show("ERROR", "Modification echouee", "OK", null);
                }

            }
        });
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, ee -> previous.showBack());

    }

}
