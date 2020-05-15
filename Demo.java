package com.mycompany.myapp;

import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.util.Resources;

/**
 * The demo class is the base class for all the demos in the application
 *
 * @author Shai Almog
 */
public abstract class Demo {
    private Resources res;
    public void init(Resources res) {
        this.res = res;
    }
    
    protected Resources getResources() {
        return res;
    }
    
    public abstract String getDisplayName();
    public abstract Image getDemoIcon();
    public Container createDemo() {
        throw new RuntimeException("Must override createDemo");
    }
    
    public String getDescription() {
        return "";
    }
    
    public String getSourceCodeURL() {
        return "https://www.codenameone.com/";
    }
    
    public Container createDemo(Form parentForm) {
        return createDemo();
    }

    boolean onBack() {
        return true;
    }
}