package com.aurawin.scs.stored.cloud;

import com.aurawin.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
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
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Cloud.Node.ByName.name,
                        query = Database.Query.Cloud.Node.ByName.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Node.ById.name,
                        query = Database.Query.Cloud.Node.ById.value
                )
        }
)
public class Node extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Cloud.Node.Id)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.Cloud.Node.Name)
    protected String Name;
    public String getName() {  return Name; }
    public void setName(String name) {     Name = name; }

    @Column(name = Database.Field.Cloud.Node.IP, length = 45)
    protected String IP;
    public String getIP(){ return IP;}
    public void setIP(String ip){IP=ip;}

    @Cascade({CascadeType.MERGE})
    @ManyToOne(targetEntity = Resource.class, fetch = FetchType.EAGER )
    @JoinColumn(name  = Database.Field.Cloud.Node.ResourceId, nullable = false)
    protected Resource Resource;
    public Resource getResource(){return Resource;}
    public void setResource(Resource resource){
        Resource=resource;
    }

    @Cascade({CascadeType.MERGE})
    @ManyToOne(targetEntity = Transactions.class, fetch = FetchType.EAGER)
    @JoinColumn(name  = Database.Field.Cloud.Node.TransactionsId)
    protected Transactions Transactions;
    public Transactions getTransactions(){return Transactions;}
    public void setTransactions(Transactions transactions){ Transactions = transactions;}


    @Cascade({CascadeType.MERGE})
    @ManyToOne(targetEntity = Uptime.class, fetch = FetchType.EAGER)
    @JoinColumn(name  = Database.Field.Cloud.Node.UptimeId)
    protected Uptime Uptime;
    public Uptime getUptime(){return Uptime;}
    public void setUptime(Uptime uptime){ Uptime = uptime;}

    @Cascade({CascadeType.MERGE})
    @OneToMany(targetEntity = Service.class, fetch=FetchType.EAGER, mappedBy="Node")
    protected List<Service> Services = new ArrayList<Service>();

    public Node() {
        Id = 0;
        Name = "";
        IP="";
        Transactions=null;
        Resource =null;
    }

    public Node(long id) {
        Id = id;
        Name="";
        IP="";
        Transactions=null;
        Resource=null;
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
    public static void entityCreated(Entities List, Stored Entity){ }
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade) {}
}
