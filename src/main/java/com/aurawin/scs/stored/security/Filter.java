package com.aurawin.scs.stored.security;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;


@Entity
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
public class Filter extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Security.Filter.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    public long getId(){return Id;}


    @Column (name= Database.Field.Security.Filter.Expires)
    @Expose(serialize = true, deserialize = true)
    private Instant Expires;
    public Instant getExpires(){ return Expires; }

    @Column (name= Database.Field.Security.Filter.Enabled)
    @Expose(serialize = true, deserialize = true)
    private boolean Enabled;
    public boolean getEnabled(){ return Enabled; }

    @Column (name= Database.Field.Security.Filter.Counter)
    @Expose(serialize = true, deserialize = true)
    public long Counter;

    @Column (name= Database.Field.Security.Filter.Value)
    @Expose(serialize = true, deserialize = true)
    public String Value;

    @Column (name= Database.Field.Security.Filter.Data)
    @Expose(serialize = true, deserialize = true)
    public String Data;

    @Expose(serialize = false, deserialize = false)
    @ManyToOne()
    @JoinColumn(name = Database.Field.Security.Filter.NamespaceId)
    @Fetch(value= FetchMode.JOIN)
    public UniqueId Namespace;

    @Transient
    public boolean Valid;

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
