package com.aurawin.scs.stored.domain;

import com.aurawin.core.lang.*;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.scs.stored.domain.network.Exposure;
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
@Table(name = Database.Table.Domain.UserAccount.Roster.Field)
@EntityDispatch(
        onCreated = false,
        onDeleted = true,
        onUpdated = false
)
@NamedQueries(
        {
                @NamedQuery(
                        name = Database.Query.Domain.UserAccount.Roster.RosterField.ByDomainId.name,
                        query = Database.Query.Domain.UserAccount.Roster.RosterField.ByDomainId.value
                )
        }
)
@QueryByDomainId(
    Name = Database.Query.Domain.UserAccount.Roster.RosterField.ByDomainId.name
)
public class RosterField extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.RosterField.Id)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @ManyToOne()
    @JoinColumn(name = Database.Field.Domain.RosterField.OwnerId)
    private Roster Owner;

    @Column(name = Database.Field.Domain.RosterField.DomainId)
    private long DomainId;

    @Column(name = Database.Field.Domain.RosterField.Key)
    private String Key;

    @Column(name = Database.Field.Domain.RosterField.Value)
    private String Value;

    public RosterField() {
    }

    public static void entityCreated(Entities List,Stored Entity) {}
    public static void entityUpdated(Entities List,Stored Entity, boolean Caascade) {}
    public static void entityDeleted(Entities List,Stored Entity, boolean Caascade) throws Exception {
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            Session ssn = List.Sessions.openSession();
            try {
                Transaction tx = ssn.beginTransaction();
                try {
                    ArrayList<Stored> lst = Entities.Lookup(
                            RosterField.class.getAnnotation(QueryByDomainId.class),
                            List,
                            d.getId()
                    );
                    for (Stored h : lst) {
                        ssn.delete(h);
                    }
                } finally {
                    tx.commit();
                }
            } finally {
                ssn.close();
            }
        }

    }

}
