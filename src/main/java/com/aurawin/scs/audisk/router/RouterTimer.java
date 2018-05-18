package com.aurawin.scs.audisk.router;

import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.Entities;

import static com.aurawin.scs.solution.Settings.AuDisk.Router.ScanTimerFastYield;
import static com.aurawin.scs.solution.Settings.AuDisk.Router.ScanTimerYield;

public class RouterTimer extends Thread {
    private boolean FastYield = true;
    public void run(){
        try{
            while (!Entities.Loaded){
                Thread.sleep(Settings.AuDisk.Router.EntitiesLoadingDelay);
            }
            while (true){
                Router.scanForRoutes();
                FastYield = Router.getRouteCount()==0;
                Thread.sleep((FastYield)? ScanTimerFastYield:ScanTimerYield);
            }
        } catch (InterruptedException ie){
            // shutting down

        }
    }
}
