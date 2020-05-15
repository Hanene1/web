/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.InfiniteContainer;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.EventDispatcher;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Webservice tutorial showing off a faux dog picture set and the ability to
 * browse thru images from the network using ImageViewer.
 *
 * @author Shai Almog
 */
public class Articles extends Demo {

    private static final String WEBSERVICE_URL = "https://www.codenameone.com/files/kitchensink/dogs/list2.json";
    private EncodedImage placeholder;

    public String getDisplayName() {
        return "Articles";
    }

    public Image getDemoIcon() {
        //return getResources().getImage("dog.jpg");
        Image imageAuthentif = null;
        try {
            imageAuthentif = Image.createImage("/articles.png");
            return imageAuthentif;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return imageAuthentif;
    }

    @Override
    public String getDescription() {
        return "Les articles de notre warehouse sont affichés dans cette rubrique.";
    }

    @Override
    public String getSourceCodeURL() {
        return "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/WebServices.java";
    }

    /**
     * List model for the image viewer that fetches full sized versions of the
     * images. For the sake of simplicity this model doesn't invoke the
     * webservice and uses the existing webservice data!
     */
    class ImageListModel implements ListModel<Image> {

        private List allItems;
        private int selection;
        private HashMap<String, Image> cache = new HashMap<>();
        private EventDispatcher listeners = new EventDispatcher();
        private EventDispatcher selectionListener = new EventDispatcher();

        public ImageListModel(List allItems, int selection) {
            this.allItems = allItems;
            this.selection = selection;
        }

        @Override
        public Image getItemAt(int index) {
            Map<String, Object> m = (Map<String, Object>) allItems.get(index);
            String url = (String) m.get("url");
            String filename = "fullsize-" + url.substring(url.lastIndexOf("/") + 1);
            if (existsInFileSystem(getAppHomePath() + filename)) {
                Image i = cache.get(filename);
                if (i != null) {
                    return i;
                }
                try {
                    i = Image.createImage(getAppHomePath() + filename);
                    cache.put(filename, i);
                    return i;
                } catch (IOException err) {
                    Log.e(err);
                }
            }
            if (cache.get(filename) == null) {
                ConnectionRequest cn = new ConnectionRequest(url);
                cn.setPost(false);
                cn.downloadImageToFileSystem(getAppHomePath() + filename, i -> {
                    cache.put(filename, i);
                    listeners.fireDataChangeEvent(index, DataChangedListener.CHANGED);
                });
                cache.put(filename, placeholder);
            }
            return placeholder;
        }

        @Override
        public int getSize() {
            return allItems.size();
        }

        @Override
        public int getSelectedIndex() {
            return selection;
        }

        @Override
        public void setSelectedIndex(int index) {
            if (index != selection) {
                int o = selection;
                selection = index;
                selectionListener.fireSelectionEvent(o, index);
            }
        }

        @Override
        public void addDataChangedListener(DataChangedListener l) {
            listeners.addListener(l);
        }

        @Override
        public void removeDataChangedListener(DataChangedListener l) {
            listeners.removeListener(l);
        }

        @Override
        public void addSelectionListener(SelectionListener l) {
            selectionListener.addListener(l);
        }

        @Override
        public void removeSelectionListener(SelectionListener l) {
            selectionListener.removeListener(l);
        }

        @Override
        public void addItem(Image item) {
        }

        @Override
        public void removeItem(int index) {
        }
    }

    public Container createDemo() {
        if (placeholder == null) {
            Image placeholderTmp = getResources().getImage("dog.jpg");
            if (Display.getInstance().isGaussianBlurSupported()) {
                placeholderTmp = Display.getInstance().gaussianBlurImage(placeholderTmp, 10);
            } else {
                placeholderTmp = placeholderTmp.modifyAlpha((byte) 100);
            }
            placeholder = EncodedImage.createFromImage(placeholderTmp, true);
        }
        InfiniteContainer ic = new InfiniteContainer(10) {
            List items;
            List allItems = new ArrayList();
            String nextURL = WEBSERVICE_URL;

            @Override
            public Component[] fetchComponents(int index, int amount) {
                // pull to refresh resets the position
                if (index == 0) {
                    nextURL = WEBSERVICE_URL;
                }
                // downloaded all the content from the webservice
                if (nextURL == null) {
                    return null;
                }

                ConnectionRequest req = new ConnectionRequest(nextURL) {
                    @Override
                    protected void readResponse(InputStream input) throws IOException {
                        items = null;
                        JSONParser parser = new JSONParser();
                        Map response = parser.parseJSON(new InputStreamReader(input, "UTF-8"));
                        items = (List) response.get("items");
                        nextURL = (String) response.get("nextPage");
                    }

                    @Override
                    protected void handleException(Exception err) {
                        Log.e(err);
                        callSerially(() -> {
                            ToastBar.showErrorMessage("An error occured while connecting to the server: " + err);
                        });
                    }

                    @Override
                    protected void handleErrorResponseCode(int code, String message) {
                        callSerially(() -> {
                            ToastBar.showErrorMessage("Error code from the server: " + code + "\n" + message);
                        });
                    }

                };
                req.setPost(false);
                addToQueueAndWait(req);

                if (items == null) {
                    return null;
                }

                // used by the list model for the image viewer
                allItems.addAll(items);

                Component[] result = new Component[items.size()];

                for (int iter = 0; iter < result.length; iter++) {
                    Map<String, Object> m = (Map<String, Object>) items.get(iter);
                    String title = (String) m.get("title");
                    String details = (String) m.get("details");
                    String url = (String) m.get("url");
                    String thumb = (String) m.get("thumb");
                    URLImage thumbImage = URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), thumb, URLImage.RESIZE_SCALE_TO_FILL);
                    ScaleImageButton btn = new ScaleImageButton(thumbImage);
                    btn.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
                    Label titleLabel = new Label(title, "TintOverlay");
                    result[iter] = LayeredLayout.encloseIn(btn,
                            BorderLayout.south(titleLabel));

                    btn.addActionListener(e -> {
                        int offset = btn.getParent().getParent().getComponentIndex(btn.getParent());
                        ImageListModel imlm = new ImageListModel(allItems, offset);
                        Form viewer = new Form(title + " - " + (offset + 1) + " of " + imlm.getSize(), new BorderLayout());
                        Image img = (Image) imlm.getItemAt(offset);
                        if (img == placeholder) {
                            img = thumbImage;
                        }
                        ImageViewer v = new ImageViewer(img);
                        v.setImageList(imlm);
                        SpanLabel desc = new SpanLabel(details);
                        desc.setTextUIID("TintOverlaySmall");
                        desc.setUIID("Container");
                        imlm.addSelectionListener((o, n) -> {
                            Map<String, Object> mm = (Map<String, Object>) allItems.get(n);
                            desc.setText((String) mm.get("details"));
                            viewer.setTitle((String) mm.get("title") + " - " + (n + 1) + " of " + imlm.getSize());
                        });
                        viewer.add(CENTER,
                                LayeredLayout.encloseIn(v, BorderLayout.south(desc)));
                        viewer.getToolbar().setBackCommand("", ee -> btn.getComponentForm().showBack());
                        viewer.show();
                    });
                }
                Layout l = getLayout();
                if (l instanceof GridLayout) {
                    int cmps = getComponentCount() - 1 + result.length;
                    int extra = 0;
                    if (cmps % 3 != 0) {
                        extra = 1;
                    }
                    setLayout(new GridLayout(cmps / 3 + extra, 3));
                }
                return result;
            }
        };

        if (isTablet()) {
            GridLayout gl = new GridLayout(3);
            ic.setLayout(gl);
        }

        return ic;
    }

}
