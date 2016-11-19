package com.example.gestalt.insulinpumpulator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodHomeMenu extends OmniPodMenu{
    public OmniPodHomeMenu(AInsulinPump pu, Context c) {
        pump = pu;
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "Bolus"));
        subMenus.add(new EmptyMenu(this, c, "More Actions"));
        subMenus.add(new EmptyMenu(this, c, "Temp basal"));
        subMenus.add(new EmptyMenu(this, c, "My records"));
        subMenus.add(new EmptyMenu(this, c, "Settings"));
        subMenus.add(new EmptyMenu(this, c, "Suspend"));
        button1Text = "Status";
        button2Text = " ";
        button3Text = "Select";
        current = 0;//default highlighted
        con = c;
        menuName = (c.getResources().getString(R.string.mainmenu));
    }
}


