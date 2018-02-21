package com.aurawin.scs.audisk.router.future;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cMakeFolder;

public class ListAllFiles extends Isssue implements Callable<ArrayList<String>> {

    @Override
    public ArrayList<String> call() throws Exception{
        // construct ListAllFiles command

        // Route.Connector.
        return new ArrayList<String>();
    }

}
