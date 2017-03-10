package com.aurawin.scs.lang;

import com.aurawin.core.stored.entities.UniqueId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Namespace extends com.aurawin.core.lang.Namespace {
    public static class Stored {
        public static class Cloud{
            public static final UniqueId Group = new UniqueId("com.aurawin.scs.stored.cloud.group");
            public static final UniqueId Location = new UniqueId("com.aurawin.scs.stored.cloud.location");
            public static final UniqueId Node = new UniqueId("com.aurawin.scs.stored.cloud.node");
            public static final UniqueId Disk = new UniqueId("com.aurawin.scs.stored.cloud.disk");
            public static final UniqueId Resource = new UniqueId("com.aurawin.scs.stored.cloud.resource");
            public static class Service{
                public static final UniqueId HTTP = new UniqueId("com.aurawin.scs.stored.cloud.service.http");
                public static final UniqueId AUDISK = new UniqueId("com.aurawin.scs.stored.cloud.service.audisk");
            }

            public static final UniqueId Transactions = new UniqueId("com.aurawin.scs.stored.cloud.transactions");
            public static final UniqueId Uptime = new UniqueId("com.aurawin.scs.stored.cloud.uptime");
        }
        public static class Domain {
            public static final UniqueId Avatar = new UniqueId("com.aurawin.scs.stored.domain.avatar");
            public static final UniqueId Item = new UniqueId("com.aurawin.scs.stored.domain");
            public static class User {
                public static final UniqueId Account = new UniqueId("com.aurawin.scs.stored.domain.user.account");
                public static final UniqueId Avatar = new UniqueId("com.aurawin.scs.stored.domain.user.avatar");
                public static final UniqueId Roster = new UniqueId("com.aurawin.scs.stored.domain.roster");
                public static final UniqueId RosterField = new UniqueId("com.aurawin.scs.stored.domain.rosterfield");
            }
            public static class Network{
                public static final UniqueId Avatar = new UniqueId("com.aurawin.scs.stored.domain.network.avatar");
                public static final UniqueId Exposure = new UniqueId("com.aurawin.scs.stored.domain.network.exposure");
                public static final UniqueId File = new UniqueId("com.aurawin.scs.stored.domain.network.file");
                public static final UniqueId Folder = new UniqueId("com.aurawin.scs.stored.domain.network.folder");
                public static final UniqueId Member = new UniqueId("com.aurawin.scs.stored.domain.network.member");
                public static final UniqueId Item = new UniqueId("com.aurawin.scs.stored.domain.network.network");
                public static final UniqueId Permission = new UniqueId("com.aurawin.scs.stored.domain.network.permission");
                public static final UniqueId Standing = new UniqueId("com.aurawin.scs.stored.domain.network.standing");
            }
        }
    }
    public static ArrayList<UniqueId> Discover(){
        ArrayList<UniqueId> l = new ArrayList<>();
        l.add(Stored.Cloud.Group);
        l.add(Stored.Cloud.Location);
        l.add(Stored.Cloud.Node);
        l.add(Stored.Cloud.Resource);
        l.add(Stored.Cloud.Service.HTTP);
        l.add(Stored.Cloud.Transactions);
        l.add(Stored.Cloud.Uptime);

        l.add(Stored.Domain.Avatar);
        l.add(Stored.Domain.Item);

        l.add(Stored.Domain.User.Account);
        l.add(Stored.Domain.User.Avatar);
        l.add(Stored.Domain.User.Roster);
        l.add(Stored.Domain.User.RosterField);

        l.add(Stored.Domain.Network.Avatar);
        l.add(Stored.Domain.Network.Exposure);
        l.add(Stored.Domain.Network.File);
        l.add(Stored.Domain.Network.Folder);
        l.add(Stored.Domain.Network.Member);
        l.add(Stored.Domain.Network.Item);
        l.add(Stored.Domain.Network.Permission);
        l.add(Stored.Domain.Network.Standing);

        return l;
    }
    public static void Merge(ArrayList<UniqueId> Destination){
        ArrayList<UniqueId> l = Discover();
        Destination.addAll(l);
        Set<UniqueId> hs = new HashSet<>();
        hs.addAll(Destination);
        Destination.clear();
        Destination.addAll(hs);
    }
}
