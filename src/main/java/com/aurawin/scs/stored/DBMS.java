package com.aurawin.scs.stored;

import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.annotations.AnnotatedList;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.UniqueId;

import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.*;
import com.aurawin.scs.stored.domain.network.Folder;
import com.aurawin.scs.stored.domain.network.Member;
import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.scs.stored.domain.vendor.Vendor;
import com.aurawin.scs.stored.domain.vendor.hawker.Hawker;
import com.aurawin.scs.stored.domain.vendor.hawker.item.HawkItem;
import com.aurawin.scs.stored.domain.vendor.hawker.item.HawkItemField;

public class DBMS {
    private Manifest Manifest;
    public Entities Entities;
    public static Manifest createManifest(
            String username,
            String password,
            String host,
            int port,
            boolean autocommit,
            int poolsizeMin,
            int poolsizeMax,
            int poolAcrement,
            int statementsMax,
            int timeout,
            String automation,
            String database,
            String dialect,
            String driver

    ){
        AnnotatedList al = new AnnotatedList();

        // basic
        al.add(UniqueId.class);
        al.add(Domain.class);
        al.add(UserAccount.class);
        al.add(RosterField.class);
        al.add(Roster.class);
        al.add(Network.class);

        al.add(Avatar.class);
        al.add(Member.class);
        al.add(Folder.class);

        // cloud.*
        al.add(Group.class);
        al.add(Location.class);
        al.add(Node.class);
        al.add(Resource.class);
        al.add(Service.class);
        al.add(Transactions.class);
        al.add(Uptime.class);

        // vendor.*
        al.add(Vendor.class);
        al.add(Hawker.class);
        al.add(HawkItem.class);
        al.add(HawkItemField.class);

        Manifest m = new Manifest(
                username,
                password,
                host,
                port,
                autocommit,
                poolsizeMin,
                poolsizeMax,
                poolAcrement,
                statementsMax,
                timeout,
                automation,
                database,
                dialect,
                driver,
                al
        );

        return m;
    }

    public void setManifest(Manifest m){
        Manifest = m;
        Entities = new Entities(m);
    }
}
