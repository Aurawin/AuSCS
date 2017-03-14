package com.aurawin.scs.stored.bootstrap;

import com.aurawin.core.lang.Table;
import com.aurawin.core.log.Syslog;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.stored.annotations.AnnotatedList;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.core.admin.cms.CMS;
import org.hibernate.Session;

import java.util.ArrayList;

public class Plugins {
    public static ArrayList<Plug> Manifest = new ArrayList<>();
    public static AnnotatedList buildAnnotations(){
        AnnotatedList al = new AnnotatedList();
        for (Plug p: Manifest) {
            al.add(p.getClass());
        }
        return al;
    }

    private static Plug initializePlugin(Class<? extends Plug> COfP, Session ssn){

        try {
            Plug p = COfP.newInstance();
            p.Setup(ssn);
            Manifest.add(p);
            return p;
        } catch (Exception ex){
            Syslog.Append(Plugins.class.getCanonicalName(),"initializePlugin",Table.Format(Table.Exception.Plugin.Instantiation,ex.getMessage()));
            return null;
        }
    }
    public static void Finalize(){
        Session ssn = Entities.openSession();
        try{
            for (Plug p : Manifest){
                p.Teardown(ssn);
            }
            Manifest.clear();
        } finally{
            ssn.close();
        }
    }
    public static void Initialize() {
        Session ssn = Entities.openSession();
        try{
            initializePlugin(com.aurawin.scs.core.admin.cms.CMS.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.cms.Domain.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.cms.Template.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.ACL.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.Admin.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.Cloud.class,ssn);

            initializePlugin(com.aurawin.scs.core.Account.class,ssn);
            initializePlugin(com.aurawin.scs.core.ACL.class,ssn);
            initializePlugin(com.aurawin.scs.core.Avatar.class,ssn);
            initializePlugin(com.aurawin.scs.core.Login.class,ssn);
            initializePlugin(com.aurawin.scs.core.Noid.class,ssn);

        } finally{
            ssn.close();
        }
    }
}
