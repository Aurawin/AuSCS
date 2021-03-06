package com.aurawin.scs.stored.cloud;

import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.time.Time;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

import static com.aurawin.core.stored.entities.Entities.UseCurrentTransaction;

@Entity
@Namespaced
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=false) // just overwrite -> faster
@Table(name= Database.Table.Cloud.Uptime)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.Cloud.Uptime.ById.name,
        Fields = { "Id" }
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Cloud.Uptime.ById.name,
                        query = Database.Query.Cloud.Uptime.ById.value
                )
        }
)
public class Uptime extends Stored implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Table.Cloud.Uptime)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.Cloud.Uptime.NodeId)
    protected long NodeId;
    public long getNodeId(){return NodeId;}
    public void setNodeId(long nodeId){ NodeId=nodeId;}

    @Column(name = Database.Field.Cloud.Uptime.Stamp)
    protected Instant Stamp;
    public Instant getStamp() {
        return Stamp;
    }
    public void setStamp(Instant stamp) {
        Stamp = stamp;
    }

    public Uptime(long id) {
        Id = id;
        Stamp= Time.instantUTC();
    }

    public Uptime() {
        Id = 0;
        Stamp=Time.instantUTC();
        NodeId=0;
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
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception {
        if (Entity instanceof Node){
            Node n = (Node) Entity;
            if (n.Uptime==null){
                n.Uptime=new Uptime();
                n.Uptime.NodeId=n.Id;
                Entities.Save(n.Uptime,Cascade);
                Entities.Update(n,Cascade);
            }
        }
    }
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Node){
            Node n = (Node) Entity;

            //Entities.Delete(n.Uptime,Entities.CascadeOff,UseCurrentTransaction);
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
