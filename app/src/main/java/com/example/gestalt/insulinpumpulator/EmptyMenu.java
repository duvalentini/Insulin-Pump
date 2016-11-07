package com.example.gestalt.insulinpumpulator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Joshua on 6/22/2016.
 */
public class EmptyMenu extends AInsulinPumpMenu {
    public EmptyMenu(AInsulinPumpMenu p, Context c){
        parent = p;
        menuName = c.getResources().getString(R.string.blankmenu);
        subMenus = new ArrayList<AInsulinPumpMenu>();
    }
    public EmptyMenu(AInsulinPumpMenu p, Context c, String name){
        menuName = name;
        parent =p;
        subMenus = new ArrayList<AInsulinPumpMenu>();
    }
    public void setName(String name){
        menuName = name;
    }
    public void down(){

    }
    public void up(){

    }
    public AInsulinPumpMenu confirm(){
        return null;
    }
}
