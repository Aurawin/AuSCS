package com.aurawin.scs.stored.bootstrap;


import com.aurawin.core.plugin.ClassScanner;
import com.aurawin.core.stored.annotations.AnnotatedList;

import java.util.ArrayList;


public class Stored {

    public static AnnotatedList buildAnnotations(String rootPackage) {
        AnnotatedList aL = new AnnotatedList();
        // todo change name of ClassScanner to PluginClassScanner
        ClassScanner cs= new ClassScanner();
        try {
            Class[] ca = cs.scanPackage(rootPackage);
            for (Class c : ca) {
                aL.add(c);
            }
        } catch (Exception ex){

        }

        aL.add(com.aurawin.scs.stored.ContentType.class);
/*
        aL.add(com.aurawin.scs.stored.cloud.Disk.class);
        aL.add(com.aurawin.scs.stored.cloud.Group.class);
        aL.add(com.aurawin.scs.stored.cloud.Location.class);
        aL.add(com.aurawin.scs.stored.cloud.Node.class);
        aL.add(com.aurawin.scs.stored.cloud.Resource.class);
        aL.add(com.aurawin.scs.stored.cloud.Service.class);
        aL.add(com.aurawin.scs.stored.cloud.Transactions.class);
        aL.add(com.aurawin.scs.stored.cloud.Uptime.class);


        aL.add(com.aurawin.scs.stored.domain.Domain.class);
        aL.add(com.aurawin.scs.stored.domain.user.Role.class);
        aL.add(com.aurawin.scs.stored.domain.user.RoleMap.class);
        aL.add(com.aurawin.scs.stored.domain.user.Account.class);
        aL.add(com.aurawin.scs.stored.domain.user.Avatar.class);
        aL.add(com.aurawin.scs.stored.domain.user.Roster.class);
        aL.add(com.aurawin.scs.stored.domain.user.RosterField.class);

        aL.add(com.aurawin.scs.stored.domain.network.Network.class);
        aL.add(com.aurawin.scs.stored.domain.network.File.class);
        aL.add(com.aurawin.scs.stored.domain.network.Folder.class);
        aL.add(com.aurawin.scs.stored.domain.network.Member.class);

        aL.add(com.aurawin.scs.stored.domain.vendor.hawker.item.HawkItem.class);
        aL.add(com.aurawin.scs.stored.domain.vendor.hawker.item.HawkItemField.class);
        aL.add(com.aurawin.scs.stored.domain.vendor.hawker.Hawker.class);
        aL.add(com.aurawin.scs.stored.domain.vendor.Vendor.class);

        aL.add(com.aurawin.scs.stored.security.ACL.class);
        aL.add(com.aurawin.scs.stored.security.Filter.class);
        aL.add(com.aurawin.scs.stored.security.Intrusion.class);
        aL.add(com.aurawin.scs.stored.security.IpLog.class);

*/
        return aL;
    }
}
