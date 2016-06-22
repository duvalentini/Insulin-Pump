package com.example.gestalt.insulinpumpulator;

import android.content.Context;
import android.view.SubMenu;

/**
 * Created by Joshua on 6/18/2016.
 */
public class MedtronicMainMenu extends AInsulinPumpMenu {


    public MedtronicMainMenu(AInsulinPump pu, Context c) {
        pump = pu;
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
        }
    }
    class ManualBolus extends AInsulinPumpMenu{
        public ManualBolus (AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            current =0;
            menuName = (c.getResources().getString(R.string.manualbolus));
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
            }
        }
        class SquareWaveBolus extends AInsulinPumpMenu{
            public SquareWaveBolus(AInsulinPumpMenu p, Context c){
                parent =p;
                pump = p.pump;
                con =c;
                menuName = (c.getResources().getString(R.string.squarebolus));
            }
        }
        class DualWaveBolus extends AInsulinPumpMenu{
            public DualWaveBolus(AInsulinPumpMenu p, Context c){
                parent =p;
                pump = p.pump;
                con =c;
                menuName = (c.getResources().getString(R.string.dualbolus));
            }
        }


    }
    class BolusHistory extends AInsulinPumpMenu{
        public BolusHistory(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.bhistory));
        }
    }
    class WizardSetup extends AInsulinPumpMenu{
        public WizardSetup(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.bwizsetup));
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
            }
        }
        class WizReviewSettings extends AInsulinPumpMenu{
            public WizReviewSettings (AInsulinPumpMenu p, Context c) {
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.revsettings));
            }
        }
    }
    class MaxBolus extends AInsulinPumpMenu{
        public MaxBolus(AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.maxbolus));
        }
    }
    class DualSquare extends AInsulinPumpMenu{
        public DualSquare (AInsulinPumpMenu p, Context c){
            parent = p;
            pump = p.pump;
            con = c;
            menuName = (c.getResources().getString(R.string.dualsquarebolus));
            subMenus.add(new DualOn(this,c));
            subMenus.add(new DualOff(this,c));

        }
        public class DualOn extends AInsulinPumpMenu{
            public DualOn(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.on));
            }
        }
        public class DualOff extends AInsulinPumpMenu{
            public DualOff(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.off));
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
            subMenus.add(new EasyOff(this, c));
            subMenus.add(new EasyOnSet(this, c));
        }
        public class EasyOff extends AInsulinPumpMenu {
            public EasyOff(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.off));
            }
        }
        public class EasyOnSet extends AInsulinPumpMenu{
            public EasyOnSet(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.onset));
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
            subMenus.add(new BgOff(this, c));
            subMenus.add(new BgOff(this, c));
        }
        public class BgOff extends AInsulinPumpMenu{
            public BgOff(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.off));
            }
        }
        public class BgOn extends AInsulinPumpMenu{
            public BgOn(AInsulinPumpMenu p, Context c){
                parent = p;
                pump = p.pump;
                con = c;
                menuName = (c.getResources().getString(R.string.on));
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
    }



}
class SensorMenu extends AInsulinPumpMenu{
    public SensorMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.sensor));
    }



}
class BasalMenu extends AInsulinPumpMenu{
    public BasalMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.basal));
    }



}
class PrimeMenu extends AInsulinPumpMenu{
    public PrimeMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.prime));
    }

}
class UtilitiesMenu extends AInsulinPumpMenu{
    public UtilitiesMenu (AInsulinPumpMenu p, Context c){
        parent =p;
        pump = p.pump;
        con = c;
        current =0;
        menuName = (c.getResources().getString(R.string.utilities));

    }


}
