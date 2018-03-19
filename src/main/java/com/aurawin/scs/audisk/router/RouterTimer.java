package com.aurawin.scs.audisk.router;

import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.Entities;

public class RouterTimer implements Runnable {
    public void run(){
        try{
            while (!Entities.Loaded){
                Thread.sleep(Settings.AuDisk.Router.EntitiesLoadingDelay);
            }
            while (true){
                Router.scanForRoutes();
                Thread.sleep(Settings.AuDisk.Router.ScanTimerYield);
            }
        } catch (InterruptedException ie){
            // shutting down

        }
    }
}
