package com.aurawin.scs.stored.cloud;

import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.lang.Database;
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
@DynamicInsert(value = true)
@DynamicUpdate(value =true)
@SelectBeforeUpdate(value =true)
@Table(name = Database.Table.Cloud.Group)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
@QueryById(
        Name = Database.Query.Cloud.Group.ById.name,
        Fields = { "Id" }
)
@QueryByName(
        Name = Database.Query.Cloud.Group.ByName.name,
        Fields = {"Name"}
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Cloud.Group.ByName.name,
                        query = Database.Query.Cloud.Group.ByName.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Group.ById.name,
                        query = Database.Query.Cloud.Group.ById.value
                ),
                @NamedQuery(
                        name = Database.Query.Cloud.Group.All.name,
                        query = Database.Query.Cloud.Group.All.value
                )
        }
)
@QueryAll(
        Name = Database.Query.Cloud.Group.All.name
)
public class Group extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Cloud.Group.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @OneToMany(
            mappedBy = "Group",
            targetEntity=Resource.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Fetch(value=FetchMode.SUBSELECT)
    @Expose(serialize = false, deserialize = false)
    public List<Resource> Resources = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL, targetEntity=Location.class)
    @JoinColumn(name = Database.Field.Cloud.Group.LocationId)
    @Expose(serialize = true, deserialize = true)
    protected Location Location;
    public Location getLocation() { return Location; }
    public void setLocation(Location location){
        Location=location;
        if (Location.Groups.indexOf(this)==-1)
            Location.Groups.add(this);
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


    @Column(name = Database.Field.Cloud.Group.Name)
    @Expose(serialize = true, deserialize = true)
    protected String Name;
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }


    @Column(name = Database.Field.Cloud.Group.Description)
    @Expose(serialize = true, deserialize = true)
    protected String Description;
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    @Column(name = Database.Field.Cloud.Group.Rack)
    @Expose(serialize = true, deserialize = true)
    protected String Rack;
    public String getRack() {
        return Rack;
    }
    public void setRack(String rack) {
        Rack = rack;
    }


    @Column(name = Database.Field.Cloud.Group.Row)
    @Expose(serialize = true, deserialize = true)
    protected String Row;
    public String getRow() { return Row; }
    public void setRow(String row) {
        Row = row;
    }

    public Group(long id) {
        Id = id;
        Name = "";
        Rack="";
        Row ="";
        Location=null;
    }
    public Group() {
        Id =0;
        Name = "";
        Rack="";
        Row = "";
        Location=null;
    }

    public static void entityCreated(Stored Entity, boolean Cascade){ }
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
