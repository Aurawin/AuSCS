package com.aurawin.scs.stored.domain;


import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.lang.Database;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate( value = true)

@javax.persistence.Table(name = Database.Table.Domain.KeyValue)
@NamedQueries(
        {
                @NamedQuery(
                        name = Database.Query.Domain.KeyValue.ByDomainIdAndNamespaceId.name,
                        query = Database.Query.Domain.KeyValue.ByDomainIdAndNamespaceId.value
                ),
                @NamedQuery(
                        name = Database.Query.Domain.KeyValue.DeleteDomainKeyValueByDomainIdAndNamespaceId.name,
                        query = Database.Query.Domain.KeyValue.DeleteDomainKeyValueByDomainIdAndNamespaceId.value
                )
        }
)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
public class KeyValue extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Collection.Id)
    protected long Id;
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.Domain.Collection.DomainId)
    protected long DomainId;
    public long getDomainId(){ return DomainId;}

    @Column(name = Database.Field.Domain.Collection.NamespaceId)
    protected long NamespaceId;
    public long getNamespaceId(){ return NamespaceId;}

    @Column(name = Database.Field.Domain.Collection.Name)
    protected String Name;
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    @Column(name = Database.Field.Domain.Collection.Value)
    protected String Value;
    public String getValue() {
        return Value;
    }
    public void setValue(String value) {
        Value = value;
    }

    public KeyValue() {
        NamespaceId = 0;
        DomainId=0;
        Name="";
        Value="";
    }
    public KeyValue(long domainId, long namespaceId, String name){
        DomainId=domainId;
        NamespaceId=namespaceId;
        Name = name;
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
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception {}
    public static void entityUpdated(Stored Entity, boolean Cascade) throws Exception {}
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception {
        if (Entity instanceof Domain) {
            Domain d = (Domain) Entity;
            Session ssn = Entities.openSession();
            try{
                Transaction tx = ssn.beginTransaction();
                Query q = ssn.getNamedQuery(Database.Query.Domain.KeyValue.DeleteDomainKeyValueByDomainIdAndNamespaceId.name);
                if (q!=null){
                    q.setParameter("DomainId",d.getId());
                    q.executeUpdate();
                    tx.commit();
                }
            }finally{
                ssn.close();
            }

        }
    }

}
