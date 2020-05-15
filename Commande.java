/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

import java.util.Date;

/**
 *
 * @author Asus
 */
public class Commande {
    private int id_commande, user, num_commande, montant;
    private Date date_commande, date_livraison;
    private String type, etat;

    public Commande() {
    }

    public Commande(int id_commande, int user, int num_commande, int montant, Date date_commande, Date date_livraison, String type, String etat) {
        this.id_commande = id_commande;
        this.user = user;
        this.num_commande = num_commande;
        this.montant = montant;
        this.date_commande = date_commande;
        this.date_livraison = date_livraison;
        this.type = type;
        this.etat = etat;
    }
    
    public Commande(int user, int num_commande, int montant, Date date_commande, Date date_livraison, String type, String etat) {
        this.user = user;
        this.num_commande = num_commande;
        this.montant = montant;
        this.date_commande = date_commande;
        this.date_livraison = date_livraison;
        this.type = type;
        this.etat = etat;
    }

    public int getId_commande() {
        return id_commande;
    }

    public void setId_commande(int id_commande) {
        this.id_commande = id_commande;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getNum_commande() {
        return num_commande;
    }

    public void setNum_commande(int num_commande) {
        this.num_commande = num_commande;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public Date getDate_commande() {
        return date_commande;
    }

    public void setDate_commande(Date date_commande) {
        this.date_commande = date_commande;
    }

    public Date getDate_livraison() {
        return date_livraison;
    }

    public void setDate_livraison(Date date_livraison) {
        this.date_livraison = date_livraison;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Commande{" + "id_commande=" + id_commande + ", user=" + user + ", num_commande=" + num_commande + ", montant=" + montant + ", date_commande=" + date_commande + ", date_livraison=" + date_livraison + ", type=" + type + ", etat=" + etat + '}';
    }
    
}
