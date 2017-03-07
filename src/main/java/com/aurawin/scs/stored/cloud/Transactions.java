package com.aurawin.scs.stored.cloud;

import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value =true)
@SelectBeforeUpdate(value =true)
@Table(name = Database.Table.Cloud.Transactions)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.Cloud.Transactions.ById.name,
        Fields = ("Id")
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Cloud.Transactions.ById.name,
                        query = Database.Query.Cloud.Transactions.ById.value
                )
        }
)
public class Transactions extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Cloud.Transactions.Id)
    protected long Id;
    public long getId(){return Id;}

    @Column(name = Database.Field.Cloud.Transactions.Filtered)
    protected long Filtered;
    public long getFiltered() {   return Filtered;  }
    public void setFiltered(long filtered) {        Filtered = filtered;    }

    @Column(name = Database.Field.Cloud.Transactions.Received)
    protected long Received;
    public long getReceived() {       return Received;   }
    public void setReceived(long received) {        Received = received;    }

    @Column(name=Database.Field.Cloud.Transactions.Sent)
    protected long Sent;
    public long getSent() {       return Sent;   }
    public void setSent(long sent) {        Sent = sent;    }

    @Column(name=Database.Field.Cloud.Transactions.Streams)
    protected long Streams;
    public long getStreams() {        return Streams;    }
    public void setStreams(long streams) {        Streams = streams;    }

    @ManyToOne(targetEntity = Node.class, cascade = CascadeType.MERGE,fetch=FetchType.EAGER)
    @JoinColumn(name = Database.Field.Cloud.Transactions.NodeId)
    protected Node Node;
    public Node getNode() { return Node; }
    public void setNode(Node node) { Node = node; }
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
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Node) {
            Node n = (Node) Entity;
            if (n.Transactions==null) {
                n.Transactions= new Transactions();
                n.Transactions.Node=n;
                Entities.Save(n.Transactions,Cascade);
                Entities.Update(n,Cascade);
            }
        }
    }
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
