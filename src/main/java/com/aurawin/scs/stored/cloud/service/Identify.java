package com.aurawin.scs.stored.cloud.service;

import com.aurawin.core.ClassScanner;
import com.aurawin.core.Package;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.solution.Table;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Set;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind.svcNone;

public class Identify {
    public static ArrayList<UniqueId> Discover(){
        ClassScanner cs= new ClassScanner();
        try {
            return cs.scanPackageForUniqueIdentity(com.aurawin.scs.stored.cloud.service.Package.class);
        } catch (Exception ex){
            return new ArrayList<>();
        }
    }
    public static ArrayList<Service> Force(Node node){
        ArrayList<Service> r = new ArrayList<>();
        ClassScanner cs= new ClassScanner();
        try {
            ArrayList<UniqueId> uids=Discover();
            ArrayList<Service> svcs = Entities.Cloud.Service.listAll(node);
            for (UniqueId uid : uids){
                String ns = "";
                Service svc = svcs.stream()
                        .filter(s->s.getNamespace().getNamespace().equalsIgnoreCase(uid.getNamespace()))
                        .findFirst()
                        .orElse(null);
                if (svc==null){
                    svc=new Service();
                    svc.setNamespace(uid);
                    svc.setOwner(node);
                    svc.setKind(getKind(uid));
                    svc.setPort(svc.getKind().getPort());
                    Entities.Save(svc,CascadeOff);
                }
                r.add(svc);

            }
        } catch (Exception ex){

        }
        return r;
    }
    public static Table.Stored.Cloud.Service.Kind getKind(UniqueId uid){
        try {
            Class c = DefaultService.class.getClassLoader().loadClass(uid.Namespace);
            if (c != null) {
                DefaultService ds = (DefaultService) c.getConstructor().newInstance();
                return ds.Kind;
            } else {
                return svcNone;
            }
        } catch (Exception ex){
            return svcNone;
        }
    }
    public static int getPort(String namespace){
        try {
            Class c = DefaultService.class.getClassLoader().loadClass(namespace);
            if (c != null) {
                DefaultService ds = (DefaultService) c.getConstructor().newInstance();
                return ds.Kind.getPort();
            } else {
                return 0;
            }
        } catch (Exception ex){
            return 0;
        }
    }


}
