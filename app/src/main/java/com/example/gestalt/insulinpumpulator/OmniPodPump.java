package com.example.gestalt.insulinpumpulator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodPump extends AInsulinPump {
    public OmniPodPump(Context c){
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
}
