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
import com.mycompany.myapp.entities.Livreur;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asus
 */
public class ServiceLivreur {

    public ArrayList<Livreur> livreurs;
    public static ServiceLivreur instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    public ServiceLivreur() {
        req = new ConnectionRequest();
    }

    public static ServiceLivreur getInstance() {
        if (instance == null) {
            instance = new ServiceLivreur();
        }
        return instance;
    }

    public boolean addLivreur(Livreur l) {
        String url = "http://localhost/pi1/test1.1/web/app_dev.php/adminMobile/ajoutlivreur?nom=" + l.getNom() + "&prenom=" + l.getPrenom() + "&ville=" + l.getVille() + "&telephone=" + l.getTelephone() + "";
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

    public ArrayList<Livreur> parseLivreurs(String jsonText) {
        try {
            System.out.println("jdontext: " + jsonText);
            livreurs = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> livreursListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) livreursListJson.get("root");
            for (Map<String, Object> obj : list) {
                Livreur t = new Livreur();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int) id);
                t.setNom(obj.get("nom").toString());
                t.setPrenom(obj.get("prenom").toString());
                t.setVille(obj.get("ville").toString());
                t.setTelephone(((int) Float.parseFloat(obj.get("telephone").toString())));
                livreurs.add(t);
            }
        } catch (IOException ex) {
        }
        return livreurs;
    }

    public ArrayList<Livreur> getAllLivreurs() {
        String url = "http://localhost/pi1/test1.1/web/app_dev.php/adminMobile/showlivreur";
        req.setUrl(url);
        System.out.println("req: " + req.getUrl());
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String res = new String(req.getResponseData());
                System.out.println("resultats: " + res);
                System.out.println(res);
                livreurs = parseLivreurs(res);
                System.out.println("bbb :" + livreurs);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return livreurs;
    }

    public boolean updateLivreur(Livreur lv) {
        System.out.println("livreur:   " + lv);
        String url = "http://localhost/pi1/test1.1/web/app_dev.php/adminMobile/updateliv?id=2&nom=122&prenom=zzz&ville=aa&telephone=25895";
        req.setUrl(url);
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
}
