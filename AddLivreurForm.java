/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.livreur;

import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import com.mycompany.myapp.entities.Livreur;
import com.mycompany.myapp.services.ServiceLivreur;

/**
 *
 * @author Asus
 */
public class AddLivreurForm extends Form {

    public AddLivreurForm(Form previous) {
        setTitle("Ajouter un nouveau livreur");
        setLayout(BoxLayout.y());

        TextField tfNom = new TextField("", "Nom du livreur");
        FontImage.setMaterialIcon(tfNom.getHintLabel(), FontImage.MATERIAL_PERSON);

        TextField tfPrenom = new TextField("", "Prenom du livreur");
        FontImage.setMaterialIcon(tfPrenom.getHintLabel(), FontImage.MATERIAL_PERSON);

        TextField tfVille = new TextField("", "Adresse du livreur");
        FontImage.setMaterialIcon(tfVille.getHintLabel(), FontImage.MATERIAL_PERSON);

        TextField tfTelephone = new TextField("", "Telephone du livreur");
        FontImage.setMaterialIcon(tfTelephone.getHintLabel(), FontImage.MATERIAL_PERSON);

        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID);
        val.addConstraint(tfNom, new LengthConstraint(2, "Nom doit avoir au minimum 2 lettres"))
                .addConstraint(tfPrenom, new LengthConstraint(2, "Prenom doit avoir au minimum 2 lettres"))
                .addConstraint(tfVille, new LengthConstraint(2, "Ville doit avoir au minimum 2 lettres"))
                .addConstraint(tfTelephone, new LengthConstraint(2, "Prenom doit avoir exactement 8 chiffres "));

        Button btnValider = new Button("Ajouter livreur");
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((tfNom.getText().length() == 0) || (tfPrenom.getText().length() == 0) || (tfVille.getText().length() == 0) || (tfTelephone.getText().length() == 0)) {
                    Dialog.show("Alert", "Veuillez remplir tous les champs", "OK", null);
                } else {
                    ToastBar.showMessage("Ajout en cours...", FontImage.MATERIAL_INFO);
                    Livreur l = new Livreur(tfNom.getText(), tfPrenom.getText(), tfVille.getText(), Integer.parseInt(tfTelephone.getText()));
                    if (ServiceLivreur.getInstance().addLivreur(l)) {
                        Dialog.show("Success", "Fournisseur ajouté avec succés", "OK", null);
                    } else {
                        Dialog.show("ERREUR", "Echec à l'ajout", "OK", null);
                    }

                }
            }
        });

        addAll(tfNom, tfPrenom, tfVille, tfTelephone, btnValider);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

}
