package com.aurawin.scs.stored.bootstrap;


import com.aurawin.core.stored.annotations.AnnotatedList;

public class Stored {

    public static AnnotatedList buildAnnotations() {
        AnnotatedList aL = new AnnotatedList();
        aL.add(com.aurawin.scs.stored.cloud.Group.class);
        aL.add(com.aurawin.scs.stored.cloud.Location.class);
        aL.add(com.aurawin.scs.stored.cloud.Node.class);
        aL.add(com.aurawin.scs.stored.cloud.Disk.class);
        aL.add(com.aurawin.scs.stored.cloud.Resource.class);
        aL.add(com.aurawin.scs.stored.cloud.Service.class);
        aL.add(com.aurawin.scs.stored.cloud.Transactions.class);
        aL.add(com.aurawin.scs.stored.cloud.Uptime.class);
        aL.add(com.aurawin.scs.stored.cloud.Uptime.class);
        aL.add(com.aurawin.scs.stored.domain.UserAccount.class);
        aL.add(com.aurawin.scs.stored.domain.Avatar.class);
        aL.add(com.aurawin.scs.stored.domain.Domain.class);
        aL.add(com.aurawin.scs.stored.domain.Roster.class);
        aL.add(com.aurawin.scs.stored.domain.RosterField.class);
        aL.add(com.aurawin.scs.stored.domain.network.Network.class);
        aL.add(com.aurawin.scs.stored.domain.network.File.class);
        aL.add(com.aurawin.scs.stored.domain.network.Folder.class);
        aL.add(com.aurawin.scs.stored.domain.network.Member.class);
        aL.add(com.aurawin.scs.stored.domain.vendor.hawker.item.HawkItem.class);
        aL.add(com.aurawin.scs.stored.domain.vendor.hawker.item.HawkItemField.class);
        aL.add(com.aurawin.scs.stored.domain.vendor.hawker.Hawker.class);
        aL.add(com.aurawin.scs.stored.domain.vendor.Vendor.class);


        return aL;
    }
}
