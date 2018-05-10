package com.aurawin.scs.stored.domain.vendor.hawker;


import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
import com.aurawin.scs.stored.Entities;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.vendor.hawker.item.HawkItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.UseCurrentTransaction;

@Entity
@Namespaced
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name= Database.Table.Domain.Vendor.Hawker.Items)
@EntityDispatch(
        onCreated = false,
        onDeleted = true,
        onUpdated = false
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.Hawker.ByNamespace.name,
                        query = Database.Query.Domain.Vendor.Hawker.ByNamespace.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.Hawker.ByDomainId.name,
                        query = Database.Query.Domain.Vendor.Hawker.ByDomainId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.Hawker.ById.name,
                        query = Database.Query.Domain.Vendor.Hawker.ById.value
                )

        }
)
@QueryById(
        Name = Database.Query.Domain.Vendor.Hawker.ById.name,
        Fields = {"DomainId","VendorId","Id"}
)
@QueryByName(
        Name = Database.Query.Domain.Vendor.Hawker.ByNamespace.name,
        Fields ={"DomainId","VendorId","Namespace"}
)
@QueryByDomainId(
        Name = Database.Query.Domain.Vendor.Hawker.ByDomainId.name
)

public class Hawker extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.Id)
    protected long Id;
    @Override
    public long getId(){
        return Id;
    }
    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.DomainId)
    protected long DomainId;
    public long getDomainId(){return DomainId;}
    public void setDomainId(long val){ DomainId=val;}

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.VendorId)
    protected long VendorId;
    public long getVendorId(){return VendorId;}
    public void setVendorId(long val){ VendorId=val;}

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.Namespace)
    protected String Namespace;
    public String getNamespace(){return Namespace;}
    public void setNamespace(String val){Namespace=val;}

    @OneToMany(targetEntity=HawkItem.class,cascade = CascadeType.ALL,fetch=FetchType.EAGER,mappedBy = "Owner")
    public List<HawkItem> Items = new ArrayList<HawkItem>();

    public Hawker(){
        Id=0;
        DomainId=0;
        VendorId=0;
        Namespace="";
    }

    public void Empty(){
        Id=0;
        DomainId=0;
        VendorId = 0;
        Namespace="";
    }
    public void Assign(Hawker src){
        Id=src.Id;
        DomainId=0;
        VendorId=src.VendorId;
        Namespace=src.Namespace;
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
    public static void entityCreated(Stored Entity, boolean Cascade){}
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst = Entities.Lookup(
                    Hawker.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored h : lst) {
                Entities.Delete(h,Cascade,UseCurrentTransaction);
            }
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade){}
}
