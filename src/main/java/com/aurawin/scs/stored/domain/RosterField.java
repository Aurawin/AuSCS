package com.aurawin.scs.stored.domain;

import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.ArrayList;

@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = com.aurawin.lang.Database.Table.Domain.UserAccount.Roster.Field)
@EntityDispatch(
        onCreated = false,
        onDeleted = true,
        onUpdated = false
)
@NamedQueries(
        {
                @NamedQuery(
                        name = com.aurawin.lang.Database.Query.Domain.UserAccount.Roster.RosterField.ByDomainId.name,
                        query = com.aurawin.lang.Database.Query.Domain.UserAccount.Roster.RosterField.ByDomainId.value
                ),
                @NamedQuery(
                        name = com.aurawin.lang.Database.Query.Domain.UserAccount.Roster.RosterField.ByOwnerId.name,
                        query = com.aurawin.lang.Database.Query.Domain.UserAccount.Roster.RosterField.ByOwnerId.value
                )
        }
)
@QueryByDomainId(
    Name = com.aurawin.lang.Database.Query.Domain.UserAccount.Roster.RosterField.ByDomainId.name
)
@QueryByOwnerId(
        Name = com.aurawin.lang.Database.Query.Domain.UserAccount.Roster.RosterField.ByOwnerId.name
)
public class RosterField extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = com.aurawin.lang.Database.Field.Domain.RosterField.Id)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @ManyToOne()
    @JoinColumn(name = com.aurawin.lang.Database.Field.Domain.RosterField.OwnerId)
    private Roster Owner;

    @Column(name = com.aurawin.lang.Database.Field.Domain.RosterField.DomainId)
    private long DomainId;

    @Column(name = com.aurawin.lang.Database.Field.Domain.RosterField.Key)
    private String Key;

    @Column(name = com.aurawin.lang.Database.Field.Domain.RosterField.Value)
    private String Value;

    public RosterField() {
    }

    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Transaction tx = ssn.beginTransaction();
            try {
                ssn.save(this);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                throw e;
            }
        }
    }
    public static void entityCreated(Entities List,Stored Entity) {}
    public static void entityUpdated(Entities List,Stored Entity, boolean Caascade) {}
    public static void entityDeleted(Entities List,Stored Entity, boolean Caascade) throws Exception {
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst =List.Lookup(
                    RosterField.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored h : lst) {
                List.Delete(h,Entities.CascadeOff);
            }
        } else if (Entity instanceof Roster){
            Roster r = (Roster) Entity;
            ArrayList<Stored> lst =List.Lookup(
                    RosterField.class.getAnnotation(QueryByOwnerId.class),
                    r.getId()
            );
            for (Stored h : lst) {
                List.Delete(h,Entities.CascadeOff);
            }
        }

    }

}
