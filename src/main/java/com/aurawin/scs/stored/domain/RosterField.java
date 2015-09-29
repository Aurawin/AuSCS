package com.aurawin.scs.stored.domain;

import com.aurawin.core.lang.*;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.Stored;
import com.aurawin.scs.stored.domain.network.Exposure;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = Database.Table.Domain.RosterField)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
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
    public static void entityDeleted(Entities List,Stored Entity, boolean Caascade) {}

}
