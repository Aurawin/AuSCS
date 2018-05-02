package com.aurawin.scs.stored.security;


import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.lang.Database;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value=true)
@Table( name = Database.Table.Security.Intrusion)
@org.hibernate.annotations.NamedQueries(
        {
                @org.hibernate.annotations.NamedQuery(
                        name  = Database.Query.Security.Intrusion.ById.name,
                        query = Database.Query.Security.Intrusion.ById.value
                )
        }
)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
public class Intrusion extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Security.Intrusion.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    public long getId() {
        return Id;
    }

    @Column (name= Database.Field.Security.Intrusion.DomainId)
    @Expose(serialize = true, deserialize = true)
    private long DomainId;
    public long getDomainId(){ return DomainId; }

    @Column (name= Database.Field.Security.Intrusion.Ip)
    @Expose(serialize = true, deserialize = true)
    private long Ip;
    public long getIp(){ return Ip; }

    @Column (name= Database.Field.Security.Intrusion.Time)
    @Expose(serialize = true, deserialize = true)
    private Instant Time;
    public Instant getTime(){ return Time; }

    @Column (name= Database.Field.Security.Intrusion.Password, length = 25)
    @Expose(serialize = true, deserialize = true)
    private String Password;
    public String getPassword(){ return Password; }

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
}
