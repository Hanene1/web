/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.livreur;

import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author Asus
 */
public class LivreurForm extends Form {

    Form current;

    public LivreurForm() { //Form previous

        current = this;
        setTitle("Gestion des livreurs");
        setLayout(BoxLayout.y());

        add(new Label("Veuillez choisir une option"));
        Button btnListLivreurs = new Button("Afficher les livreurs");
        Button btnAddLivreur = new Button("Ajouter un livreur");
        // Button btnUpdateLivreur = new Button("Modifier un livreur");
        Button btnDeleteLivreur = new Button("Supprimer un livreur");

        btnAddLivreur.addActionListener(e -> new AddLivreurForm(current).show());
        btnListLivreurs.addActionListener(e -> new ListLivreursForm(current).show());

        add(btnAddLivreur);
        add(btnListLivreurs);
        // add(btnUpdateLivreur);
        add(btnDeleteLivreur);

        //getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }
}
