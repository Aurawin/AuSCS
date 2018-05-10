package com.aurawin.scs.stored.cloud;

import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.domain.Domain;
import com.google.gson.annotations.Expose;

import com.aurawin.scs.stored.Entities;
import com.aurawin.core.stored.Stored;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;


import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.core.stored.entities.Entities.UseCurrentTransaction;

@Entity
@Namespaced
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = Database.Table.Cloud.Node)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
@QueryByName(
        Name = Database.Query.Cloud.Node.ByName.name,
        Fields = {"Name"}
)
@QueryById(
        Name = Database.Query.Cloud.Node.ById.name,
        Fields = ("Id")
)
@QueryByOwnerId(
        Name = com.aurawin.scs.lang.Database.Query.Cloud.Node.ByOwnerId.name
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Cloud.Node.ByName.name,
                        query = Database.Query.Cloud.Node.ByName.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Node.ById.name,
                        query = Database.Query.Cloud.Node.ById.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Node.ByOwnerId.name,
                        query = Database.Query.Cloud.Node.ByOwnerId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Node.ByIP.name,
                        query = Database.Query.Cloud.Node.ByIP.value
                )
        }
)

public class Node extends Stored implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Cloud.Node.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.Cloud.Node.Name)
    @Expose(serialize = true, deserialize = true)
    protected String Name;
    public String getName() {  return Name; }
    public void setName(String name) {     Name = name; }

    @Column(name = Database.Field.Cloud.Node.IP, length = 45)
    @Expose(serialize = true, deserialize = true)
    protected long IP;
    public long getIP(){ return IP;}
    public void setIP(long ip){IP=ip;}

    @Expose(serialize = false, deserialize = false)
    @ManyToOne(
            targetEntity = Domain.class,
            fetch = FetchType.EAGER
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(
            name  = Database.Field.Cloud.Node.DomainId,
            nullable = true
    )
    protected Domain Domain;
    public Domain getDomain(){return Domain;}
    public void setDomain(Domain domain){
        Domain=domain;
    }

    @Expose(serialize = false, deserialize = false)
    @ManyToOne(
            targetEntity = Group.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.REFRESH
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name  = Database.Field.Cloud.Node.GroupId, nullable = false)
    protected Group Group;
    public Group getGroup(){return Group;}

    @Expose(serialize = false, deserialize = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(
            targetEntity = Resource.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.REFRESH
    )
    @JoinColumn(name  = Database.Field.Cloud.Node.OwnerId, nullable = false)
    protected Resource Resource;
    public Resource getResource(){return Resource;}
    public void setResource(Resource resource){
        Group = (resource!=null) ? resource.Group : null;
        Resource=resource;
    }

    @Expose(serialize = true, deserialize = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(
            targetEntity = Transactions.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name  = Database.Field.Cloud.Node.TransactionsId
    )
    protected Transactions Transactions;
    public Transactions getTransactions(){return Transactions;}
    public void setTransactions(Transactions transactions){ Transactions = transactions;}


    @Expose(serialize = true, deserialize = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(
            nullable = true,
            name  = Database.Field.Cloud.Node.UptimeId
    )
    @ManyToOne(

            targetEntity = Uptime.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE

    )
    protected Uptime Uptime;
    public Uptime getUptime(){return Uptime;}
    public void setUptime(Uptime uptime){ Uptime = uptime;}


    @Expose(serialize = true, deserialize = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            targetEntity = Service.class,
            fetch=FetchType.EAGER,
            mappedBy="Owner",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )

    protected List<Service> Services = new ArrayList<Service>();



    public Node() {
        Reset();
    }
    public void Reset(){
        Id = 0;
        Name = "";
        IP = 0;
        Transactions = null;
        Resource = null;
        Group = null;
    }
    public Node(long id) {
        Reset();
        Id = id;
    }

    @Override
    public String toString(){
        return Name;
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
    public static void entityDeleted(Stored Entity, boolean Cascade) {
        if (Entity instanceof Resource){
            Resource r = (Resource) Entity;
            ArrayList<Node> nodes = Entities.Cloud.Node.listAll(r);
            for (Node n :nodes){
                Entities.Delete(n, CascadeOn,UseCurrentTransaction);
            }

        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
