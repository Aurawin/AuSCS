package com.aurawin.scs.stored.bootstrap;

import com.aurawin.core.stored.annotations.AnnotatedList;
import com.aurawin.scs.core.Login;
import com.aurawin.scs.core.Noid;

public class Plugins {
    public static AnnotatedList buildAnnotations(){
        AnnotatedList aL = new AnnotatedList();
        aL.add(Login.class);
        aL.add(Noid.class);
        return aL;
    }
}
