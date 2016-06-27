package com.example.gestalt.insulinpumpulator;

/**
 * Created by Joshua on 6/18/2016.
 */
public abstract class AInsulinPump {
    protected AInsulinPumpMenu topMenu;
    public AInsulinPumpMenu getTopMenu(){
        return topMenu;
    }
    protected AInsulinPumpMenu currentMenu;
    protected double ICR;//insulin carb ratio
    protected double ISEN;//insulin Sensitivity
    protected int lowGluc;//low glucose level
    protected int highGluc;//high glucose level
    protected double AIT;//active insulin time
    protected double INHL;//insulin half life
    //temp basal settings
    protected boolean tempB;
    protected int tempBasalPercent;
    protected double tempBasalDuration;
    public void setBasalPercent(int a){
        tempBasalPercent = a;
    }
    public void setBasalDuration(double hours){
        tempBasalDuration = hours;
    }
    public void activateTempBasal(){
        tempB = true;
    }
    public void deactivateTempBasal(){
        tempB = false;
    }
    //end temp basal settings

    //Bolas Settings
    protected int bolasBG;
    protected int gramsCarbs;
    protected double manualBolusUnits;
    public void setBolasBG(int bG){
        bolasBG = bG;
    }
    public void setGramsCarbs(int gc){
        gramsCarbs =gc;
    }
    public void setBolusUnits(double bU){
        manualBolusUnits = bU;
    }
    //end Bolas Settings
    //Blood Glucose settings
    protected double bloodGlucose;
    protected double activeInsulin;//insulin currently in blood
    protected double addedInsulin;//insulin from bolas that hasn't been added to blood, is added in 2 things, half at 15 mins, half at 30 mins
    protected double foodgrams;
    protected double timeSinceBolas;
    //end blood glucose settings

    protected boolean suspended;

    public void eatFood(int grams){
        foodgrams+= grams;
    }
    public void vomit(){
        foodgrams =0;
    }
    public void passTime(int minutes){
        boolean addInsulin = false;
        //recalculate blood glucose, active insulin and foodGrams
        if(timeSinceBolas<30){
            addInsulin = true;
            if(minutes>(30-timeSinceBolas)){//if time passed greater than total time for insulin to be added, dump all left
                activeInsulin += addedInsulin;
                addedInsulin =0;
            }
            else{//else, calculate the amount needed to add and subtract what is added and update variables
                activeInsulin += (addedInsulin*(minutes/(30-timeSinceBolas)));
                addedInsulin -=((addedInsulin*(minutes/(30-timeSinceBolas))));//subtract what was added
            }

        }
        timeSinceBolas+=minutes;//increase the amount of time since the last bolus, since time passed.

        if(!addInsulin){//if no added insulin, calc half life
            activeInsulin = activeInsulin*(Math.pow(.5,(minutes/INHL)));
        }


    }
    public void manualBolas(){
        //use manual bolas settings to effect active insulin
        addedInsulin +=manualBolusUnits;
        timeSinceBolas =0;
    }
    public void bolasWiz(){
        //bolas wiz settings
        double total=0;
        if(bolasBG-highGluc>0){
            total += (bolasBG-highGluc)/ISEN;
        }
        if(gramsCarbs>0){
            total += gramsCarbs/ICR;
        }
        addedInsulin+=total;
        timeSinceBolas=0;

    }

    public AInsulinPump(){

    }
    public AInsulinPump(int carbRatio, int sensitive, int lowGC, int highGC, int active){
        ICR = carbRatio;
        ISEN = sensitive;
        lowGluc = lowGC;
        highGluc = highGC;
        AIT = active;
        tempB = false;
        INHL = (AIT-.5)/5;
        suspended = false;

    }
    public void confirm(){
        currentMenu.confirm();
    }
    public void back(){
        AInsulinPumpMenu alpha = currentMenu.back();
        if(alpha!=null){
            currentMenu = alpha;
        }
    }
    public void up(){
        currentMenu.up();
    }
    public void down(){
        currentMenu.down();
    }

    public void suspend(){
        suspended= !suspended;
    }
    public boolean isSuspended(){
        return suspended;
    }

}
