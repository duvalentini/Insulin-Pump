package com.example.gestalt.insulinpumpulator;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodPump extends AInsulinPump {
    protected boolean useBG;
    protected boolean useCarbs;

    public OmniPodPump(Context c){
        con = c;
        ICR = 15;
        ISEN = 30;
        lowGluc = 70;
        highGluc = 130;
        AIT = 3.25;
        tempB = false;
        INHL = (AIT-.5)/5;
        suspended = false;
        topMenu= new OmniPodHomeMenu(this,c);
        currentMenu = topMenu;
        bloodGlucose = 90;
        bloodSugarCarbRatio = ISEN/ICR;
        trueInsulinFactor = (ISEN / INHL *Math.log10(2.0))/(1.0-Math.pow(2.0,-AIT/INHL));
        trueCarbFactor = (bloodSugarCarbRatio *2.5 *Math.log10(2)/(1.0-Math.pow(2.0,-2.5)));
        exercising = false;
    }
    public OmniPodPump(int carbRatio, int sensitive, int lowGC, int highGC, int active, ArrayList<Double> basals, Context c){
        con = c;
        ICR = carbRatio;
        ISEN = sensitive;
        lowGluc = lowGC;
        highGluc = highGC;
        AIT = active;
        tempB = false;
        INHL = (AIT-.5)/5;
        suspended = false;
        topMenu = new OmniPodHomeMenu(this, c);
        currentMenu = topMenu;
        basalRates = basals;
        bloodGlucose = 110;
        bloodSugarCarbRatio = ISEN/ICR;
        trueInsulinFactor = (ISEN / INHL *Math.log10(2.0))/(1.0-Math.pow(2.0,-AIT/INHL));
        trueCarbFactor = (bloodSugarCarbRatio *2.5 *Math.log10(2)/(1-Math.pow(2.0,-2.5)));
    }

    public void home() {
        currentMenu = topMenu;
    }


    public void button1() {
        OmniPodMenu temp = ((OmniPodMenu)currentMenu).button1();
        if(temp!=null){
            currentMenu = temp;
        }
    }

    public void button2() {
        OmniPodMenu temp = ((OmniPodMenu)currentMenu).button2();
        if(temp!=null){
            currentMenu = temp;
        }
    }

    public void button3() {
        OmniPodMenu temp = ((OmniPodMenu)currentMenu).button3();
        if(temp!=null){
            currentMenu = temp;
        }
    }

    public void setBolusBG(int i, boolean b) {
        bolasBG = i;
        useBG = b;
    }

    public int getBolusBG() {
        return bolasBG;
    }

    public void setGramsCarbs(int i, boolean b) {
        gramsCarbs = i;
        useCarbs = b;
    }

    public int getGramsCarbs() {
        return gramsCarbs;
    }

    @Override
    public double getBolusUnits() {
        //bolas wiz settings
        double total=0;
        if(bolasBG-highGluc>0 && useBG){
            total += (bolasBG-highGluc)/ISEN;
        }
        if(gramsCarbs>0 && useCarbs){
            total += gramsCarbs/ICR;
        }

        int rounded20 = (int)Math.round(total*20);
        total = (double)(rounded20 / 20);
        return total;
    }

    public void bolasWiz(double total){
        addedInsulin+=total;
        timeSinceBolas=0;

        Toast.makeText(con, "Bolas successful with " + total + " units.", Toast.LENGTH_LONG).show();
    }

}
