/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui.livreur;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Livreur;
import com.mycompany.myapp.services.ServiceLivreur;
import java.util.ArrayList;

/**
 *
 * @author Asus
 */
public class ListLivreursForm extends Form {

    Form current;

    public ListLivreursForm(Form previous) {

        current = this;
        setTitle("Liste des livreurs");

        SpanLabel sp = new SpanLabel();
        ArrayList<Livreur> livreurs;
        System.out.println("hello hanene");
        livreurs = ServiceLivreur.getInstance().getAllLivreurs();
        System.out.println(livreurs);

        for (Livreur fer : livreurs) {
            Container c1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            Label id = new Label("" + fer.getId());
            Label nom = new Label("" + fer.getNom());
            Label prenom = new Label("" + fer.getPrenom());
            Label ville = new Label("" + fer.getVille());
            Label telephone = new Label("" + fer.getTelephone());

            c1.add(id);
            c1.add(nom);
            c1.add(prenom);
            c1.add(ville);
            c1.add(telephone);
            add(c1);
            
            id.addPointerPressedListener(e -> new UpdateLivreurForm(current, Integer.parseInt(id.getText()), nom.getText(), prenom.getText(), ville.getText(), Integer.parseInt(telephone.getText())));
        }
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }

}
