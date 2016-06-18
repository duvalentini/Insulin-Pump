package com.example.gestalt.insulinpumpulator;

import java.util.ArrayList;

/**
 * Created by Joshua on 6/18/2016.
 */
public abstract class AInsulinPumpMenu {

    protected ArrayList<AInsulinPumpMenu> subMenus;
    protected int current;
    protected AInsulinPumpMenu parent;
    protected AInsulinPump pump;

   public AInsulinPumpMenu confirm(){
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



}
