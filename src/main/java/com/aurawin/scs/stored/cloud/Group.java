package com.aurawin.scs.stored.cloud;

import com.aurawin.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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
                )
        }
)
public class Group extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Cloud.Group.Id)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }


    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL, targetEntity=Location.class)
    @JoinColumn(name = Database.Field.Cloud.Group.LocationId)
    protected Location Location;
    public Location getLocation() { return Location; }
    public void setLocation(Location location){
        Location=location;
        if (Location.Groups.indexOf(this)==-1)
            Location.Groups.add(this);
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
    @OneToMany(
            targetEntity = Resource.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "Group"
    )
    protected List<Resource> Resources = new ArrayList<Resource>();

    @Column(name = Database.Field.Cloud.Group.Name)
    protected String Name;
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    @Column(name = Database.Field.Cloud.Group.Rack)
    protected String Rack;
    public String getRack() {
        return Rack;
    }
    public void setRack(String rack) {
        Rack = rack;
    }


    @Column(name = Database.Field.Cloud.Group.Row)
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

    public static void entityCreated(Entities List, Stored Entity){ }
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade) {}
}
