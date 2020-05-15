package com.mycompany.myapp;

import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.Log;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import static com.codename1.ui.CN.*;
import com.codename1.ui.ComboBox;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.animations.ComponentAnimation;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import com.mycompany.myapp.entities.utilisateur;
import java.io.IOException;
import com.mycompany.myapp.gui.fournisseur.*;
import com.mycompany.myapp.gui.livreur.LivreurForm;
import com.mycompany.myapp.services.ServiceUtilisateur;

public class MyApplication {

    private Resources res;
    private Form currentForm;
    private Container tabletSurface;
    private Command gridCommand;
    private Command listCommand;

    private Object imageMask;
    private int maskWidth;
    private int maskHeight;
    private Object circleMask;
    private int circleMaskWidth;
    private int circleMaskHeight;

    private int[] colors;
    private Image[] colorBottoms;
    private int currentColor;

    public void init(Object context) {
        // use 2 network threads for slightly faster networking but not too much to overburden the UI
        updateNetworkThreadCount(2);
        res = UIManager.initFirstTheme("/theme");
        Toolbar.setGlobalToolbar(true);
        if (!isTablet()) {
            Toolbar.setOnTopSideMenu(true);
        }
        Dialog.setDefaultBlurBackgroundRadius(10);
        Log.bindCrashProtection(false);
    }

    private void showDemoInformation(Form back, Demo d) {
        Form f = new Form("Information", new BorderLayout());
        Button sourceCode = new Button("View Source");
        FontImage.setMaterialIcon(sourceCode, FontImage.MATERIAL_WEB);
        sourceCode.addActionListener(e -> execute(d.getSourceCodeURL()));
        f.add(CENTER, new SpanLabel(d.getDescription())).
                add(SOUTH, sourceCode);
        f.getToolbar().setBackCommand("", e -> back.showBack());
        f.show();
    }

    private DemoComponent createDemoButton(Demo d) {
        DemoComponent dc = new DemoComponent(d.getDisplayName(), d.getDemoIcon(), imageMask,
                maskWidth, maskHeight, colorBottoms[currentColor],
                circleMask, res.getImage("circle-line.png"), circleMaskWidth, circleMaskHeight,
                "Blank" + (currentColor + 1));
        currentColor++;
        if (currentColor == colorBottoms.length) {
            currentColor = 0;
        }
        dc.addActionListener(e -> {
            if (isTablet()) {
                tabletSurface.getAnimationManager().flushAnimation(() -> {
                    tabletSurface.replace(tabletSurface.getComponentAt(0), d.createDemo(getCurrentForm()),
                            CommonTransitions.createCover(CommonTransitions.SLIDE_HORIZONTAL, true, 200));
                });
            } else {
                Form previous = getCurrentForm();
                Form f = new Form(d.getDisplayName(), new BorderLayout());
                f.add(CENTER, d.createDemo(f));
                f.getToolbar().setBackCommand(" ", ee -> {
                    if (d.onBack()) {
                        previous.showBack();
                    }
                });
                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_INFO, 4, ee -> {
                    showDemoInformation(f, d);
                });
                f.show();
            }
        });
        return dc;
    }

    private void showSplashAnimation() {
        Form splash = new Form(new LayeredLayout());
        splash.setUIID("Splash");
        splash.getContentPane().setUIID("Container");
        splash.getToolbar().setUIID("Container");
        ScaleImageLabel iconBackground = new ScaleImageLabel(res.getImage("codenameone-icon-background.png"));
        iconBackground.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Container centerBackground = BorderLayout.center(iconBackground);
        splash.add(centerBackground);
        Label iconForeground = new Label(res.getImage("codenameone-icon-foreground.png"));
        Container centerIcon = BorderLayout.centerAbsolute(iconForeground);
        splash.add(centerIcon);

        splash.show();
        callSerially(() -> {
            ((BorderLayout) centerBackground.getLayout()).setCenterBehavior(CENTER_BEHAVIOR_CENTER_ABSOLUTE);
            centerBackground.setShouldCalcPreferredSize(true);
            centerBackground.animateLayoutAndWait(350);

            iconForeground.remove();
            iconBackground.remove();
            centerIcon.remove();
            Container layers = LayeredLayout.encloseIn(
                    new Label(iconBackground.getIcon(), "CenterIcon"),
                    new Label(iconForeground.getIcon(), "CenterIcon"));
            Container boxy = BoxLayout.encloseY(layers);
            Label placeholder = new Label();
            placeholder.setShowEvenIfBlank(true);
            Label smartTruck = new Label("Smart Truck", "SplashTitle");
            Component.setSameHeight(placeholder, smartTruck);
            Component.setSameWidth(placeholder, smartTruck, boxy);
            centerBackground.add(CENTER, boxy);
            splash.revalidate();
            callSerially(() -> {
                placeholder.setText(" ");
                boxy.add(placeholder);
                boxy.setShouldCalcPreferredSize(true);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(placeholder, smartTruck, CommonTransitions.createFade(500));

                Label newPlaceholder = new Label(" ");
                Label byCodenameOne = new Label("by Codename One", "SplashSubTitle");
                Component.setSameHeight(newPlaceholder, byCodenameOne);
                Component.setSameWidth(newPlaceholder, byCodenameOne);
                boxy.add(newPlaceholder);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(newPlaceholder, byCodenameOne, CommonTransitions.createFade(500));

                byCodenameOne.setY(splash.getHeight());
                smartTruck.setY(splash.getHeight());
                layers.setY(splash.getHeight());
                boxy.setHeight(splash.getHeight());

                boxy.animateUnlayoutAndWait(450, 20);
                splash.setTransitionOutAnimator(CommonTransitions.createEmpty());

                // create image masks for card effects
                Image mask = res.getImage("card-full.png");
                maskWidth = mask.getWidth();
                maskHeight = mask.getHeight() / 4 * 3;
                Image top = mask.subImage(0, 0, maskWidth, maskHeight, true);
                Image bottom = mask.subImage(0, maskHeight, maskWidth, mask.getHeight() / 4, true);
                imageMask = top.createMask();

                Image circleMaskImage = res.getImage("circle.png");
                circleMask = circleMaskImage.createMask();
                circleMaskWidth = circleMaskImage.getWidth();
                circleMaskHeight = circleMaskImage.getHeight();

                colorBottoms = new Image[7];
                colors = new int[colorBottoms.length];
                Object bottomMask = bottom.createMask();
                for (int iter = 0; iter < colorBottoms.length; iter++) {
                    colors[iter] = splash.getUIManager().getComponentStyle("Blank" + (iter + 1)).getBgColor();
                    colorBottoms[iter] = Image.createImage(bottom.getWidth(), bottom.getHeight(), 0xff000000 | colors[iter]);
                    colorBottoms[iter] = colorBottoms[iter].applyMask(bottomMask);
                }

                showAuth();
            });
        });
    }

    public void start() {
        if (getAppstoreURL() != null) {
            RatingWidget.bindRatingListener(180000, getAppstoreURL(), "apps@codenameone.com");
        }
        if (currentForm != null && !(currentForm instanceof Dialog)) {
            currentForm.show();
            return;
        }
        showSplashAnimation();
    }

    private void showAuth() {

        if (currentForm != null) {
            currentForm.show();
            return;
        }
        Form hi = new Form("Smart Truck", BoxLayout.y());
        Container cn1 = new Container(new FlowLayout(CENTER, CENTER));
        try {
            Image imageAuthentif = Image.createImage("/authentif.png");
            ImageViewer ivAuthentif = new ImageViewer(imageAuthentif);

            cn1.add(ivAuthentif);
            hi.add(cn1);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        TextField nom = new TextField("", "Identifiant");
        TextField prenom = new TextField("", "Mot de passe");
        prenom.setConstraint(TextField.PASSWORD);
        Button btn = new Button("Se connecter");
        Button btnFb = new Button("Se connecter avec Facebook");
        Button btnRegister = new Button("Créez un nouveau compte");

        hi.add(nom);
        hi.add(prenom);
        hi.add(btn);
        hi.add(btnFb);
        hi.add(btnRegister);

        btnFb.addActionListener(e -> new UserForm().show());

        //connect
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ConnectionRequest cr = new ConnectionRequest();
                cr.setUrl("http://localhost/authentif/cnx.php");
                cr.setPost(false);
                cr.addArgument("name", nom.getText());
                cr.addArgument("password", prenom.getText());
                cr.addResponseListener(new ActionListener<NetworkEvent>() {
                    @Override
                    public void actionPerformed(NetworkEvent evt) {
                        String res = new String(cr.getResponseData());
                        if (res.equalsIgnoreCase("error")) {
                            Dialog.show("Error", "Bad credentials", "ok", null);
                            System.out.println(cr);
                        } else {
                            //showMainUI();
                            showMainAdmin();
//                            String id = nom.getText();
//                            String pwd = prenom.getText();
//                            utilisateur f = new utilisateur(id, pwd);
//                            String role = ServiceUtilisateur.getInstance().login(id, pwd).getGrade();
//                            if (role.equals("Client")) {
//                                showMainUI();
//                            } else if (role.equals("Administrateur")) {
//                                showMainAdmin();
//                            }

                        }
                    }
                });
                NetworkManager.getInstance().addToQueue(cr);
            }

        });

        //Register
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Form hi1 = new Form("Créer un nouveau compte", BoxLayout.y());

                TextField tfNom = new TextField("", "Nom", 20, TextField.ANY);
                FontImage.setMaterialIcon(tfNom.getHintLabel(), FontImage.MATERIAL_PERSON);
                TextField tfPrenom = new TextField("", "Prenom", 20, TextField.ANY);
                FontImage.setMaterialIcon(tfPrenom.getHintLabel(), FontImage.MATERIAL_PERSON);
                TextField tfAdresse = new TextField("", "Adresse", 2, 20);
                FontImage.setMaterialIcon(tfAdresse.getHintLabel(), FontImage.MATERIAL_LIBRARY_BOOKS);
                TextField tfTelephone = new TextField("", "Telephone", 8, 20);
                FontImage.setMaterialIcon(tfTelephone.getHintLabel(), FontImage.MATERIAL_LIBRARY_BOOKS);
                TextField tfEmail = new TextField("", "E-mail", 20, TextField.EMAILADDR);
                FontImage.setMaterialIcon(tfEmail.getHintLabel(), FontImage.MATERIAL_EMAIL);
                ComboBox rolesList = new ComboBox("Client", "Administrateur");
                String role = rolesList.getSelectedItem().toString();
                TextField tfUsername = new TextField("", "Identifiant", 20, TextField.ANY);
                FontImage.setMaterialIcon(tfUsername.getHintLabel(), FontImage.MATERIAL_PERSON);
                TextField tfPassword = new TextField("", "Mot de passe", 20, TextField.PASSWORD);
                FontImage.setMaterialIcon(tfPassword.getHintLabel(), FontImage.MATERIAL_LOCK);

                Validator val = new Validator();
                val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID);
                val.addConstraint(tfNom, new LengthConstraint(2, "Name must have at least 2 characters")).
                        addConstraint(tfPrenom, new LengthConstraint(2, "prenom must have at least 2 characters")).
                        addConstraint(tfAdresse, new LengthConstraint(2, "adresse must have at least 2 characters")).
                        addConstraint(tfTelephone, new LengthConstraint(8, "Name must have at least 8 numbers")).
                        addConstraint(tfEmail, RegexConstraint.validEmail("E-Mail must be a valid email address")).
                        addConstraint(tfUsername, new LengthConstraint(2, "Name must have at least 2 charachters ")).
                        addConstraint(tfPassword, new LengthConstraint(6, "Password must have at least 6 characters"));

                Button btnValider = new Button("Créer un nouveau compte");
                btnValider.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        utilisateur f = new utilisateur(tfNom.getText(), tfPrenom.getText(), tfAdresse.getText(), Integer.parseInt(tfTelephone.getText()), tfEmail.getText(), role, tfUsername.getText(), tfPassword.getText());
                        if (ServiceUtilisateur.getInstance().addUtilisateur(f)) {
                            Dialog.show("Success", "Connection accepted", "OK", null);
                            if (f.getGrade().equals("Client")) {
                                showMainUI(); //showMainAdmin
                            } else if (f.getGrade().equals("Administrateur")) {
                                showMainAdmin();
                            }

                        } else {
                            Dialog.show("ERROR", "Server error", "OK", null);
                        }

                    }

                });

                hi1.addAll(tfNom, tfPrenom, tfAdresse, tfTelephone, tfEmail, rolesList, tfUsername, tfPassword, btnValider);
                hi1.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> hi.showBack());
                hi1.show();

            }
        });

        hi.show();

    }

    //menu client
    public void showMainUI() {
        final Form f = new Form("Smart Truck", new BorderLayout());

        Demo[] demos = new Demo[]{
            new Articles(),
            new ClockDemo(),
            new Themes(),
            new Video(),};
//commandes
        for (Demo d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("codenameone-icon-foreground.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Duke");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");

        if (isTablet()) {
            Toolbar.setPermanentSideMenu(true);
            f.getToolbar().addComponentToSideMenu(dukeContainer);
            for (Demo d : demos) {
                f.getToolbar().addComponentToSideMenu(createDemoButton(d));
            }
            tabletSurface = f.getContentPane();
            f.add(CENTER, demos[0].createDemo(f));
            f.show();
            return;
        }

        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if (Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for (Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for (Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        }
        cnt.setScrollableY(true);
        f.add(CENTER, cnt);

        f.getToolbar().addSearchCommand(e -> {
            String t = (String) e.getSource();
            if (t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for (Component c : cnt) {
                DemoComponent mb = (DemoComponent) c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if (cnt.getAnimationManager().isAnimating()) {
                return;
            }
            if (!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();

                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);

                cnt.animateLayout(300);

            }
        });

        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if (!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];

                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if (cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        f.getToolbar().addMaterialCommandToSideMenu("CodenameOne.com",
                FontImage.MATERIAL_WEB, e -> execute("https://www.codenameone.com/"));
        f.getToolbar().addMaterialCommandToSideMenu("Getting Started", FontImage.MATERIAL_WEB, e -> execute("https://www.codenameone.com/"));
        f.getToolbar().addMaterialCommandToSideMenu("Source Code", FontImage.MATERIAL_WEB, e -> execute("https://github.com/codenameone/KitchenSink"));

        if (isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                share("Check out the Smart Truck app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("About",
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("About", "Smart Truck est une application mobile qui a pour rôle de faciliter la gestion d'un entrepot. ", "OK", null);
                });

        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for (Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for (Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());
        f.show();
    }

    //menu administrateur
    public void showMainAdmin() {
        final Form f = new Form("Smart Truck", new BorderLayout());

        Demo[] demos = new Demo[]{
            new Fournisseurs(), new Articles(),
            new Livreurs(), new ClockDemo(),
            new Themes(), new Input(),
            new Video(), new SalesDemo(),};
//articles, commandes, fournisseurs, livreurs, emplacements, allees, travees
        for (Demo d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("codenameone-icon-foreground.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Duke");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");

        if (isTablet()) {
            Toolbar.setPermanentSideMenu(true);
            f.getToolbar().addComponentToSideMenu(dukeContainer);
            for (Demo d : demos) {
                f.getToolbar().addComponentToSideMenu(createDemoButton(d));
            }
            tabletSurface = f.getContentPane();
            f.add(CENTER, demos[0].createDemo(f));
            f.show();
            return;
        }

        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if (Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for (Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for (Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        }
        cnt.setScrollableY(true);
        f.add(CENTER, cnt);

        f.getToolbar().addSearchCommand(e -> {
            String t = (String) e.getSource();
            if (t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for (Component c : cnt) {
                DemoComponent mb = (DemoComponent) c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if (cnt.getAnimationManager().isAnimating()) {
                return;
            }
            if (!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();

                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);

                cnt.animateLayout(300);

            }
        });

        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if (!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];

                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if (cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        f.getToolbar().addMaterialCommandToSideMenu("CodenameOne.com",
                FontImage.MATERIAL_WEB, e -> execute("https://www.codenameone.com/"));
        f.getToolbar().addMaterialCommandToSideMenu("Getting Started", FontImage.MATERIAL_WEB, e -> execute("https://www.codenameone.com/"));
        f.getToolbar().addMaterialCommandToSideMenu("Source Code", FontImage.MATERIAL_WEB, e -> execute("https://github.com/codenameone/KitchenSink"));

        if (isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                share("Check out the Smart Truck app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("About",
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("About", "Smart Truck est une application mobile qui a pour rôle de faciliter la gestion d'un entrepot. ", "OK", null);
                });

        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for (Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for (Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());

        f.show();
    }

    public String getAppstoreURL() {
        if (getPlatformName().equals("ios")) {
            return "https://itunes.apple.com/us/app/kitchen-sink-codename-one/id635048865";
        }
        if (getPlatformName().equals("and")) {
            return "https://play.google.com/store/apps/details?id=com.codename1.demos.kitchen";
        }
        return null;
    }

    public void stop() {
        currentForm = getCurrentForm();
        RatingWidget.suspendRating();
    }

    public void destroy() {
    }
}
