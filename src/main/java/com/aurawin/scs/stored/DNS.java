package com.aurawin.scs.stored;

import com.aurawin.core.rsr.IpHelper;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.lang.Database;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Iterator;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;

@Entity
@Namespaced
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = Database.Table.DNS)
@NamedQueries(
        {
            @NamedQuery(
                    name  = Database.Query.DNS.ById.name,
                    query = Database.Query.DNS.ById.value
            ),
            @NamedQuery(
                    name  = Database.Query.DNS.ByIp.name,
                    query = Database.Query.DNS.ByIp.value
            ),
            @NamedQuery(
                    name  = Database.Query.DNS.All.name,
                    query = Database.Query.DNS.All.value
)
        }
                )
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.DNS.ById.name,
        Fields = ("Id")
)

@QueryAll(
        Name = Database.Query.DNS.All.name
)
public class DNS extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.DNS.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.DNS.Host)
    @Expose(serialize = true, deserialize = true)
    protected long Host;
    public long getHost(){return Host;}
    public void setHost(long host){ Host = host;}

    public String getHostName(){
        return IpHelper.fromLong(Host);
    }

    public void Assign(DNS dns){
        Id = dns.Id;
        Host = dns.Host;
    }

    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Transaction tx = (ssn.isJoinedToTransaction())? ssn.getTransaction() : ssn.beginTransaction();
            try {
                ssn.save(this);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                throw e;
            }
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade){ }
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}

}
