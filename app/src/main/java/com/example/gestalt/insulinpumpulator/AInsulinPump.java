package com.example.gestalt.insulinpumpulator;

/**
 * Created by Joshua on 6/18/2016.
 */
public abstract class AInsulinPump {
    private AInsulinPumpMenu topMenu;
    private AInsulinPumpMenu currentMenu;
    private int setting1;//changes names of these
    private int setting2;
    private int setting3;
    public AInsulinPump(){

    }
    public AInsulinPump(int set1, int set2, int set3){
        setting1 = set1;
        setting2 = set2;
        setting3 = set3;

    }
    public void confirm(){
        currentMenu.confirm();
    }
}
