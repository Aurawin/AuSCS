package com.aurawin.scs.stored.cloud;

import com.aurawin.lang.Database;
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
import java.time.Instant;

@Entity
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
public class Uptime extends Stored{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Table.Cloud.Uptime)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Cascade({CascadeType.MERGE})
    @ManyToOne(fetch=FetchType.EAGER,targetEntity=Node.class)
    @JoinColumn(name = Database.Field.Cloud.Uptime.NodeId)
    protected Node Node;
    public Node getNode(){return Node;}
    public void setNode(Node node){ Node=node;}

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
        Node=null;
    }

    public Uptime() {
        Id = 0;
        Stamp=Time.instantUTC();
        Node=null;
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
    public static void entityCreated(Entities List, Stored Entity) throws Exception {
        if (Entity instanceof Node){
            Node n = (Node) Entity;
            if (n.Uptime==null){
                n.Uptime=new Uptime();
                n.Uptime.Node=n;
                List.Save(n.Uptime);
                List.Update(n,Entities.CascadeOff);
            }
        }
    }
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Node){
            Node n = (Node) Entity;
            List.Delete(n.Uptime,Entities.CascadeOn);
        }
    }
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade) {}
}
