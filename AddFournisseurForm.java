/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.fournisseur;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
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
public class AddFournisseurForm extends Form {

    public AddFournisseurForm(Form previous) {
        setTitle("Ajouter un nouveau fournisseur");
        setLayout(BoxLayout.y());

        TextField tfNomSociete = new TextField("", "Nom de la societe");
        TextField tfCin = new TextField("", "Cin du fournisseur");
        TextField tfAdresse = new TextField("", "Adresse du fournisseur");
        TextField tfEmail = new TextField("", "Email du livreur");
        TextField tfTelephone = new TextField("", "Telephone du fournisseur");
        TextField tfFax = new TextField("", "Fax du fournisseur");

        Button btnValider = new Button("Ajouter fournisseur");
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
              
                    try {
                        Fournisseur l = new Fournisseur(Integer.parseInt(tfCin.getText()), tfEmail.getText(), tfAdresse.getText(), Integer.parseInt(tfTelephone.getText()), Integer.parseInt(tfFax.getText()), tfNomSociete.getText());
                        if (ServiceFournisseur.getInstance().addFournisseur(l)) {
                            Dialog.show("Success", "Fournisseur ajouté avec succés","OK",null);
                        } else {
                           Dialog.show("ERREUR", "Echec à l'ajout", "OK", null);
                        }
                    } catch (NumberFormatException e) {
                        Dialog.show("ERREUR", "ça doit être un numero", "OK", null);
                    }

                
            }
        });

        addAll(tfNomSociete, tfCin, tfAdresse, tfTelephone, tfFax, btnValider);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }
}
