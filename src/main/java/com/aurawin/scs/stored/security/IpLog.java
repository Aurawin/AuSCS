package com.aurawin.scs.stored.security;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.solution.Settings;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value=true)
@Table( name = Database.Table.Security.IpLog)
@org.hibernate.annotations.NamedQueries(
        {
                @org.hibernate.annotations.NamedQuery(
                        name  = Database.Query.Security.IpLog.ById.name,
                        query = Database.Query.Security.IpLog.ById.value
                ),
                @org.hibernate.annotations.NamedQuery(
                        name  = Database.Query.Security.IpLog.CountLastEntriesByIpAndTime.name,
                        query = Database.Query.Security.IpLog.CountLastEntriesByIpAndTime.value
                )
        }
)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
public class IpLog extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Security.IpLog.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    public long getId() {
        return Id;
    }

    @Column (name= Database.Field.Security.IpLog.Ip)
    @Expose(serialize = true, deserialize = true)
    private long Ip;
    public long getIp(){ return Ip; }

    @Column (name= Database.Field.Security.IpLog.Time)
    @Expose(serialize = true, deserialize = true)
    private Instant Time;
    public Instant getTime(){ return Time; }


    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Transaction tx = (ssn.isJoinedToTransaction()) ? ssn.getTransaction() : ssn.beginTransaction();
            try {
                ssn.save(this);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
            }
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade)throws Exception { }
    public static void entityUpdated(Stored Entity, boolean Cascade)throws Exception { }
    public static void entityDeleted(Stored Entity, boolean Cascade)throws Exception { }

    public static long countRecentEntries(Session ssn, long Ip){
        Instant Time = com.aurawin.core.time.Time.instantUTC().minusSeconds(Settings.Security.IpLog.RecentItemsTimeWindowInSeconds);
        return (Long) ssn.getNamedQuery(
                    Database.Query.Security.IpLog.CountLastEntriesByIpAndTime.name
                )
                .setParameter("Ip",Ip)
                .setParameter("Time",Time)
                .uniqueResult();

    }
}
