package com.example.gestalt.insulinpumpulator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Joshua on 6/18/2016.
 */
public abstract class AInsulinPumpMenu {

    protected ArrayList<AInsulinPumpMenu> subMenus;
    protected int current;
    protected AInsulinPumpMenu parent;
    protected AInsulinPump pump;
    protected String menuName;
    protected Context con;
    protected double enterField;
    public double getEnterField(){
        return enterField;
    }

   public AInsulinPumpMenu confirm(){
       AInsulinPumpMenu curr = subMenus.get(current);
       if(curr instanceof EmptyMenu) {
           return this;
       }
       return subMenus.get(current);
   }

    public AInsulinPumpMenu(){
        current = -1;
    }
    public AInsulinPumpMenu(AInsulinPumpMenu par){
        parent = par;
    }

    public void up(){
        if(current>0){
            current --;
        }
    }
    public void down() {
        if (current < subMenus.size() - 1) {
            current++;
        }
    }
    public AInsulinPumpMenu back(){
        return parent;
    }

    public String getMenuName(){
        return menuName;
    }
    public AInsulinPumpMenu enter(){
        return subMenus.get(current);
    }
    public ArrayList<AInsulinPumpMenu> getSubMenus(){
        return subMenus;
    }
    public int getCurrent(){
        return current;
    }
}
