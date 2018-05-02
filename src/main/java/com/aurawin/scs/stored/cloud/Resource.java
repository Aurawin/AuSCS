package com.aurawin.scs.stored.cloud;

import com.aurawin.core.stored.annotations.*;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Namespaced
@DynamicInsert(value =true)
@DynamicUpdate(value= true)
@SelectBeforeUpdate(value =true)
@Table(name = com.aurawin.scs.lang.Database.Table.Cloud.Resource)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@QueryById(
        Name = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ById.name,
        Fields = { "Id" }
)
@QueryByOwnerId(
        Name = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ByOwnerId.name
)
@QueryByName(
        Name = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ByName.name,
        Fields = {"Name"}
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ByOwnerId.name,
                        query = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ByOwnerId.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ByName.name,
                        query = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ByName.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ById.name,
                        query = com.aurawin.scs.lang.Database.Query.Cloud.Resource.ById.value
                )
        }
)
public class Resource extends Stored{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = com.aurawin.scs.lang.Database.Field.Cloud.Resource.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Column(name = com.aurawin.scs.lang.Database.Field.Cloud.Resource.Name)
    @Expose(serialize = true, deserialize = true)
    protected String Name;
    public String getName() {    return Name;  }
    public void setName(String name) {      Name = name;   }

    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE,targetEntity=Group.class)
    @JoinColumn(name = com.aurawin.scs.lang.Database.Field.Cloud.Resource.OwnerId)
    @Expose(serialize = true, deserialize = true)
    protected Group Group;
    public Group getGroup() { return Group; }
    public void setGroup(Group group) {
        Group = group;
        if (!group.Resources.contains(this)==true)
            group.Resources.add(this);
    }

    @OneToMany(
            mappedBy = "Resource",
            targetEntity=Node.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Fetch(value=FetchMode.SUBSELECT)
    @Expose(serialize = false, deserialize = false)
    public List<Node> Nodes = new ArrayList<Node>();

    public Resource(long id) {
        Id = id;
        Name = "";
        Group=null;
    }

    public Resource() {
        Id =0;
        Name = "";
        Group=null;
    }

    public Resource(long id, String name) {
        Id = id;
        Name = name;
        Group=null;
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
    public static void entityCreated(Stored Entity, boolean Cascade) {}
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
