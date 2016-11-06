package com.example.gestalt.insulinpumpulator;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;

/**
 * Created by Joshua on 6/18/2016.
 */
public class MedtronicPump extends AInsulinPump {

    public MedtronicPump(Context c){
        ICR = 15;
        ISEN = 30;
        lowGluc = 70;
        highGluc = 130;
        AIT = 3.25;
        tempB = false;
        INHL = (AIT-.5)/5;
        suspended = false;
        topMenu= new MedtronicMainMenu(this,c);
        currentMenu = topMenu;
        bloodGlucose = 90;
        bloodSugarCarbRatio = ISEN/ICR;
        trueInsulinFactor = (ISEN / INHL *Math.log10(2.0))/(1.0-Math.pow(2.0,-AIT/INHL));
        trueCarbFactor = (bloodSugarCarbRatio *2.5 *Math.log10(2)/(1.0-Math.pow(2.0,-2.5)));
        exercising = false;
    }
    public MedtronicPump(int carbRatio, int sensitive, int lowGC, int highGC, int active, ArrayList<Double> basals, Context c){
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
        basalRates = basals;
        bloodGlucose = 110;
        bloodSugarCarbRatio = ISEN/ICR;
        trueInsulinFactor = (ISEN / INHL *Math.log10(2.0))/(1.0-Math.pow(2.0,-AIT/INHL));
        trueCarbFactor = (bloodSugarCarbRatio *2.5 *Math.log10(2)/(1-Math.pow(2.0,-2.5)));
    }

    public void testFormula(){
        double startgluc = this.bloodGlucose;
        double startfood = this.foodgrams;
        double startinsulin = this.activeInsulin;
        double startaddedinsulin = this.addedInsulin;
        System.out.println("inhl ="+INHL+"\n trueInsulinFactor = "+trueInsulinFactor+"\n true carb = "+trueCarbFactor);

        System.out.println("start gluc = "+startgluc+"\nstart food = "+startfood+"\nstart insulin = "+startinsulin+ "startAdInsul = "+startaddedinsulin);
        for(int i = 0; i<120;i+=5){
            passTime(5);
            System.out.println("time ="+i+"\ngluc = "+this.bloodGlucose+"\n food ="+foodgrams+"\ninsul = "+this.activeInsulin+"\ninsul adding ="+this.addedInsulin);

        }
        bloodGlucose = startgluc;
        foodgrams = startfood;
        activeInsulin = startinsulin;
        addedInsulin = startaddedinsulin;
        timeSinceBolas =0;
        for(int i = 0; i<120;i+=10){
            passTime(10);
            System.out.println("time ="+i+"\ngluc = "+this.bloodGlucose+"\n food ="+foodgrams+"\ninsul = "+this.activeInsulin+"\ninsul adding ="+this.addedInsulin);

        }
        bloodGlucose = startgluc;
        foodgrams = startfood;
        activeInsulin = startinsulin;
        addedInsulin = startaddedinsulin;
        timeSinceBolas =0;
        for(int i = 0; i<120;i+=15){
            passTime(15);
            System.out.println("time ="+i+"\ngluc = "+this.bloodGlucose+"\n food ="+foodgrams+"\ninsul = "+this.activeInsulin+"\ninsul adding ="+this.addedInsulin);

        }


    }
}
