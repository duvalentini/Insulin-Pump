package com.example.gestalt.insulinpumpulator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Joshua on 6/18/2016.
 */
public class MedtronicMainMenu extends AInsulinPumpMenu {


    public MedtronicMainMenu(AInsulinPump pu, Context c) {
        pump = pu;
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new BolusMenu(this, c));
        subMenus.add(new SuspendMenu(this, c));
        subMenus.add(new SensorMenu(this, c));
        subMenus.add(new BasalMenu(this, c));
        subMenus.add(new PrimeMenu(this, c));
        subMenus.add(new UtilitiesMenu(this, c));
        current = 0;//default highlighted
        con = c;
        menuName = (c.getResources().getString(R.string.mainmenu));


    }



}
class BolusMenu extends AInsulinPumpMenu{
    public BolusMenu(AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.bolus));
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new UseBWizard(this, c));
        subMenus.add(new ManualBolus(this,c));
        subMenus.add(new BolusHistory(this,c));
        subMenus.add(new WizardSetup(this,c));
        subMenus.add(new MaxBolus(this,c));
        subMenus.add(new DualSquare(this,c));
        subMenus.add(new EasyBolus(this,c));
        subMenus.add(new BgReminder(this,c));

    }
    class UseBWizard extends AInsulinPumpMenu{
        public UseBWizard(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.bwizard));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EnterBG(this,c));
        }
        class EnterBG extends AInsulinPumpMenu{

            public EnterBG(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = ("Enter BG");
                current =0;
                enterField=0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EnterFood(this,c));
                subMenus.add(new EmptyMenu(this,c,"Blood Glucose : "+enterField+" mg/dl"));
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
                else{
                    enterField+=5;
                }
                ((EmptyMenu)subMenus.get(1)).setName("Blood Glucose : "+enterField+" mg/dl");
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
                ((EmptyMenu)subMenus.get(1)).setName("Blood Glucose : "+enterField+" mg/dl");
            }

            @Override
            public AInsulinPumpMenu confirm(){
                pump.setBolasBG((int)enterField);
                return subMenus.get(0);
            }
            class EnterFood extends AInsulinPumpMenu{

                public EnterFood(AInsulinPumpMenu p, Context c){
                    parent = p;
                    pump = p.pump;
                    con = c;
                    menuName = (c.getResources().getString(R.string.bwizard));
                    current =0;
                    enterField =0;
                    subMenus = new ArrayList<AInsulinPumpMenu>();
                    subMenus.add(new EmptyMenu(p,c,"Food Eaten : "+enterField+"grams"));
                }

                @Override
                public void down() {
                    if(enterField ==0){
                        enterField =300;
                    }
                    else{
                        enterField -=3;
                    }
                    ((EmptyMenu)subMenus.get(0)).setName("Food Eaten : "+enterField+" g");
                }

                @Override
                public void up() {
                    if(enterField ==300){
                        enterField =0;
                    }
                    else{
                        enterField +=3;
                    }
                    ((EmptyMenu)subMenus.get(0)).setName("Food Eaten : "+enterField+" g");
                }

                @Override
                public AInsulinPumpMenu confirm() {
                    pump.setGramsCarbs((int)enterField);
                    pump.bolasWiz();
                    return pump.getTopMenu();
                }
            }
        }

    }
    class ManualBolus extends AInsulinPumpMenu{
        public ManualBolus (AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            current =0;
            menuName = (c.getResources().getString(R.string.manualbolus));
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new NormalBolus(this,c));
            subMenus.add(new SquareWaveBolus(this,c));
            subMenus.add(new DualWaveBolus(this,c));

        }

        class NormalBolus extends AInsulinPumpMenu{
            public NormalBolus(AInsulinPumpMenu p, Context c){
                parent =p;
                pump = p.pump;
                con =c;
                menuName = (c.getResources().getString(R.string.normbolus));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new NormEnter(this,c));
            }
            class NormEnter extends AInsulinPumpMenu{
                public NormEnter(AInsulinPumpMenu p, Context c){
                    parent =p;
                    pump = p.pump;
                    con =c;
                    menuName = (c.getResources().getString(R.string.normbolus));
                    current =0;
                    enterField=0;
                    subMenus = new ArrayList<AInsulinPumpMenu>();
                    subMenus.add(new EmptyMenu(this,c));
                    ((EmptyMenu)subMenus.get(0)).setName("Insulin : "+enterField+" u");
                }
                @Override
                public double getEnterField(){
                    return enterField/3.0;
                }

                @Override
                public void up(){
                    if(enterField==45){
                        enterField=0;
                    }
                    else{
                        enterField++;
                    }
                    ((EmptyMenu)subMenus.get(0)).setName("Insulin : "+enterField+" u");
                }
                @Override
                public void down(){
                    if(enterField==0){
                        enterField=45;
                    }
                    else{
                        enterField--;
                    }
                    ((EmptyMenu)subMenus.get(0)).setName("Insulin : "+enterField+" u");
                }

                @Override
                public AInsulinPumpMenu confirm() {
                    pump.setBolusUnits(enterField/3.0);
                    pump.manualBolas();
                    return pump.getTopMenu();
                }
            }
        }
        class SquareWaveBolus extends AInsulinPumpMenu{
            public SquareWaveBolus(AInsulinPumpMenu p, Context c){
                parent =p;
                pump = p.pump;
                con =c;
                menuName = (c.getResources().getString(R.string.squarebolus));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
        class DualWaveBolus extends AInsulinPumpMenu{
            public DualWaveBolus(AInsulinPumpMenu p, Context c){
                parent =p;
                pump = p.pump;
                con =c;
                menuName = (c.getResources().getString(R.string.dualbolus));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }


    }
    class BolusHistory extends AInsulinPumpMenu{
        public BolusHistory(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.bhistory));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class WizardSetup extends AInsulinPumpMenu{
        public WizardSetup(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.bwizsetup));
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new WizEditSettings(this,c));
            subMenus.add(new WizReviewSettings(this,c));
            current =0;
        }
        class WizEditSettings extends AInsulinPumpMenu{
            public WizEditSettings (AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.editsettings));
                subMenus = new ArrayList<AInsulinPumpMenu>();
                current =0;
                subMenus.add(new EmptyMenu(this,c));
            }
        }
        class WizReviewSettings extends AInsulinPumpMenu{
            public WizReviewSettings (AInsulinPumpMenu p, Context c) {
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.revsettings));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
    }
    class MaxBolus extends AInsulinPumpMenu{
        public MaxBolus(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.maxbolus));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class DualSquare extends AInsulinPumpMenu{
        public DualSquare (AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.dualsquarebolus));
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new DualOn(this,c));
            subMenus.add(new DualOff(this,c));

        }
        public class DualOn extends AInsulinPumpMenu{
            public DualOn(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.on));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
        public class DualOff extends AInsulinPumpMenu{
            public DualOff(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.off));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
    }
    class EasyBolus extends AInsulinPumpMenu{
        public EasyBolus(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.easybolus));
            current =0;//This needs to be check normal value.
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EasyOff(this, c));
            subMenus.add(new EasyOnSet(this, c));
        }
        public class EasyOff extends AInsulinPumpMenu {
            public EasyOff(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.off));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
        public class EasyOnSet extends AInsulinPumpMenu{
            public EasyOnSet(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.onset));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
    }
    class BgReminder extends AInsulinPumpMenu{
        public BgReminder(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.bgreminder));
            current = 0;//take in current setting if possible
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new BgOff(this, c));
            subMenus.add(new BgOn(this, c));
        }
        public class BgOff extends AInsulinPumpMenu{
            public BgOff(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.off));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
        public class BgOn extends AInsulinPumpMenu{
            public BgOn(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.on));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }
        }
    }
}

class SuspendMenu extends AInsulinPumpMenu{
    public SuspendMenu(AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.suspend));
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new SuspendConfirm(this,c));
    }
    class SuspendConfirm extends AInsulinPumpMenu{
        public SuspendConfirm(AInsulinPumpMenu p, Context c){
            parent =p;
            pump = p.pump;
            con = c;
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            menuName = (con.getResources().getString(R.string.suspend));
        }
        @Override
        public AInsulinPumpMenu confirm(){
            pump.suspend();
            if(pump.isSuspended()){
                menuName = con.getResources().getString(R.string.suspended);
                return this;
            }
            else{
                menuName = (con.getResources().getString(R.string.suspend));
                return pump.getTopMenu();

            }
        }
        @Override
        public void up(){

        }
        @Override
        public void down(){

        }
    }



}

class SensorMenu extends AInsulinPumpMenu{
    public SensorMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        subMenus = new ArrayList<AInsulinPumpMenu>();
        menuName = (c.getResources().getString(R.string.sensor));
        subMenus.add(new SensorSetup(this, c));
    }
    class SensorSetup extends AInsulinPumpMenu{
        public SensorSetup( AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.sensorsetup));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new SensorEdit(this, c));
            subMenus.add(new SensorReview(this, c));
            //needs Settings added if sensor is turned on.
        }
        class SensorEdit extends AInsulinPumpMenu{
            public SensorEdit(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.editsettings));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new SensorOn(this, c));
                subMenus.add(new SensorOff(this, c));
            }//needs extra settings if sensor is turned on.

            class SensorOn extends AInsulinPumpMenu{
                public SensorOn(AInsulinPumpMenu p, Context c){
                    parent = p;
                    pump = p.pump;
                    con = c;
                    menuName = (c.getResources().getString(R.string.on));
                    current =0;
                    subMenus = new ArrayList<AInsulinPumpMenu>();
                    subMenus.add(new EmptyMenu(this,c));
                }
            }
            class SensorOff extends AInsulinPumpMenu{
                public SensorOff(AInsulinPumpMenu p, Context c){
                    parent = p;
                    pump = p.pump;
                    con = c;
                    menuName = (c.getResources().getString(R.string.off));
                    current =0;
                    subMenus = new ArrayList<AInsulinPumpMenu>();
                    subMenus.add(new EmptyMenu(this,c));
                }
            }

        }
        class SensorReview extends AInsulinPumpMenu{
            public SensorReview(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.revsettings));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new EmptyMenu(this,c));
            }// needs extra settings added.
        }
    }

}
class BasalMenu extends AInsulinPumpMenu{
    public BasalMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.basal));
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new EditTempBasal(this, c));
        subMenus.add(new SelectPatterns(this, c));
        subMenus.add(new EditBasal(this,c));
        subMenus.add(new BasalReview(this,c));
        subMenus.add(new MaxBasal(this,c));
        subMenus.add(new Patterns(this,c));
        subMenus.add(new TempBasalType(this,c));
    }
    class EditTempBasal extends AInsulinPumpMenu{
        public EditTempBasal(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.settempbasal));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new TempDuration(this,c));
        }
        class TempDuration extends AInsulinPumpMenu{
            public TempDuration(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.settempbasal));
                current =0;
                subMenus = new ArrayList<AInsulinPumpMenu>();
                subMenus.add(new TempPercent(this,c));
                enterField=0;
                subMenus.add(new EmptyMenu(this, c, "time :"+enterField+" hours"));
            }

            @Override
            public void up(){
                enterField+=.5;
                if(enterField>24){
                    enterField=0;
                }
                ((EmptyMenu)subMenus.get(1)).setName("time :"+enterField+" hours");
            }
            @Override
            public void down(){
                enterField-=.5;
                if(enterField<0){
                    enterField=24;
                }
                ((EmptyMenu)subMenus.get(1)).setName("time :"+enterField+" hours");
            }
            @Override
            public AInsulinPumpMenu confirm(){
                pump.setBasalDuration(enterField);
                return subMenus.get(0);
            }

            class TempPercent extends AInsulinPumpMenu{
                public TempPercent(AInsulinPumpMenu p, Context c){
                    parent = p;
                    pump = p.pump;
                    con = c;
                    menuName = (c.getResources().getString(R.string.settempbasal));
                    current =0;
                    subMenus = new ArrayList<AInsulinPumpMenu>();
                    enterField=100;
                    subMenus.add(new EmptyMenu(this,c));
                    ((EmptyMenu)subMenus.get(0)).setName("percent :"+enterField);
                }

                @Override
                public void up(){
                    enterField+=5;
                    if(enterField>200){
                        enterField=0;
                    }
                    ((EmptyMenu)subMenus.get(0)).setName("percent :"+enterField);
                }
                @Override
                public void down(){
                    enterField-=5;
                    if(enterField<0){
                        enterField=200;
                    }
                    ((EmptyMenu)subMenus.get(0)).setName("percent :"+enterField);
                }
                @Override
                public AInsulinPumpMenu confirm(){
                    pump.setBasalPercent((int)enterField);
                    pump.activateTempBasal();
                    return pump.getTopMenu();
                }

            }
        }
    }
    class SelectPatterns extends AInsulinPumpMenu{
        public SelectPatterns (AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.selectpatterns));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class EditBasal extends AInsulinPumpMenu{
        public EditBasal(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.setbasal));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class BasalReview extends AInsulinPumpMenu{
        public BasalReview(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.basalreview));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class MaxBasal extends AInsulinPumpMenu{
        public MaxBasal(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.maxbasal));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class Patterns extends AInsulinPumpMenu{
        public Patterns(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.patterns));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class TempBasalType extends AInsulinPumpMenu{
        public TempBasalType (AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.tempbasaltype));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }


}
class PrimeMenu extends AInsulinPumpMenu{
    public PrimeMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.prime));
        subMenus = new ArrayList<AInsulinPumpMenu>();
        subMenus.add(new FixedPrime(this,c));
        subMenus.add(new Rewind(this,c));
        subMenus.add(new PrimeHistory(this,c));
    }
    class FixedPrime extends AInsulinPumpMenu{
        public FixedPrime(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.fixedprime));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class Rewind extends AInsulinPumpMenu{
        public Rewind(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.rewind));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class PrimeHistory extends AInsulinPumpMenu{
        public PrimeHistory(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.primehistory));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }

}
class UtilitiesMenu extends AInsulinPumpMenu{
    public UtilitiesMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        subMenus = new ArrayList<AInsulinPumpMenu>();
        menuName = (c.getResources().getString(R.string.utilities));
        subMenus.add(new LockKeypad(this,c));
        subMenus.add(new Alarm(this, c));
        subMenus.add(new DailyTotals(this, c));
        subMenus.add(new TimeDate(this, c));
        subMenus.add(new AlarmClock(this, c));
        subMenus.add(new MeterOptions(this, c));
        subMenus.add(new RemoteOptions(this,c));
        subMenus.add(new Block(this,c));
        subMenus.add(new Selftest(this,c));
        subMenus.add(new UserSettings(this,c));
        subMenus.add(new Language(this,c));

    }
    class LockKeypad extends AInsulinPumpMenu{
        public LockKeypad(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.lockkeypad));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class Alarm extends AInsulinPumpMenu{
        public Alarm(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.alarm));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class DailyTotals extends AInsulinPumpMenu{
        public DailyTotals(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.dailytotals));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class TimeDate extends AInsulinPumpMenu{
        public TimeDate(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.timedate));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class AlarmClock extends AInsulinPumpMenu{
        public AlarmClock(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.alarmclock));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class MeterOptions extends AInsulinPumpMenu{
        public MeterOptions(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.meteroptions));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class RemoteOptions extends AInsulinPumpMenu{
        public RemoteOptions(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.remoteoptions));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class Block extends AInsulinPumpMenu{
        public Block(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.block));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class Selftest extends AInsulinPumpMenu{
        public Selftest(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.selftest));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class UserSettings extends AInsulinPumpMenu{
        public UserSettings(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.usersettings));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }
    class Language extends AInsulinPumpMenu{
        public Language(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.language));
            current =0;
            subMenus = new ArrayList<AInsulinPumpMenu>();
            subMenus.add(new EmptyMenu(this,c));
        }
    }


}
