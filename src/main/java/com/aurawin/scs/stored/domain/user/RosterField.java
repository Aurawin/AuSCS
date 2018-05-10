package com.aurawin.scs.stored.domain.user;

import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.Stored;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.domain.Domain;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.UseCurrentTransaction;
import static org.hibernate.annotations.CascadeType.ALL;

@javax.persistence.Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = com.aurawin.scs.lang.Database.Table.Domain.User.Account.Roster.Field)
@EntityDispatch(
        onCreated = false,
        onDeleted = true,
        onUpdated = false
)
@NamedQueries(
        {
                @NamedQuery(
                        name = com.aurawin.scs.lang.Database.Query.Domain.User.Roster.RosterField.ByDomainId.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Roster.RosterField.ByDomainId.value
                ),
                @NamedQuery(
                        name = com.aurawin.scs.lang.Database.Query.Domain.User.Roster.RosterField.ByOwnerId.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Roster.RosterField.ByOwnerId.value
                )
        }
)
@QueryByDomainId(
    Name = com.aurawin.scs.lang.Database.Query.Domain.User.Roster.RosterField.ByDomainId.name
)
@QueryByOwnerId(
        Name = com.aurawin.scs.lang.Database.Query.Domain.User.Roster.RosterField.ByOwnerId.name
)
public class RosterField extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.RosterField.Id)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @ManyToOne()
    @Cascade(ALL)
    @JoinColumn(name = Database.Field.Domain.User.RosterField.OwnerId)
    @Fetch(value=FetchMode.JOIN)
    private Roster Owner;

    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.RosterField.DomainId)
    private long DomainId;

    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.RosterField.Key)
    private String Key;

    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.RosterField.Value)
    private String Value;

    public RosterField() {
    }

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
    public static void entityCreated(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Caascade) {}
    public static void entityDeleted(Stored Entity, boolean Caascade) throws Exception {
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst =Entities.Lookup(
                    RosterField.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored h : lst) {
                Entities.Delete(h,Entities.CascadeOff,UseCurrentTransaction);
            }
        } else if (Entity instanceof Roster){
            Roster r = (Roster) Entity;
            ArrayList<Stored> lst =Entities.Lookup(
                    RosterField.class.getAnnotation(QueryByOwnerId.class),
                    r.getId()
            );
            for (Stored h : lst) {
                Entities.Delete(h,Entities.CascadeOff,UseCurrentTransaction);
            }
        }

    }

}
