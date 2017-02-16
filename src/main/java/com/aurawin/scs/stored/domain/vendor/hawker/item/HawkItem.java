package com.aurawin.scs.stored.domain.vendor.hawker.item;

import com.aurawin.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.vendor.hawker.Hawker;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name= Database.Table.Domain.Vendor.Hawker.Item.Items)
@EntityDispatch(
        onCreated = false,
        onDeleted = true,
        onUpdated = false
)
public class HawkItem extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.Id)
    protected long Id;
    @Override
    public long getId(){
        return Id;
    }

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.DomainId)
    protected long DomainId;
    public long getDomainId(){return DomainId;}

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.VendorId)
    protected long VendorId;
    public long getVendorId(){return VendorId;}

    @ManyToOne(targetEntity=Hawker.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = Database.Field.Domain.Entities.Vendor.Hawker.HawkItem.OwnerId)
    protected Hawker Owner;

    @OneToMany(
            targetEntity = HawkItemField.class,
            cascade =CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy="Owner"
    )
    protected List<HawkItemField> fields = new ArrayList<HawkItemField>();

    public HawkItemField addField(String name, FieldValueKind kind){
        HawkItemField f = new HawkItemField(name,kind);
        fields.add(f);
        return f;
    }
    public void fromJSON(String src) throws Exception{
        Assign(new Gson().fromJson(src,HawkItem.class));
    }
    public void Empty(){
        Id=0;
        DomainId=0;
        VendorId=0;
        fields.clear();
    }
    public void Assign(HawkItem m){
        Empty();
        Id=m.Id;
        DomainId=m.DomainId;
        VendorId=m.VendorId;
        for (HawkItemField f : m.fields){
            fields.add(new HawkItemField(f));
        }
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
                            HawkItem.class.getAnnotation(QueryByDomainId.class),
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
