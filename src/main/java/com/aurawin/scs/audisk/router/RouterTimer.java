package com.aurawin.scs.audisk.router;

import com.aurawin.scs.solution.Settings;

public class RouterTimer implements Runnable {
    public void run(){
        try{
            while (true){
                Router.scanForRoutes();
                Thread.sleep(Settings.AuDisk.Router.ScanTimerYield);
            }
        } catch (InterruptedException ie){
            // shutting down

        }
    }
}
