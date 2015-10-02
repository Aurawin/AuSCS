package com.aurawin.core.stored.entities.vendor;

import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
import com.aurawin.core.stored.entities.Entities;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import javax.xml.crypto.Data;

@Entity
@EntityDispatch(
        onUpdated = false,
        onDeleted = false,
        onCreated = false
)
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name= Database.Table.Domain.Vendor.Items )
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.ByNamespace.name,
                        query = Database.Query.Domain.Vendor.ByNamespace.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.ById.name,
                        query = Database.Query.Domain.Vendor.ById.value
                )
        }
)
@QueryById(
        Name = Database.Query.Domain.Vendor.ById.name,
        Fields = {"DomainId","Id"}
)
@QueryByName(
        Name = Database.Query.Domain.Vendor.ByNamespace.name,
        Fields = {"DomainId","Namespace"}
)

public class Vendor extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Entities.Vendor.Id)
    protected long Id;
    @Override
    public long getId(){return Id;}

    @Column(name = Database.Field.Domain.Entities.Vendor.DomainId)
    protected long DomainId;
    public long getDomainId(){return DomainId;}

    @Column(name = Database.Field.Domain.Entities.Vendor.NetworkId)
    protected long NetworkId;
    public long getNetworkId(){return NetworkId;}

    @Column(name = Database.Field.Domain.Entities.Vendor.OwnerId)
    protected long OwnerId;
    public long getOwnerId(){return OwnerId;}

    @Column(name=Database.Field.Domain.Entities.Vendor.Created)
    protected long Created;
    public long getCreated(){return Created;}

    @Column(name=Database.Field.Domain.Entities.Vendor.Modified)
    protected long Modified;
    public long getModified(){return Modified;}
    public void setModified(long val){ Modified=val;}

    @Column(name= Database.Field.Domain.Entities.Vendor.Namespace)
    protected String Namespace;
    public String getNamespace(){return Namespace;}
    public void setNamespace(String val){ Namespace=val;}

    public static void entityCreated(Entities List, Stored Entity){}
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade){}
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade){}
}
