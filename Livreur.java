/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

/**
 *
 * @author Asus
 */
public class Livreur {
    private int id, telephone;
    private String nom, prenom, ville;

    public Livreur() {
    }

    public Livreur(int id, String nom, String prenom, String ville, int telephone) {
        this.id = id;
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
    }
    
    public Livreur(String nom, String prenom, String ville, int telephone) {
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "Livreur{" + "id=" + id + ", telephone=" + telephone + ", nom=" + nom + ", prenom=" + prenom + ", ville=" + ville + '}';
    }
    
    
    
}
