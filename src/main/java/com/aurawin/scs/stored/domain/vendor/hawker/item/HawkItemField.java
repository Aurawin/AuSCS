package com.aurawin.scs.stored.domain.vendor.hawker.item;

import com.aurawin.core.Memo;
import com.aurawin.core.array.KeyItem;
import com.aurawin.core.array.KeyPair;
import com.aurawin.core.array.VarString;
import com.aurawin.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.domain.Domain;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.Hawker.Item.Field.ById.name,
                        query = Database.Query.Domain.Vendor.Hawker.Item.Field.ById.value
                )
        }
)
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name=Database.Table.Domain.Vendor.Hawker.Item.Field.Items)
@QueryById(
        Name = Database.Query.Domain.Vendor.Hawker.Item.Field.ById.name,
        Fields = {"DomainId","VendorId","OwnerId","Id"}
)
public class HawkItemField extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.Fields.Id)
    protected long Id;
    @Override
    public long getId(){return Id;}

    @Column (name=Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.Fields.DomainId)
    protected long DomainId;

    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.Fields.VendorId)
    protected long VendorId;

    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.Fields.Name)
    public String Name;

    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.Fields.DefaultLength)
    protected long defaultLength;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            targetEntity = HawkItem.class
    )
    @JoinColumn(name=Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.Fields.OwnerId)
    protected HawkItem Owner;


    @Transient
    public FieldValue Value;

    public HawkItemField() {
    }

    public HawkItemField(HawkItemField src){
        Name = src.Name;
        Value = new FieldValue(src.Value.getKind());
        Value.setValue(src.Value.getData());
        defaultLength =src.Value.getKind().getLength();
    }
    public HawkItemField(String name, FieldValueKind kind){
        Name = name;
        Value = new FieldValue(kind);
        defaultLength=kind.getLength();
    }
    public HawkItemField(String name, String value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.String);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, Memo value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.Memo);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, VarString value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.StringList);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, int value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.Integer);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, long value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.Int64);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, KeyItem value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.KeyPair);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, KeyPair value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.KeyPairList);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, double value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.Double);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public HawkItemField(String name, boolean value) throws Exception{
        Name = name;
        Value = new FieldValue(FieldValueKind.Boolean);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
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
    public static void entityCreated(Entities List, Stored Entity){}
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade)throws Exception{
        if (Entity instanceof Domain) {
            Domain d = (Domain) Entity;
            Session ssn = List.acquireSession();

                Transaction tx = ssn.beginTransaction();
                try {
                    ArrayList<Stored> lst = List.Lookup(
                            HawkItemField.class.getAnnotation(QueryByDomainId.class),
                            d.getId()
                    );
                    for (Stored h : lst) {
                        ssn.delete(h);
                    }
                } finally {
                    tx.commit();
                }

        }
    }
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade){}
}
