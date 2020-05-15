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
public class Fournisseur {
    public int id, cin, telephone,fax;
    public String email, adresse, nomSociete; 

    public Fournisseur() {
    }

    public Fournisseur(int id, int cin, String email, String adresse, int telephone, int fax, String nomSociete) {
        this.id = id;
        this.cin = cin;
        this.telephone = telephone;
        this.fax = fax;
        this.email = email;
        this.adresse = adresse;
        this.nomSociete = nomSociete;
    }
    
    public Fournisseur(int cin, String email, String adresse, int telephone, int fax, String nomSociete) {
        this.cin = cin;
        this.telephone = telephone;
        this.fax = fax;
        this.email = email;
        this.adresse = adresse;
        this.nomSociete = nomSociete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public int getFax() {
        return fax;
    }

    public void setFax(int fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    @Override
    public String toString() {
        return "Fournisseur{" + "id=" + id + ", cin=" + cin + ", telephone=" + telephone + ", fax=" + fax + ", email=" + email + ", adresse=" + adresse + ", nomSociete=" + nomSociete + '}';
    }    
    
    
}
