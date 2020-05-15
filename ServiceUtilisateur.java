/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.utilisateur;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asus
 */
public class ServiceUtilisateur {

    public ArrayList<utilisateur> utilisateurs;
    public static ServiceUtilisateur instance = null;
    public boolean resultOK;
    private ConnectionRequest req;
    private ConnectionRequest cr;

    public ServiceUtilisateur() {
        req = new ConnectionRequest();
    }

    public static ServiceUtilisateur getInstance() {
        if (instance == null) {
            instance = new ServiceUtilisateur();
        }
        return instance;
    }

    public boolean addUtilisateur(utilisateur l) {
        String url = "http://localhost/pi1/test1.1/web/app_dev.php/user/ajoututilisateur?nom=" + l.getNom() + "&prenom=" + l.getPrenom() + "&adresse=" + l.getAdresse() + "&telephone=" + l.getTelephone() + "&email=" + l.getEmail() + "&grade=Client" + "&username=" + l.getUsername() + "&password=" + l.getPassword() + "";
//http://localhost/pi1/test1.1/web/app_dev.php/user/ajoututilisateur?nom=q&prenom=s&adresse=d&telephone=9&email=f&grade=user&username=11&password=11

        req.setUrl(url);
        System.out.println("url: " + url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public ArrayList<utilisateur> parseUtilisateurs(String jsonText) {
        try {
            System.out.println("jdontext: " + jsonText);
            utilisateurs = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> utilisateursListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) utilisateursListJson.get("root");
            for (Map<String, Object> obj : list) {
                utilisateur t = new utilisateur();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int) id);
                t.setNom(obj.get("nom").toString());
                t.setPrenom(obj.get("prenom").toString());
                t.setAdresse(obj.get("adresse").toString());
                t.setTelephone(((int) Float.parseFloat(obj.get("telephone").toString())));
                t.setEmail(obj.get("email").toString());
                t.setGrade(obj.get("grade").toString());
                t.setUsername(obj.get("username").toString());
                t.setPassword(obj.get("password").toString());
                utilisateurs.add(t);
            }
        } catch (IOException ex) {
        }
        return utilisateurs;
    }

    public ArrayList<utilisateur> getAllUtilisateurs() {
        String url = "http://localhost/pi1/test1.1/web/app_dev.php/adminMobile/showfournisseur";
        req.setUrl(url);
        System.out.println("cr: " + req.getUrl());

        req.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {
                // System.out.println("hello omaa jmai ");
                String res = new String(req.getResponseData());
                System.out.println("resultats: " + res);
                System.out.println(res);
                utilisateurs = parseUtilisateurs(res);
                System.out.println("bbb :" + utilisateurs);

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return utilisateurs;
    }
    
   
    public static utilisateur login(String usernam, String pwd) {
        String req = "SELECT * from utilisateur where username='" + usernam + "' and password='" + pwd + "'";
        utilisateur u = new utilisateur(usernam,pwd);
        
        return u;
    }

    
    
//    public static utilisateur getUser(String usernam) throws SQLException {
//       
//        utilisateur u = new utilisateur();
//        ResultSet rs=null;
//
//        while (rs.next()) {
//
//            String nom = rs.getString("nom");
//            String prenom = rs.getString("prenom");
//            String adresse = rs.getString("adresse");
//            int tel = rs.getInt("telephone");  
//            String email = rs.getString("email");
//            String grade = rs.getString("grade");
//            String username = rs.getString("username");
//            String password = rs.getString("password");
//            utilisateur uu = new utilisateur(nom, prenom, adresse, tel, email, grade, username, password);
//
//            u = uu;
//        }
//        return u;
//    }
    

}
