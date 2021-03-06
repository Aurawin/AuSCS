package com.aurawin.scs.rsr.protocol.imap.method;

import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Method;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.solution.Version;
import com.aurawin.scs.rsr.protocol.transport.IMAP_4_1;
import static com.aurawin.scs.rsr.protocol.imap.def.status.Status.sOK;
import org.hibernate.Session;

import java.lang.reflect.InvocationTargetException;


import static com.aurawin.core.lang.Table.CRLF;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;
import static com.aurawin.core.solution.Table.RSR.IMAP.Method.Id;

/**
 * Created by atbrunner on 2/12/18.
 */
public class ID extends Method {
    public ID() {
        super(Id);
    }
    public ID(String key) {
        super(key);
    }

    public Result onProcess(Session ssn, Transport transport) throws IllegalAccessException,InvocationTargetException {
        IMAP_4_1 h = (IMAP_4_1) transport;
        h.Response.Status=sOK;
        h.Response.Sequence="*";
        h.Request.Payload.Write(
                "ID ("+
                "\"name\" \"" + System.getProperty(Settings.Properties.Title)+"\" "+
                "\"vendor\" \""+System.getProperty(Settings.Properties.Vendor)+"\" "+
                "\"support-url\" https://aurawin.com\" "+
                "\"edition\" " + System.getProperty(Settings.Properties.Edition)+"\" "+
                "\"version\" " + h.Owner.Engine.Version.Major+"."+h.Owner.Engine.Version.Mid+"."+h.Owner.Engine.Version.Minor+"\" "+
                "\"build\" " + h.Owner.Engine.Version.Build+"\" "+
                "\"remote-host\" "+h.Address.getHostName()+"\""+
                ")"+CRLF
        );

        return Ok;

    }
}
