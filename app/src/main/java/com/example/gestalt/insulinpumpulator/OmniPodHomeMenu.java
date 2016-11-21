package com.example.gestalt.insulinpumpulator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodHomeMenu extends OmniPodMenu{
    public OmniPodHomeMenu(AInsulinPump pu, Context c) {
        pump = pu;
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new OmniBolusMenu(this, c));
        subMenus.add(new OmniMoreActionsMenu(this, c));
        subMenus.add(new OmniTempBasalMenu(this, c));
        subMenus.add(new OmniMyRecordsMenu(this, c));
        subMenus.add(new OmniSettingsMenu(this, c));
        subMenus.add(new OmniSuspendMenu(this, c));
        button1Text = "Status";
        button2Text = " ";
        button3Text = "Select";
        current = 0;//default highlighted
        con = c;
        menuName = (c.getResources().getString(R.string.mainmenu));
    }

    @Override
    public OmniPodMenu button1() {
        OmniPodMenu m = new OmniStatusMenu(this, con);
        return m;
    }
}

class OmniStatusMenu extends OmniPodMenu {
    public OmniStatusMenu(OmniPodMenu p, Context c) {
        parent = p;
        pump = p.pump;
        con = c;
        current = -2;
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "We're sorry, this menu is not yet implemented."));
        button1Text = "Back";
        button2Text = " ";
        button3Text = " ";
    }

    @Override
    public void up() {
        return;
    }
    @Override
    public void down(){
        return;
    }

    @Override
    public OmniPodMenu button3() {
        return this;
    }
}

class OmniMoreActionsMenu extends OmniPodMenu {
    public OmniMoreActionsMenu(OmniPodMenu p, Context c) {
        parent = p;
        pump = p.pump;
        con = c;
        current = -1;
        menuName = "More Actions";
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "We're sorry, this menu is not yet implemented."));
        button1Text = "Back";
        button2Text = " ";
        button3Text = " ";
    }

    @Override
    public OmniPodMenu button3() {
        return this;
    }
}

class OmniTempBasalMenu extends OmniPodMenu {
    public OmniTempBasalMenu(OmniPodMenu p, Context c) {
        parent = p;
        pump = p.pump;
        con = c;
        current = -1;
        menuName = "Temp Basal";
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "We're sorry, this menu is not yet implemented."));
        button1Text = "Back";
        button2Text = " ";
        button3Text = " ";
    }

    @Override
    public OmniPodMenu button3() {
        return this;
    }
}

class OmniMyRecordsMenu extends OmniPodMenu {
    public OmniMyRecordsMenu(OmniPodMenu p, Context c) {
        parent = p;
        pump = p.pump;
        con = c;
        current = -1;
        menuName = "My Records";
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "We're sorry, this menu is not yet implemented."));
        button1Text = "Back";
        button2Text = " ";
        button3Text = " ";
    }

    @Override
    public OmniPodMenu button3() {
        return this;
    }
}

class OmniSettingsMenu extends OmniPodMenu {
    public OmniSettingsMenu(OmniPodMenu p, Context c) {
        parent = p;
        pump = p.pump;
        con = c;
        current = -1;
        menuName = "Settings";
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "We're sorry, this menu is not yet implemented."));
        button1Text = "Back";
        button2Text = " ";
        button3Text = " ";
    }

    @Override
    public OmniPodMenu button3() {
        return this;
    }
}

class OmniSuspendMenu extends OmniPodMenu {
    public OmniSuspendMenu(OmniPodMenu p, Context c) {
        parent = p;
        pump = p.pump;
        con = c;
        current = -1;
        menuName = "Suspend";
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "We're sorry, this menu is not yet implemented."));
        button1Text = "Back";
        button2Text = " ";
        button3Text = " ";
    }

    @Override
    public OmniPodMenu button3() {
        return this;
    }
}

class OmniBolusMenu extends OmniPodMenu {
    public OmniBolusMenu(OmniPodMenu p, Context c) {
        parent = p;
        pump = p.pump;
        con = c;
        current = -1;
        menuName = c.getString(R.string.bolus);
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EmptyMenu(this, c, "Enter current BG."));
        subMenus.add(new EmptyMenu(this, c, " "));
        subMenus.add(new EmptyMenu(this, c, " "));
        subMenus.add(new EmptyMenu(this, c, " "));
        subMenus.add(new EmptyMenu(this, c, " "));
        subMenus.add(new EmptyMenu(this, c, "Use this for bolus calcs?"));
        button1Text = "Back";
        button2Text = "No";
        button3Text = "Yes";
        enterField = 100;
        inputSuffix = "mg/dl";
    }

    @Override
    public void up() {
        if(enterField==0){
            enterField=90;
        }
        else if(enterField ==85){
            enterField=0;
        }
        else if(enterField==500){
            enterField =30;
        }
        else {
            enterField += 5;
        }
    }
    @Override
    public void down(){
        if(enterField==90){
            enterField=0;
        }
        else if(enterField ==0){
            enterField =85;
        }
        else if(enterField==30){
            enterField =500;
        }
        else{
            enterField -=5;
        }
    }

    @Override
    public OmniPodMenu button2() {
        ((OmniPodPump)pump).setBolusBG((int)enterField, false);
        return new OmniEatingMenu(this, con);
    }

    @Override
    public OmniPodMenu button3() {
        ((OmniPodPump)pump).setBolusBG((int)enterField, true);
        return new OmniEatingMenu(this, con);
    }


    class OmniEatingMenu extends OmniPodMenu{
        public OmniEatingMenu(OmniPodMenu p, Context c) {
            parent = p;
            pump = p.pump;
            con = c;
            current = -2;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this, c, "Are you going to eat now?"));
            subMenus.add(new EmptyMenu(this, c, "Did you just eat?"));
            button1Text = "Back";
            button2Text = "No";
            button3Text = "Yes";
        }

        @Override
        public void up() {
            return;
        }
        @Override
        public void down(){
            return;
        }

        @Override
        public OmniPodMenu button2() {
            ((OmniPodPump)pump).setGramsCarbs(0, false);
            return new OmniBolusConfirm(this, con);
        }

        @Override
        public OmniPodMenu button3() {
            return new OmniCarbs(this, con);
        }
    }

    class OmniCarbs extends OmniPodMenu {
        public OmniCarbs(OmniPodMenu p, Context c) {
            parent = p;
            pump = p.pump;
            con = c;
            current = -1;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this, c, "Enter carb value for the food you are about to eat (or that you just ate)."));
            button1Text = "Back";
            button2Text = " ";
            button3Text = "Enter";
            enterField = 0;
            inputSuffix = "g";
        }

        @Override
        public void down() {
            if(enterField ==0){
                enterField =300;
            }
            else{
                enterField -=3;
            }
        }

        @Override
        public void up() {
            if(enterField ==300){
                enterField =0;
            }
            else{
                enterField +=3;
            }
        }

        @Override
        public OmniPodMenu button3() {
            ((OmniPodPump)pump).setGramsCarbs((int)enterField, true);
            return new OmniBolusConfirm(this, con);
        }
    }

    class OmniBolusConfirm extends OmniPodMenu {
        public OmniBolusConfirm(OmniPodMenu p, Context c) {
            parent = p;
            pump = p.pump;
            con = c;
            current = -2;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this, c, "Use these values for bolus calculations?"));
            subMenus.add(new EmptyMenu(this, c, " "));
            subMenus.add(new EmptyMenu(this, c, "BG: " + ((OmniPodPump) pump).getBolusBG() + " mg/dl"));
            subMenus.add(new EmptyMenu(this, c, "Carbs: " + ((OmniPodPump) pump).getGramsCarbs() + " g"));
            button1Text = "Back";
            button2Text = "No";
            button3Text = "Yes";
        }

        @Override
        public void up() {
            return;
        }
        @Override
        public void down(){
            return;
        }

        @Override
        public OmniPodMenu button2() {
            return (OmniPodMenu)back();
        }

        @Override
        public OmniPodMenu button3() {
            return new OmniFinalBolusConfirm(this, con);
        }
    }

    class OmniFinalBolusConfirm extends OmniPodMenu {
        public OmniFinalBolusConfirm(OmniPodMenu p, Context c) {
            parent = p;
            pump = p.pump;
            con = c;
            current = -1;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this, c, "Suggested Bolus: " + ((OmniPodPump)pump).getBolusUnits()));
            subMenus.add(new EmptyMenu(this, c, "BG: " + ((OmniPodPump) pump).getBolusBG() + " mg/dl"));
            subMenus.add(new EmptyMenu(this, c, "Carbs: " + ((OmniPodPump) pump).getGramsCarbs() + " g"));
            button1Text = "Back";
            button2Text = "Extend";
            button3Text = "Enter";
            enterField = ((OmniPodPump)pump).getBolusUnits();
            inputSuffix = "u";
        }

        @Override
        public String getInputText() {
            return enterField + inputSuffix;
        }

        @Override
        public void down() {
            if(enterField < 0){
                enterField = 0;
            }
            else{
                enterField -= 0.05;

                int rounded20 = (int)Math.round(enterField*20);
                enterField = (double)rounded20 / 20;
            }
        }

        @Override
        public void up() {
            if(enterField > 20){
                enterField =20;
            }
            else{
                enterField +=0.05;

                int rounded20 = (int)Math.round(enterField*20);
                enterField = (double)rounded20 / 20;
            }
        }

        @Override
        public OmniPodMenu button3() {
            ((OmniPodPump)pump).bolasWiz(enterField);
            return (OmniPodMenu) pump.getTopMenu();
        }
    }
}


