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
import com.mycompany.myapp.entities.Fournisseur;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asus
 */
public class ServiceFournisseur {

    public ArrayList<Fournisseur> fournisseurs;
    public static ServiceFournisseur instance = null;
    public boolean resultOK;
    private ConnectionRequest req;
    private ConnectionRequest cr;

    public ServiceFournisseur() {
        req = new ConnectionRequest();
    }

    public static ServiceFournisseur getInstance() {
        if (instance == null) {
            instance = new ServiceFournisseur();
        }
        return instance;
    }

    public boolean addFournisseur(Fournisseur l) {
        String url = "http://localhost/pi1/test1.1/web/app_dev.php/adminMobile/ajoutfournisseur?cin=" + l.getCin() + "&email=" + l.getEmail() + "&adresse=" + l.getAdresse() + "&telephone=" + l.getTelephone() + "&fax=" + l.getFax() + "&nomSociete=" + l.getNomSociete() + "";
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

    public boolean updateFournisseur(Fournisseur l) {
        System.out.println("fournisseur:   " + l);
        String url = "http://localhost/pi1/test1.1/web/app_dev.php/adminMobile/updatefr?id=2&cin=122&email=zzz&adresse=aa&telephone=25895&fax=444&nomSociete=ppp";        
        req.setUrl(url);
        //System.out.println("url: "+url);
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

    public ArrayList<Fournisseur> parseFournisseurs(String jsonText) {
        try {
            System.out.println("jdontext: " + jsonText);
            fournisseurs = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> fournisseursListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) fournisseursListJson.get("root");
            for (Map<String, Object> obj : list) {
                Fournisseur t = new Fournisseur();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int) id);
                t.setCin(((int) Float.parseFloat(obj.get("cin").toString())));
                t.setEmail(obj.get("email").toString());
                t.setAdresse(obj.get("adresse").toString());
                t.setTelephone(((int) Float.parseFloat(obj.get("telephone").toString())));
                t.setFax(((int) Float.parseFloat(obj.get("fax").toString())));
                t.setNomSociete(obj.get("nomSociete").toString());
                fournisseurs.add(t);
            }
        } catch (IOException ex) {
        }
        return fournisseurs;
    }

    public ArrayList<Fournisseur> getAllFournisseurs() {
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
                fournisseurs = parseFournisseurs(res);
                System.out.println("bbb :" + fournisseurs);

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return fournisseurs;
    }

}
