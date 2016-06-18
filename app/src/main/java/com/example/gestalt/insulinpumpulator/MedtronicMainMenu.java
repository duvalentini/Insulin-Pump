package com.example.gestalt.insulinpumpulator;

/**
 * Created by Joshua on 6/18/2016.
 */
public class MedtronicMainMenu extends AInsulinPumpMenu {


    public MedtronicMainMenu(AInsulinPump pu) {
        pump = pu;
        subMenus.add(new BolusMenu(this));
        subMenus.add(new SuspendMenu(this));
        subMenus.add(new SensorMenu(this));
        subMenus.add(new BasalMenu(this));
        subMenus.add(new PrimeMenu(this));
        subMenus.add(new UtilitiesMenu(this));
        current = 0;//default highlighted

    }



}
class BolusMenu extends AInsulinPumpMenu{
    public BolusMenu(AInsulinPumpMenu p){
        parent =p;
        pump = p.pump;
    }
    class UseBWizard extends AInsulinPumpMenu{
        public UseBWizard(AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
    class ManualBolus extends AInsulinPumpMenu{
        public ManualBolus (AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
    class BolusHistory extends AInsulinPumpMenu{
        public BolusHistory(AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
    class WizardSetup extends AInsulinPumpMenu{
        public WizardSetup(AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
    class MaxBolus extends AInsulinPumpMenu{
        public MaxBolus(AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
    class DualSquare extends AInsulinPumpMenu{
        public DualSquare (AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
    class EasyBolus extends AInsulinPumpMenu{
        public EasyBolus(AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
    class BgReminder extends AInsulinPumpMenu{
        public BgReminder(AInsulinPumpMenu p){
            parent = p;
            pump = p.pump;
        }
    }
}


class SuspendMenu extends AInsulinPumpMenu{
    public SuspendMenu(AInsulinPumpMenu p){
        parent =p;
    }



}
class SensorMenu extends AInsulinPumpMenu{
    public SensorMenu (AInsulinPumpMenu p){
        parent =p;
    }



}
class BasalMenu extends AInsulinPumpMenu{
    public BasalMenu (AInsulinPumpMenu p){
        parent =p;
    }



}
class PrimeMenu extends AInsulinPumpMenu{
    public PrimeMenu (AInsulinPumpMenu p){
        parent =p;
    }

}
class UtilitiesMenu extends AInsulinPumpMenu{
    public UtilitiesMenu (AInsulinPumpMenu p){
        parent =p;
    }


}
