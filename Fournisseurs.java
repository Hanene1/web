/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.table.TableLayout;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Form;
import com.mycompany.myapp.gui.fournisseur.FournisseurForm;

/**
 * Demonstrates some of the basic layout types available in Codename One with
 * explanation and a smooth animation
 *
 * @author Shai Almog
 */
public class Fournisseurs extends Demo {

    public String getDisplayName() {
        return "Fournisseurs";
    }

    public Image getDemoIcon() {
        return getResources().getImage("layout.png");
    }

    @Override
    public String getSourceCodeURL() {
        return "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/Layouts.java";
    }

    private void resetMargin(Container c) {
        for (Component cc : c) {
            cc.setUIID(cc.getUIID());
        }
    }

    public Container createDemo() {
        Container cn = new Container();
        Button fournisseurs = new Button("fournisseurs");
        fournisseurs.addActionListener(e -> new FournisseurForm().show());
        cn.add(fournisseurs);
        return cn;
    }

    @Override
    public String getDescription() {
        return "La gestion des fournisseurs: cette rubrique permet d'ajouter afficher modifier et supprimer un fournisseur";
    }
}
