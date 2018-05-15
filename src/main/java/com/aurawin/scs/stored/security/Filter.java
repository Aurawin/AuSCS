package com.aurawin.scs.stored.security;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

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
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Security.Filter.ById.name,
                        query = Database.Query.Security.Filter.ById.value
                ),
                @NamedQuery(
                        name = Database.Query.Security.Filter.ListAll.name,
                        query = Database.Query.Security.Filter.ListAll.value
                ),
                @NamedQuery(
                        name = Database.Query.Security.Filter.Increment.name,
                        query = Database.Query.Security.Filter.Increment.value
                )
        }
)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.Security.Filter.ById.name,
        Fields = ("Id")
)

@QueryAll(
        Name =Database.Query.Security.Filter.ListAll.name
)
public class Filter extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Security.Filter.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    public long getId(){return Id;}

    @Column (name= Database.Field.Security.Filter.Counter)
    @Expose(serialize = true, deserialize = true)
    protected long Counter;
    public long getCounter(){ return Counter;}

    @Type(type="text")
    @Column (name= Database.Field.Security.Filter.Value)
    @Expose(serialize = true, deserialize = true)
    protected String Value;
    public String getValue(){ return Value;}
    public void setValue(String value){ Value=value;}

    @ManyToOne()
    @JoinColumn(nullable =  false, name = Database.Field.Security.Filter.NamespaceId)
    @Expose(serialize = true, deserialize = true)
    @Fetch(value= FetchMode.JOIN)
    public UniqueId Namespace;

    public Filter(UniqueId namespace) {
        Namespace = namespace;
    }

    public Filter() {}

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
