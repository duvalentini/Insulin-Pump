package com.example.gestalt.insulinpumpulator;

import java.util.ArrayList;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodMenu extends AInsulinPumpMenu {
    protected String button1Text;
    protected String button2Text;
    protected String button3Text;

    public ArrayList<String> getButtonText() {
        ArrayList<String> text = new ArrayList<String>();
        text.add(button1Text);
        text.add(button2Text);
        text.add(button3Text);

        return text;
    }

    public OmniPodMenu button1() {
        return (OmniPodMenu)back();
    }

    public OmniPodMenu button2() {
        return this;
    }

    public OmniPodMenu button3() {
        return (OmniPodMenu)confirm();
    }
}
