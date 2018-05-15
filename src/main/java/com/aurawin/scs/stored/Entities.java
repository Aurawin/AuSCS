package com.aurawin.scs.stored;

import com.aurawin.core.lang.Table;
import com.aurawin.core.log.Syslog;
import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.annotations.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.KeyValue;
import com.aurawin.scs.stored.domain.network.File;
import com.aurawin.scs.stored.domain.network.Folder;
import com.aurawin.scs.stored.domain.network.Member;
import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.scs.stored.domain.user.Account;
import com.aurawin.scs.stored.domain.user.Roster;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by atbrunner on 3/8/17.
 */
public class Entities extends com.aurawin.core.stored.entities.Entities{
    @SuppressWarnings("unchecked")
    public static ArrayList<Stored> Lookup(QueryByDomainId aQuery, long Id){
        Session ssn = acquireSession();
        if (ssn==null) return new ArrayList<Stored>();
        try {
            Query q = ssn.getNamedQuery(aQuery.Name())
                    .setParameter("DomainId", Id);
            if (q != null) {
                return new ArrayList(q.list());
            } else {
                return null;
            }
        } finally{
            ssn.close();
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<Stored> Lookup(QueryByFileId aQuery, long Id){
        Session ssn = acquireSession();
        if (ssn==null) return new ArrayList<Stored>();
        try {
            Query q = ssn.getNamedQuery(aQuery.Name())
                    .setParameter("FileId", Id);
            if (q != null) {
                return new ArrayList(q.list());
            } else {
                return null;
            }
        } finally {
            ssn.close();
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<Stored> Lookup(QueryByFolderId aQuery, long Id){
        Session ssn = acquireSession();
        if (ssn==null) return new ArrayList<Stored>();
        try {
            return new ArrayList(ssn.getNamedQuery(aQuery.Name())
                    .setParameter("FolderId", Id)
                    .list()
            );
        } finally {
            ssn.close();
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<Stored> Lookup(QueryByNetworkId aQuery, long Id){
        Session ssn = acquireSession();
        if (ssn==null) return new ArrayList<Stored>();
        try {
            Query q = ssn.getNamedQuery(aQuery.Name())
                    .setParameter("NetworkId", Id);
            if (q != null) {
                return new ArrayList(q.list());
            } else {
                return null;
            }
        } finally {
            ssn.close();
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<Stored> LookupByParentId(Class<? extends  Stored>CofE, long ParentId){
        Session ssn = acquireSession();
        if (ssn==null) return new ArrayList<Stored>();
        try {
            QueryByParentId qc = CofE.getAnnotation(QueryByParentId.class);
            Query q = ssn.getNamedQuery(qc.Name())
                    .setParameter("ParentId", ParentId);
            if (q != null) {
                return new ArrayList(q.list());
            } else {
                return null;
            }
        } finally {
            ssn.close();
        }
    }

    public static ArrayList<Stored> LookupByNetworkId(Class<? extends  Stored>CofE, long NetworkId){
        Session ssn = acquireSession();
        if (ssn==null) return new ArrayList<Stored>();
        try {
            QueryByNetworkId qc = CofE.getAnnotation(QueryByNetworkId.class);
            Query q = ssn.getNamedQuery(qc.Name())
                    .setParameter("NetworkId", NetworkId);
            if (q != null) {
                return new ArrayList(q.list());
            } else {
                return null;
            }
        } finally {
            ssn.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Stored>T Lookup(Class<? extends Stored> CofE,long DomainId, long Id){
        Session ssn = acquireSession();
        if (ssn==null) return null;
        try {
            QueryByDomainIdAndId qc = CofE.getAnnotation(QueryByDomainIdAndId.class);
            Query q = ssn.getNamedQuery(qc.Name())
                    .setParameter("DomainId", DomainId)
                    .setParameter("Id", Id);

            return (T) CofE.cast(q.uniqueResult());
        } finally {
            ssn.close();
        }
    }
    @SuppressWarnings("unchecked")
    public static <T extends Stored>T Lookup(Class<? extends Stored> CofE,long DomainId, String Name){
        Session ssn = acquireSession();
        if (ssn==null) return null;
        try {
            QueryByDomainIdAndName qc = CofE.getAnnotation(QueryByDomainIdAndName.class);
            Query q = ssn.getNamedQuery(qc.Name())
                    .setParameter("DomainId", DomainId)
                    .setParameter("Name", Name);

            Stored r = (Stored) q.uniqueResult();
            return (T) CofE.cast(r);
        } finally {
            ssn.close();
        }
    }
    public static class Cloud {
        public static class Disk{
            public static com.aurawin.scs.stored.cloud.Disk byService(com.aurawin.scs.stored.cloud.Service s){
                if (s==null) return null;
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.Cloud.Disk.ByServiceId.name);
                    if (q!=null){
                        q.setParameter("ServiceId",s.getId());
                        return ( com.aurawin.scs.stored.cloud.Disk) q.stream()
                                .findFirst()
                                .orElse(null);

                    }

                } finally{
                    ssn.close();
                }
                return null;
            }
            public static ArrayList<com.aurawin.scs.stored.cloud.Disk> listAll() {
                QueryAll qa = (QueryAll) com.aurawin.scs.stored.cloud.Disk.class.getAnnotation(QueryAll.class);
                return (ArrayList<com.aurawin.scs.stored.cloud.Disk>) Entities.Lookup(qa).stream()
                        .filter(s -> s instanceof com.aurawin.scs.stored.cloud.Disk)
                        .map(com.aurawin.scs.stored.cloud.Disk.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

            }
        }
        public static class Node{
            public static ArrayList<com.aurawin.scs.stored.cloud.Node> listAll(com.aurawin.scs.stored.cloud.Resource owner) {
                QueryByOwnerId qa = (QueryByOwnerId) com.aurawin.scs.stored.cloud.Node.class.getAnnotation(QueryByOwnerId.class);
                return (ArrayList<com.aurawin.scs.stored.cloud.Node>) Entities.Lookup(qa,owner.getId()).stream()
                        .filter(s -> s instanceof com.aurawin.scs.stored.cloud.Node)
                        .map(com.aurawin.scs.stored.cloud.Node.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

            }

            public static boolean Exists(com.aurawin.scs.stored.cloud.Resource resource, String name) {
                QueryByOwnerId qa = (QueryByOwnerId) com.aurawin.scs.stored.cloud.Node.class.getAnnotation(QueryByOwnerId.class);
                return Entities.Lookup(qa,resource.getId()).stream()
                        .filter(s -> s instanceof com.aurawin.scs.stored.cloud.Node)
                        .filter(n-> ((com.aurawin.scs.stored.cloud.Node) n).getName().equalsIgnoreCase(name))
                        .map(com.aurawin.scs.stored.cloud.Node.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new)).size()!=0;
            }
        }
        public static class Resource{
            public static boolean Exists(com.aurawin.scs.stored.cloud.Group cluster, String name) {
                QueryByOwnerId qa = (QueryByOwnerId) com.aurawin.scs.stored.cloud.Resource.class.getAnnotation(QueryByOwnerId.class);
                return Entities.Lookup(qa,cluster.getId()).stream()
                        .filter(s -> s instanceof com.aurawin.scs.stored.cloud.Resource)
                        .filter(r-> ((com.aurawin.scs.stored.cloud.Resource) r).getName().equalsIgnoreCase(name))
                        .map(com.aurawin.scs.stored.cloud.Resource.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new)).size()!=0;
            }
            public static ArrayList<com.aurawin.scs.stored.cloud.Resource> listAll(com.aurawin.scs.stored.cloud.Group owner) {
                QueryByOwnerId qa = (QueryByOwnerId) com.aurawin.scs.stored.cloud.Resource.class.getAnnotation(QueryByOwnerId.class);
                return (ArrayList<com.aurawin.scs.stored.cloud.Resource>) Entities.Lookup(qa,owner.getId()).stream()
                        .filter(r -> r instanceof com.aurawin.scs.stored.cloud.Resource)
                        .map(com.aurawin.scs.stored.cloud.Resource.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

            }

        }
        public static class Group {

            public static ArrayList<com.aurawin.scs.stored.cloud.Group> listAll() {
                QueryAll qa = (QueryAll) com.aurawin.scs.stored.cloud.Group.class.getAnnotation(QueryAll.class);

                return (ArrayList<com.aurawin.scs.stored.cloud.Group>) Entities.Lookup(qa).stream()
                        .filter(s -> s instanceof com.aurawin.scs.stored.cloud.Group)
                        .map(com.aurawin.scs.stored.cloud.Group.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

            }
        }

        public static class Service {
            public static ArrayList<com.aurawin.scs.stored.cloud.Service> listAll(com.aurawin.scs.stored.cloud.Node n) {
                QueryByOwnerId q = (QueryByOwnerId) com.aurawin.scs.stored.cloud.Service.class.getAnnotation(QueryByOwnerId.class);
                return Entities.Lookup(q, n.getId()).stream()
                        .filter(s -> s instanceof com.aurawin.scs.stored.cloud.Service)
                        .map(com.aurawin.scs.stored.cloud.Service.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

            }
            public static com.aurawin.scs.stored.cloud.Service byOwnerIdAndKind(com.aurawin.scs.stored.cloud.Node Owner, com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind Kind){
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.Cloud.Service.ByOwnerIdAndKind.name);
                    if (q!=null){
                        q.setParameter("OwnerId",Owner.getId());
                        q.setParameter("Kind",Kind);
                        return ( com.aurawin.scs.stored.cloud.Service) q.stream()
                                .findFirst()
                                .orElse(null);

                    }

                } finally{
                    ssn.close();
                }
                return null;
            }
        }

    }
    public static class Domains{
        public static class Network{
            public static class Files{
                public static ArrayList<File> listAll(Folder f){
                    return Entities.Lookup(
                            File.class.getAnnotation(QueryByFolderId.class),
                            f.getId()
                    ).stream()
                            .filter(fl->fl instanceof File)
                            .map(File.class::cast)
                            .collect(Collectors.toCollection(ArrayList::new));

                }
            }
        }
        public static class Users{
            public static ArrayList<Account> listAll(Domain d){
                if (d==null) return new ArrayList<>();
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.Domain.User.Account.All.name);
                    if (q!=null){
                        q.setParameter("DomainId",d.getId());
                        return (ArrayList<Account>)q.list().stream()
                                .filter(k-> k instanceof Account)
                                .map(Account.class::cast)
                                .collect(Collectors.toCollection(ArrayList::new));

                    }

                } finally{
                    ssn.close();
                }
                return new ArrayList<>();
            }


        }
        public static class KeyValues{
            public static ArrayList<KeyValue>listAll(Domain d){
                if (d==null) return new ArrayList<>();
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.Domain.KeyValue.ByDomainIdAndNamespaceId.name);
                    if (q!=null){
                        q.setParameter("DomainId",d.getId());
                        q.setParameter("NamespaceId",Namespace.Entities.Identify(KeyValue.class));
                        return (ArrayList<KeyValue>)q.list().stream()
                                .filter(k-> k instanceof KeyValue)
                                .map(KeyValue.class::cast)
                                .collect(Collectors.toCollection(ArrayList::new));

                    }

                } finally{
                    ssn.close();
                }
                return new ArrayList<>();
            }
        }

        public static class Certificates{
            public static ArrayList<Certificate> listAll(Domain d) {
                QueryByDomainId qa = (QueryByDomainId) Certificate.class.getAnnotation(QueryByDomainId.class);
                return (ArrayList<Certificate>) Entities.Lookup(qa,d.getId()).stream()
                    .filter(s -> s instanceof Certificate)
                    .map(Certificate.class::cast)
                    .collect(Collectors.toCollection(ArrayList::new));
            }

        }
        public static ArrayList<Domain> listAll() {
            QueryAll qa = (QueryAll) Domain.class.getAnnotation(QueryAll.class);

            return (ArrayList<Domain>) Entities.Lookup(qa).stream()
                    .filter(s -> s instanceof Domain)
                    .map(Domain.class::cast)
                    .collect(Collectors.toCollection(ArrayList::new));

        }
    }
    public static class Security{
        public static class Filter{
            public static ArrayList<com.aurawin.scs.stored.security.Filter> listAll(long namespaceId){
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.Security.Filter.ListAll.name);
                    q.setParameter("NamespaceId",namespaceId);
                    return (ArrayList<com.aurawin.scs.stored.security.Filter>) q.stream()
                            .filter(s -> s instanceof com.aurawin.scs.stored.security.Filter)
                            .map(com.aurawin.scs.stored.security.Filter.class::cast)
                            .collect(Collectors.toCollection(ArrayList::new));
                }finally{
                    ssn.close();
                }
            }
            public void incrementCounter(com.aurawin.scs.stored.security.Filter f){
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.Security.Filter.Increment.name);
                    q.setParameter("Id",f.getId());
                    q.executeUpdate();

                }finally{
                    ssn.close();
                }
            }
        }
    }

    public static class Settings{
        public static class DNS{
            public static com.aurawin.scs.stored.ContentType Lookup(long host){
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.DNS.ByIp.name);
                    q.setParameter("Ip", host);
                    return (com.aurawin.scs.stored.ContentType) q.uniqueResult();
                }finally{
                    ssn.close();
                }
            }
            public static ArrayList<com.aurawin.scs.stored.DNS> listAll(){
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.getNamedQuery(Database.Query.DNS.All.name);
                    return (ArrayList<com.aurawin.scs.stored.DNS>) q.stream()
                            .filter(s -> s instanceof com.aurawin.scs.stored.DNS)
                            .map(com.aurawin.scs.stored.DNS.class::cast)
                            .collect(Collectors.toCollection(ArrayList::new));
                }finally{
                    ssn.close();
                }
            }

        }
        public static class ContentType {
            public static com.aurawin.scs.stored.ContentType Lookup(String major, String minor, String ext){
                Session ssn = Entities.openSession();
                try{
                    Query q = ssn.createQuery("from ContentType where Major=:major and Minor=:minor and Ext=:ext");
                    q.setParameter("major", major);
                    q.setParameter("minor",minor);
                    q.setParameter("ext",ext);
                    return (com.aurawin.scs.stored.ContentType) q.uniqueResult();
                }finally{
                    ssn.close();
                }
            }
            public static ArrayList<com.aurawin.scs.stored.ContentType> listAll(){
                QueryAll qa = (QueryAll) com.aurawin.scs.stored.ContentType.class.getAnnotation(QueryAll.class);
                return (ArrayList<com.aurawin.scs.stored.ContentType>) Entities.Lookup(qa).stream()
                        .filter(s -> s instanceof com.aurawin.scs.stored.ContentType)
                        .map(com.aurawin.scs.stored.ContentType.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

            }
        }
    }


}
