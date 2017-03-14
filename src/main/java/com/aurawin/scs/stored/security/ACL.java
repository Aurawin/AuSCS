package com.aurawin.scs.stored.security;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.domain.user.Account;
import com.aurawin.scs.stored.domain.user.Roster;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import static org.hibernate.annotations.CascadeType.ALL;

/**
 * Created by atbrunner on 3/12/17.
 */
@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value=true)
@Table( name = Database.Table.Security.ACL)
@org.hibernate.annotations.NamedQueries(
        {
                @org.hibernate.annotations.NamedQuery(
                        name  = Database.Query.Security.ACL.ById.name,
                        query = Database.Query.Security.ACL.ById.value
                ),
                @org.hibernate.annotations.NamedQuery(
                        name  = Database.Query.Security.ACL.ListAllByOwnerId.name,
                        query = Database.Query.Security.ACL.ListAllByOwnerId.value
                ),
                @org.hibernate.annotations.NamedQuery(
                        name  = Database.Query.Security.ACL.ByNamespaceIdAndOwnerId.name,
                        query = Database.Query.Security.ACL.ByNamespaceIdAndOwnerId.value
                )
        }
)
@QueryByOwnerId(
        Name = Database.Query.Security.ACL.ListAllByOwnerId.name
)
@QueryById(
        Name = Database.Query.Security.ACL.ById.name,
        Fields = {"Id"}
)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
public class ACL extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Security.ACL.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    public long getId() {
        return Id;
    }


    @ManyToOne()
    @Cascade(ALL)
    @Expose(serialize = true, deserialize = true)
    @JoinColumn(name = Database.Field.Security.ACL.OwnerId)
    @Fetch(value=FetchMode.JOIN)
    public Account  Owner;
    public long getOwnerId(){ return Owner.getId();}


    @Column(name = Database.Field.Security.ACL.NamespaceId)
    @Expose(serialize = true, deserialize = true)
    public long NamespaceId;


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
