package com.aurawin.scs.rsr.protocol.imap.method;

import com.aurawin.core.rsr.client.protocol.imap.IMAP_4_1;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Method;
import com.aurawin.core.rsr.transport.methods.Result;
import org.hibernate.Session;

import java.lang.reflect.InvocationTargetException;

import static com.aurawin.core.rsr.transport.methods.Result.None;
import static com.aurawin.core.solution.Table.RSR.IMAP.Method.Check;

/**
 * Created by atbrunner on 2/12/18.
 */
public class CHECK extends Method {
    public CHECK() {
        super(Check);
    }
    public CHECK(String key) {
        super(key);
    }

    public Result onProcess(Session ssn, Transport transport) throws IllegalAccessException,InvocationTargetException {
        IMAP_4_1 h = (IMAP_4_1) transport;

        return None;

    }
}

