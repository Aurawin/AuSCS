package com.aurawin.scs.stored.cloud;

import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.entities.UniqueId;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = Database.Table.Cloud.Service)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.Cloud.Service.ById.name,
        Fields = ("Id")
)
@QueryByName(
        Name = Database.Query.Cloud.Service.ByName.name,
        Fields = {"Namepsace"}
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Cloud.Service.ById.name,
                        query = Database.Query.Cloud.Service.ById.value
                )
        }
)
public class Service extends Stored{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=Database.Field.Cloud.Service.Id)
    protected long Id;
    public long getId(){return Id;}


    @Column(name = Database.Field.Cloud.Service.ScaleStart)
    protected int ScaleStart;
    public int getScaleStart(){return ScaleStart;}
    public void setScaleStart(int scaleStart){ ScaleMax=scaleStart;}

    @Column(name = Database.Field.Cloud.Service.ScaleMax)
    protected int ScaleMax;
    public int getScaleMax(){return ScaleMax;}
    public void setScaleMax(int scaleMax){ ScaleMax=scaleMax;}

    @Column(name = Database.Field.Cloud.Service.ScaleMin)
    protected int ScaleMin;
    public int getScaleMin(){return ScaleMin;}
    public void setScaleMin(int scaleMin){ ScaleMax=scaleMin;}

    @Cascade({CascadeType.MERGE})
    @ManyToOne(fetch=FetchType.EAGER,targetEntity = Node.class)
    @JoinColumn(name = Database.Field.Cloud.Service.NodeId)
    protected Node Node;
    public void setNode(Node node){ Node=node;}
    public Node getNode(){return Node;}

    @Cascade({CascadeType.MERGE})
    @ManyToOne(fetch=FetchType.EAGER,targetEntity = UniqueId.class)
    @JoinColumn(name = Database.Field.Cloud.Service.UniqueId)
    protected UniqueId UniqueId;
    public void setUniqueId(UniqueId id){ UniqueId=id;}
    public UniqueId getUniqueId(){return UniqueId;}

    public Service() {
        Id=0;
        ScaleStart=0;
        ScaleMin=1;
        ScaleMax=10;
        Node=null;
        UniqueId=null;
    }

    public Service(long id) {
        Id = id;
        ScaleStart=0;
        ScaleMin=1;
        ScaleMax=10;
        Node=null;
        UniqueId=null;
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
    public static void entityCreated(Stored Entity, boolean Cascade) {}
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
