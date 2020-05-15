/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.fournisseur;

import com.codename1.components.ImageViewer;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.mycompany.myapp.MyApplication;
import java.io.IOException;

/**
 *
 * @author Asus
 */
public class FournisseurForm extends Form {

    Form current;

    public FournisseurForm() {
        current = this;
        setTitle("Gestion des fournisseurs");
        setLayout(BoxLayout.y());

        add(new Label("Veuillez choisir une option"));
        Button btnListFournisseurs = new Button("Afficher les fournisseurs");
        Button btnAddFournisseur = new Button("Ajouter un fournisseur");
        //Button btnUpdateFournisseur = new Button("Modifier un fournisseur");
        Button btnDeleteFournisseur = new Button("Supprimer un fournisseur");

        btnAddFournisseur.addActionListener(e -> new AddFournisseurForm(current).show());
        btnListFournisseurs.addActionListener(e -> new ListFournisseursForm(current).show());

        add(btnAddFournisseur);
        add(btnListFournisseurs);
        //add(btnUpdateFournisseur);
        add(btnDeleteFournisseur);

        // getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

}
