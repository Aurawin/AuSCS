package com.aurawin.core.stored.entities.vendor.hawker;


import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.entities.Entities;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name= Database.Table.Domain.Vendor.Hawker.Items)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.Hawker.ByNamespace.name,
                        query = Database.Query.Domain.Vendor.Hawker.ByNamespace.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.Hawker.ById.name,
                        query = Database.Query.Domain.Vendor.Hawker.ById.value
                )
        }
)
@QueryById(
        Name = Database.Query.Domain.Vendor.Hawker.ById.name,
        Fields = {""}
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

    public Hawker(){
        Id=0;
        VendorId=0;
    }

    public void Empty(){
        Id=0;
    }
    public void Assign(Hawker src){
        Id=src.Id;
    }

    public static void entityCreated(Entities List, Stored Entity){}
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade){}
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade){}
}
