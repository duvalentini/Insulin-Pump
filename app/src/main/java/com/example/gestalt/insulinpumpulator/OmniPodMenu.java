package com.example.gestalt.insulinpumpulator;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodMenu extends AInsulinPumpMenu {
    protected String button1Text;
    protected String button2Text;
    protected String button3Text;

    public AInsulinPumpMenu button1() {
        return back();
    }

    public AInsulinPumpMenu button2() {
        return this;
    }

    public AInsulinPumpMenu button3() {
        return confirm();
    }
}
