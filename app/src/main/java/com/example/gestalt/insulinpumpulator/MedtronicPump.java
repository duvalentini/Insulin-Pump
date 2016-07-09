package com.example.gestalt.insulinpumpulator;

import android.content.Context;

/**
 * Created by Joshua on 6/18/2016.
 */
public class MedtronicPump extends AInsulinPump {

    public MedtronicPump(Context c){
        ICR = 15;
        ISEN = 30;
        lowGluc = 70;
        highGluc = 130;
        AIT = 3;
        tempB = false;
        INHL = (AIT-.5)/5;
        suspended = false;
        topMenu= new MedtronicMainMenu(this,c);
        currentMenu = topMenu;
        bloodGlucose = 110;
        bloodSugarCarbRatio = ISEN/ICR;
        trueInsulinFactor = (ISEN * 1/INHL *Math.log10(2.0))/(1-Math.pow(2.0,-AIT*INHL));
        trueCarbFactor = (bloodSugarCarbRatio *2.5 *Math.log10(2)/(1-Math.pow(2.0,-2.5)));
    }
    public MedtronicPump(int carbRatio, int sensitive, int lowGC, int highGC, int active, Context c){
            ICR = carbRatio;
            ISEN = sensitive;
            lowGluc = lowGC;
            highGluc = highGC;
            AIT = active;
            tempB = false;
            INHL = (AIT-.5)/5;
            suspended = false;
            topMenu = new MedtronicMainMenu(this, c);
            currentMenu = topMenu;
        bloodGlucose = 110;
        bloodSugarCarbRatio = ISEN/ICR;
        trueInsulinFactor = (ISEN * 1/INHL *Math.log10(2.0))/(1-Math.pow(2.0,-AIT*INHL));
        trueCarbFactor = (bloodSugarCarbRatio *2.5 *Math.log10(2)/(1-Math.pow(2.0,-2.5)));


    }
}
