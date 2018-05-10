package com.aurawin.scs.stored.cloud;

import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.lang.Database;
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

import static javax.persistence.CascadeType.ALL;

@Entity
@Namespaced
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
@QueryByOwnerId(
        Name = Database.Query.Cloud.Service.ByOwnerId.name
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
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Service.ByName.name,
                        query = Database.Query.Cloud.Service.ByName.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Service.ByOwnerId.name,
                        query = Database.Query.Cloud.Service.ByOwnerId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Service.ByOwnerIdAndKind.name,
                        query = Database.Query.Cloud.Service.ByOwnerIdAndKind.value
                )
        }
)
public class Service extends Stored{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=Database.Field.Cloud.Service.Id)
    protected long Id;
    public long getId(){return Id;}

    @Column(name = Database.Field.Cloud.Service.Enabled)
    protected boolean Enabled;
    public boolean getEnabled(){return Enabled;}
    public void setEnabled(boolean enabled){ Enabled=enabled;}


    @Column(name = Database.Field.Cloud.Service.Port)
    protected int Port;
    public int getPort(){return Port;}
    public void setPort(int port){ Port=port;}

    @Column(name = Database.Field.Cloud.Service.Service)
    protected com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind Kind;
    public com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind getKind(){return Kind;}
    public void setKind(com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind kind){ Kind=kind;}

    public long getIP(){
        return (Owner!=null) ? Owner.IP : 0;
    }
    public String getHostname(){
        return ( (Owner!=null) && (Owner.Domain!=null) ) ? Owner.Domain.getName() : "";
    }

    @Column(name = Database.Field.Cloud.Service.ScaleStart)
    protected int ScaleStart;
    public int getScaleStart(){return ScaleStart;}
    public void setScaleStart(int scaleStart){ ScaleStart=scaleStart;}

    @Column(name = Database.Field.Cloud.Service.ScaleMax)
    protected int ScaleMax;
    public int getScaleMax(){return ScaleMax;}
    public void setScaleMax(int scaleMax){ ScaleMax=scaleMax;}

    @Column(name = Database.Field.Cloud.Service.ScaleMin)
    protected int ScaleMin;
    public int getScaleMin(){return ScaleMin;}
    public void setScaleMin(int scaleMin){ ScaleMax=scaleMin;}

    @Column(name = Database.Field.Cloud.Service.MountPoint)
    protected String MountPoint;
    public String getMountPoint(){return MountPoint;}
    public void setMountPoint(String mountPoint){ MountPoint=mountPoint;}



    @Cascade({CascadeType.MERGE})
    @ManyToOne(
            fetch=FetchType.EAGER,
            cascade= ALL,
            targetEntity = Node.class
    )
    @JoinColumn(
            nullable=true,
            name = Database.Field.Cloud.Service.OwnerId
    )
    @Fetch(value=FetchMode.JOIN)
    protected Node Owner;
    public void setOwner(Node node){ Owner=node;}
    public Node getOwner(){return Owner;}

    @Cascade({CascadeType.MERGE})
    @ManyToOne(fetch=FetchType.EAGER,targetEntity = UniqueId.class)
    @JoinColumn(name = Database.Field.Cloud.Service.UniqueId)
    protected UniqueId Namespace;
    public void setNamespace(UniqueId ns){ Namespace=ns;}
    public UniqueId getNamespace(){return Namespace;}


    public Service() {
        Id=0;
        ScaleStart=0;
        ScaleMin=1;
        ScaleMax=10;
        Owner=null;
        Namespace=null;
    }

    public Service(long id) {
        Id = id;
        ScaleStart=0;
        ScaleMin=1;
        ScaleMax=10;
        Owner=null;
        Namespace=null;
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
