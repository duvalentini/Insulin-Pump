package com.example.gestalt.insulinpumpulator;

import android.content.Context;

/**
 * Created by Joshua on 6/22/2016.
 */
public class EmptyMenu extends AInsulinPumpMenu {
    public EmptyMenu(AInsulinPumpMenu p, Context c){
        parent = p;
        menuName = c.getResources().getString(R.string.blankmenu);
    }
    public void down(){

    }
    public void up(){

    }
    public AInsulinPumpMenu enter(){
        return null;
    }
}
