package com.aurawin.scs.stored;

import com.aurawin.core.stored.Stored;
import com.aurawin.scs.stored.annotations.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;

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
}
