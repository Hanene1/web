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
public class utilisateur {
    private int id, telephone;
    private String  nom, prenom, adresse,email, grade, username, password;

    public utilisateur() {
    }

    public utilisateur(int id, int telephone, String nom, String prenom, String adresse, String email, String grade, String username, String password) {
        this.id = id;
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.grade = grade;
        this.username = username;
        this.password = password;
    }
    
    public utilisateur(String nom, String prenom, String adresse, int telephone, String email, String grade, String username, String password) {
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.grade = grade;
        this.username = username;
        this.password = password;
    }
    
    public utilisateur(String nom, String prenom, String adresse, int telephone, String email, String username, String password) {
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    public utilisateur (String username, String password){
        this.username = username;
        this.password = password;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "fos_user{" + "id=" + id + ", telephone=" + telephone + ", nom=" + nom + ", prenom=" + prenom + ", adresse=" + adresse + ", email=" + email + ", grade=" + grade + ", username=" + username + ", password=" + password + '}';
    }
    
}