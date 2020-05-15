/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;


import com.codename1.components.ScaleImageLabel;
import com.codename1.facebook.FaceBookAccess;
import com.codename1.facebook.User;
import com.codename1.io.Storage;
import com.codename1.social.FacebookConnect;
import com.codename1.social.Login;
import com.codename1.social.LoginCallback;
import com.codename1.ui.Button;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import java.io.IOException;

/**
 * GUI builder created Form
 *
 * @author asus
 */
public class UserForm extends com.codename1.ui.Form {

    public UserForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public UserForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        showFormElements();
    }


    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Connexion avec Facebook");
        setName("UserForm");
    }


    private void showFormElements() {
        this.setScrollable(false);
        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        showData(this);
    }

    private void showData(UserForm form) {
        String token = (String) Storage.getInstance().readObject("token");
        if(token == null || token.equals("")){
            showIfNotLoggedIn(form);
        } else {
            showIfLoggedIn(form);
        }
    }

    private void showIfNotLoggedIn(UserForm form) {
        try {
            form.getContentPane().removeAll();
            Storage.getInstance().writeObject("token", "");
            
            ScaleImageLabel myPic = new ScaleImageLabel();
            Image img = Image.createImage("/anonimo.jpg");
            myPic.setIcon(img);
            Dimension d = new Dimension(50, 50);
            myPic.setPreferredSize(d);
            
            form.add(myPic);
            
            form.add(new Label("Vous n\'etes pas encore connecté"));
            
            Button buttonLogin = new Button("Se connecter");
            buttonLogin.addActionListener((e) -> {
                facebookLogin(form);
            });
            form.add(buttonLogin);
            
            form.revalidate();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void showIfLoggedIn(UserForm form) {
        String token = (String) Storage.getInstance().readObject("token");
        FaceBookAccess.setToken(token);
            final User me = new User();
            try {
                FaceBookAccess.getInstance().getUser("me", me, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        String miNombre = me.getName();
                        
                        form.getContentPane().removeAll();
                        
                        form.add(new Label(miNombre));
                        
                        Button buttonLogout = new Button("Deconnexion");
                        buttonLogout.addActionListener((e) -> {
                            facebookLogout(form);
                            showIfNotLoggedIn(form);
                        });

                        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(50, 50, 0xffff0000), true);
                        URLImage background = URLImage.createToStorage(placeholder, "fbuser.jpg", //wms.png
                                "https://graph.facebook.com/v2.11/me/picture?access_token=" + token);
                        background.fetch();
                        ScaleImageLabel myPic = new ScaleImageLabel();
                        myPic.setIcon(background);
                        
                        form.add(myPic);
                        form.add(buttonLogout);
                        
                        form.revalidate();
                        System.out.println("khedmet hamdelah aleha");
                        //form.show();
                    }

                    
                });
            } catch (IOException ex) {
                ex.printStackTrace();
                showIfNotLoggedIn(form);
            }
    }

    private void facebookLogout(UserForm form) {
        String clientId = "PON TU FACEBOOK APP ID";
        String redirectURI = "http://localhost/"; 
        String clientSecret = "PON TU CONTRASEÑA DE TU APP DE FACEBOOK";
        Login fb = FacebookConnect.getInstance();
        fb.setClientId(clientId);
        fb.setRedirectURI(redirectURI);
        fb.setClientSecret(clientSecret);

        fb.doLogout();
        Storage.getInstance().writeObject("token", "");
        showIfNotLoggedIn(form);
    }
    
    private void facebookLogin(UserForm form) {
        //These are used for the auth web login process on the Simulator.
        String clientId = "PON TU FACEBOOK APP ID";
        String redirectURI = "http://localhost/"; 
        String clientSecret = "PON TU CONTRASEÑA DE TU APP DE FACEBOOK";
        Login fb = FacebookConnect.getInstance();
        fb.setClientId(clientId);
        fb.setRedirectURI(redirectURI);
        fb.setClientSecret(clientSecret);
        
        //Sets a LoginCallback listener
        fb.setCallback(new LoginCallback() {
            @Override
            public void loginFailed(String errorMessage) {
                System.out.println("Connexion échouée");
                Storage.getInstance().writeObject("token", "");
                showIfNotLoggedIn(form);
            }

            @Override
            public void loginSuccessful() {
                System.out.println("Connexion avec succes");
                String token = fb.getAccessToken().getToken();
                Storage.getInstance().writeObject("token", token);
                showIfLoggedIn(form);
            }
            
        });
        if(!fb.isUserLoggedIn()){
            fb.doLogin();
        }else{
            //get the token and now you can query the facebook API
            String token = fb.getAccessToken().getToken();
            Storage.getInstance().writeObject("token", token);
            showIfLoggedIn(form);
        }
    }
}
