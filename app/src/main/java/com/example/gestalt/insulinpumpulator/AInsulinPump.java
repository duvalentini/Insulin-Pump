package com.example.gestalt.insulinpumpulator;

import java.util.ArrayList;

/**
 * Created by Joshua on 6/18/2016.
 */
public abstract class AInsulinPump {
    protected AInsulinPumpMenu topMenu;
    public AInsulinPumpMenu getTopMenu(){
        return topMenu;
    }
    protected AInsulinPumpMenu currentMenu;
    protected ArrayList<Double> basalRates;
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
    protected double trueCarbFactor;
    protected double trueInsulinFactor;
    protected double bloodSugarCarbRatio;
    //end blood glucose settings
    protected double insulinDelay =30;
    protected double insulinActivate = 30;
    protected boolean suspended;


    public void eatFood(int grams){
        foodgrams+= grams;
    }
    public void setBloodGlucose(int bg){
        bloodGlucose = (double)bg;
    }
    public void vomit(){
        foodgrams =0;
    }
    public boolean exercising = false;
    public void exercise(){
        exercising = !exercising;
    }
    public void passTime(int minutes){

        //recalculate blood glucose, active insulin and foodGrams
        //does not include basal rate at this moment.
        bloodGlucose = bloodGlucose -(trueInsulinFactor*(activeInsulin*((1-Math.pow(2.0,(minutes/(-60.0))/INHL))/(Math.log10(2)/INHL))) - (trueCarbFactor*(foodgrams*(1-Math.pow(2.0,(minutes/(-60.0))*2.5))/(Math.log10(2)*2.5))));//does not include basal rate
        //
        foodgrams = (foodgrams*(Math.pow(2.0,(minutes/(-60.0))/.4)));
        activeInsulin = (activeInsulin*(Math.pow(2.0,(minutes/(-60.0))/INHL)));


        if(!suspended){
            if(tempB){
                bloodGlucose -= (minutes/6.0)*tempBasalPercent;
                tempBasalDuration -= minutes/60.0;
                if(tempBasalDuration<0){
                    tempB = false;
                }
            }
            else{
                bloodGlucose -= minutes/6.0;
            }

        }
        if(exercising){
            bloodGlucose -= minutes/6.0;
        }

        if(timeSinceBolas<insulinActivate+insulinDelay){//if added insulin hasn't taken complete effect yet
            double activeDelay = insulinDelay-timeSinceBolas;

            if (activeDelay<0){
                activeDelay =0;
            }
            double nonDelay = minutes-activeDelay;
            double amountAdded = (minutes-activeDelay)/insulinActivate;
            if(amountAdded>1){
                amountAdded=1;
            }
            double insulinToAdd = amountAdded*addedInsulin;
            addedInsulin = (1-amountAdded)*addedInsulin;

            bloodGlucose = bloodGlucose +(trueInsulinFactor*((insulinToAdd)*((1-Math.pow(2.0,(nonDelay/2)/(-60.0))/INHL))/(Math.log10(2)/INHL)));
            activeInsulin = (activeInsulin+(insulinToAdd)*(Math.pow(2.0,((nonDelay/2)/(-60.0))/INHL)));


        }
        timeSinceBolas+=minutes;//increase the amount of time since the last bolus, since time passed.




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
        bloodSugarCarbRatio = ISEN/ICR;
        bloodGlucose = 110;
        trueInsulinFactor = (ISEN * 1/INHL *Math.log10(2.0))/(1-Math.pow(2.0,-AIT*INHL));
        trueCarbFactor = (bloodSugarCarbRatio *2.5 *Math.log10(2)/(1-Math.pow(2.0,-2.5)));
    }
    public void confirm(){
        AInsulinPumpMenu temp =currentMenu.confirm();
        if(temp!=null){
            currentMenu = temp;
        }
        else {
            currentMenu = topMenu;
            System.out.println("null object returned in confirm.");
        }
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
    public String getMenuName(){
        return currentMenu.getMenuName();
    }

    public ArrayList<String> getSubMenuNames(){
        ArrayList<String> names = new ArrayList<String>();
        if(names!=null) {
            ArrayList<AInsulinPumpMenu> subs = currentMenu.getSubMenus();
            for (int i = 0; i < subs.size(); i++) {
                names.add(subs.get(i).getMenuName());
            }
        }
        return names;
    }

    public void suspend(){
        suspended= !suspended;
    }
    public boolean isSuspended(){
        return suspended;
    }
    public int getSubCurrent(){
        return currentMenu.getCurrent();
    }

}
