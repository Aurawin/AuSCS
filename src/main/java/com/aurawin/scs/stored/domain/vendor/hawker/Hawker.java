package com.aurawin.scs.stored.domain.vendor.hawker;


import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
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

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.VendorId)
    protected long VendorId;
    public long getVendorId(){return VendorId;}
    public void setVendorId(long val){ VendorId=val;}

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.Namespace)
    protected String Namespace;
    public String getNamespace(){return Namespace;}
    public void setNamepsace(String val){Namespace=val;}

    public Hawker(){
        Id=0;
        VendorId=0;
        Namespace="";
    }

    public void Empty(){
        Id=0;
        VendorId = 0;
        Namespace="";
    }
    public void Assign(Hawker src){
        Id=src.Id;
        VendorId=src.VendorId;
        Namespace=src.Namespace;
    }

    public static void entityCreated(Entities List, Stored Entity){}
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            Session ssn = List.Sessions.openSession();
            try {
                Transaction tx = ssn.beginTransaction();
                try {
                    ArrayList<Stored> lst = Entities.Lookup(
                            Hawker.class.getAnnotation(QueryByDomainId.class),
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
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade){}
}
