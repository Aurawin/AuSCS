package com.aurawin.scs.stored;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.stored.annotations.*;
import com.aurawin.scs.stored.domain.Domain;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
        }
    }
    public static class Domains{

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


}
