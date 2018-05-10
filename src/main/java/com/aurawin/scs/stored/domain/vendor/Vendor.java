package com.aurawin.scs.stored.domain.vendor;

import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.Stored;

import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
import com.aurawin.core.stored.annotations.QueryByDomainId;

import com.aurawin.core.time.Time;
import com.aurawin.scs.stored.Entities;

import com.aurawin.scs.stored.domain.Domain;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.UseCurrentTransaction;

@javax.persistence.Entity
@Namespaced
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
                ),
                @NamedQuery(
                        name = Database.Query.Domain.Vendor.ByDomainId.name,
                        query= Database.Query.Domain.Vendor.ByDomainId.value
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
@QueryByDomainId(
        Name=Database.Query.Domain.Vendor.ByDomainId.name
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
    public void setDomainId(long val){ DomainId=val;}

    @Column(name = Database.Field.Domain.Entities.Vendor.NetworkId)
    protected long NetworkId;
    public long getNetworkId(){return NetworkId;}

    @Column(name = Database.Field.Domain.Entities.Vendor.OwnerId)
    protected long OwnerId;
    public long getOwnerId(){return OwnerId;}
    public void setOwnerId(long val){ OwnerId=val;}

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

    public Vendor() {
        Id=0;
        DomainId=0;
        NetworkId=0;
        OwnerId=0;
        Created= Time.dtUTC();
        Modified=Created;
        Namespace="";
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
    public static void entityCreated(Stored Entity, boolean Cascade){}
    public static void entityDeleted(Stored Entity, boolean Cascade)throws Exception {
        if (Entity instanceof Domain) {
            Domain d = (Domain) Entity;

            ArrayList<Stored> lst = Entities.Lookup(
                        Vendor.class.getAnnotation(QueryByDomainId.class),
                        d.getId()
            );

            for (Stored h : lst) {
                Entities.Delete(h,Cascade,UseCurrentTransaction);
            }

        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade) throws Exception{

    }
}
