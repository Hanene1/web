/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.fournisseur;

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
import com.mycompany.myapp.entities.Fournisseur;
import com.mycompany.myapp.services.ServiceFournisseur;

/**
 *
 * @author Asus
 */
public class UpdateFournisseurForm extends Form {

    Form current;

    public UpdateFournisseurForm(Form previous, int id, int fax, String e, String add, int t, String n) {

        current = this;
        System.out.println("email:  " + previous);
        TextField email = new TextField(e);
        TextField adresse = new TextField(add);
        TextField tel = new TextField("" + t);
        TextField faxx = new TextField("" + fax);
        TextField nomsociete = new TextField(n);
        Button btnUpadte = new Button("update");
        Container c1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        add(email);
        add(adresse);
        add(tel);
        add(faxx);
        add(nomsociete);
        add(btnUpadte);
        btnUpadte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Fournisseur fr = new Fournisseur(id, 3232,e, add, fax, t, n);

                if (ServiceFournisseur.getInstance().updateFournisseur(fr)) {
                    Dialog.show("Success", "Modification avec succes","OK", null);
                }else{
                   Dialog.show("ERREUR", "Modification echouee", "OK", null);
                }

            }
        });
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, ee -> previous.showBack());
    }

}
