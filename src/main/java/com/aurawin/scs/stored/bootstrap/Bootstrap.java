package com.aurawin.scs.stored.bootstrap;


import com.aurawin.core.stored.annotations.AnnotatedList;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Namespace;

import java.util.ArrayList;

public class Bootstrap {

    public static AnnotatedList buildAnnotations(){
        AnnotatedList al = new AnnotatedList();
        al.addAll(Plugins.buildAnnotations());
        al.addAll(Stored.buildAnnotations());
        return al;
    }
    public static ArrayList<UniqueId> buildNamespaces(){
        return Namespace.Discover();
    }
}
