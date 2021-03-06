package com.aurawin.scs.stored.domain.network;


import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.stored.Entities;


import com.aurawin.scs.stored.annotations.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.core.time.Time;

import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.core.stored.entities.Entities.UseCurrentTransaction;

@javax.persistence.Entity
@Namespaced
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@javax.persistence.Table(name = Database.Table.Domain.Network.Folder)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ByDomainId.name,
                        query = Database.Query.Domain.Network.Folder.ByDomainId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ByDomainIdAndName.name,
                        query = Database.Query.Domain.Network.Folder.ByDomainIdAndName.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ByDomainIdAndNetworkIdAndParentIdAndName.name,
                        query = Database.Query.Domain.Network.Folder.ByDomainIdAndNetworkIdAndParentIdAndName.value
                ),
                @NamedQuery(
                        name = Database.Query.Domain.Network.Folder.ByNetworkId.name,
                        query = Database.Query.Domain.Network.Folder.ByNetworkId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ById.name,
                        query = Database.Query.Domain.Network.Folder.ById.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ByParentId.name,
                        query = Database.Query.Domain.Network.Folder.ByParentId.value
                )
        }
)
@QueryByParentId(Name = Database.Query.Domain.Network.Folder.ByParentId.name)
@QueryByDomainId(Name = Database.Query.Domain.Network.Folder.ByDomainId.name)
@QueryByNetworkId(Name = Database.Query.Domain.Network.Folder.ByNetworkId.name)
@QueryById(
        Name = Database.Query.Domain.Network.Folder.ById.name,
        Fields = {
                "Id",
                "DomainId"
        }
)
@QueryByDomainIdAndParentIdAndName(
        Name = Database.Query.Domain.Network.Folder.ByDomainIdAndNetworkIdAndParentIdAndName.name
)
@QueryByDomainIdAndName(
        Name = Database.Query.Domain.Network.Folder.ByDomainIdAndName.name
)
@EntityDispatch(
        onPurge = true,
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
@FetchFields(
        {
                @FetchField(
                        Class = Folder.class,
                        Target = "Children"
                )
        }
)
public class Folder extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Network.Id)
    protected long Id;
    public long getId() {
        return Id;
    }
    @Column(name = Database.Field.Domain.Network.Folders.DomainId)
    private long DomainId;

    @Column(name = Database.Field.Domain.Network.Folders.OwnerId)
    private long OwnerId;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(
            targetEntity = Folder.class,
            fetch = FetchType.EAGER
    )
    @Fetch(value=FetchMode.JOIN)
    @JoinColumn(nullable=true, name  = Database.Field.Domain.Network.Folders.ParentId)
    @Expose(serialize = false, deserialize = false)
    public Folder Parent;
    public void setParent(Folder parent){
        if (Parent!=null)
            Parent.Children.remove(this);
        Parent = parent;
        if (parent!=null)
            parent.Children.add(this);
    }

    @Expose(serialize = false, deserialize = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Fetch(value=FetchMode.SUBSELECT)
    @OneToMany(
            fetch=FetchType.EAGER,
            targetEntity = Folder.class,
            mappedBy = "Parent",
            orphanRemoval = true,
            cascade={CascadeType.REMOVE,CascadeType.REFRESH}
    )
    public List<Folder>Children = new ArrayList<Folder>();

    @Column(name = Database.Field.Domain.Network.Folders.NetworkId)
    private long NetworkId;

    @Column(name = Database.Field.Domain.Network.Folders.DiskId)
    private long DiskId;

    @Column(name = Database.Field.Domain.Network.Folders.Exposition)
    private byte Exposition;

    @Column(name = Database.Field.Domain.Network.Folders.Created)
    private Instant Created;

    @Column(name = Database.Field.Domain.Network.Folders.Modified)
    private Instant Modified;

    @Column(name = Database.Field.Domain.Network.Folders.Name)
    private String Name;

    @Transient
    private boolean Validated;


    public Folder getRoot(){
        Folder r = this;
        while (r.Parent!=null)
            r=r.Parent;
        return r;
    }

    public void Invalidate(){
        Validated=false;
        for (Folder c : Children) {
            c.Invalidate();
        }
    }
    public String buildPath(){
        String sPath="";
        Folder f = this;
        while (f!=null) {
            sPath = f.Name + '/' + sPath;
            f = f.Parent;
        }
        sPath=sPath.substring(0, sPath.length() - 1);
        return sPath;
    }
    public void recalculatePath(){
        Modified=Time.instantUTC();
        for (Folder f : Children){
            f.recalculatePath();
        }
    }
    public void Assign(Folder Source){
        Id = Source.Id;
        DomainId=Source.DomainId;
        OwnerId=Source.OwnerId;
        NetworkId=Source.NetworkId;
        DiskId = Source.DiskId;
        Modified=Source.Modified;
        Created = Source.Created;
        Exposition = Source.Exposition;
        Name = Source.Name;
    }
    public Folder addChild(String name){
        Folder c = new Folder(DomainId,OwnerId,NetworkId,DiskId,name);
        c.Parent=this;
        c.Exposition=Exposition;
        Modified=c.Created;


        Children.add(c);
        return c;
    }
    public Folder getChildByName(String name){
        for (Folder f : Children){
            if (f.Name.compareTo(name)==0)
                return f;
        }
        return null;
    }
    public void Rename(String name) throws Exception{
        if (Parent==null){
            if (getChildByName(name)==null) {
                Name=name;
                recalculatePath();
            } else {
                throw new Exception(Table.Format(Table.Exception.Entities.Domain.Folder.UnableToCreateExists,name));
            }
        } else {
            if (Parent.getChildByName(name)==null){
                Name=name;
                recalculatePath();
            } else {
                throw new Exception(Table.Format(Table.Exception.Entities.Domain.Folder.UnableToCreateExists,name));
            }
        }
    }
    public Folder() {
        Parent=null;
        Id=0;
        DomainId=0;
        Created=Time.instantUTC();
        Modified=Created;
        Exposition = Exposure.None;
        Name="";
    }
    public Folder(long domainId, long ownerId, long networkId, long diskId, String name) {
        Parent = null;
        DomainId = domainId;
        OwnerId = ownerId;
        NetworkId = networkId;
        DiskId = diskId;
        Name = name;
        Created=Time.instantUTC();
        Modified = Created;
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
                Folder f = (Folder) ssn.getNamedQuery(Database.Query.Domain.Network.Folder.ByDomainIdAndNetworkIdAndParentIdAndName.name)
                        .setParameter("DomainId",DomainId)
                        .setParameter("NetworkId",NetworkId)
                        .setParameter("ParentId",Parent.Id)
                        .setParameter("Name",Name)
                        .uniqueResult();
                if (f == null) {
                    ssn.save(this);
                } else {
                    Assign(f);
                }
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                throw e;
            }
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Network){
            Network n = (Network) Entity;
            if (n.Root==null){
                n.Root = new Folder(n.getDomainId(), n.Owner.getId(), n.Id, n.getDiskId(),"");
                Entities.Save(n.Root,Cascade);
                AuDisk.makeFolder(
                        n.getDiskId(),
                        Namespace.Entities.Identify(File.class),
                        n.getDomainId(),
                        n.Id,
                        n.Root.Id
                );
                if (n.Owner.getName().equalsIgnoreCase(Table.String(Table.Entities.Domain.Root))){
                    // need the basic folders for www
                    Folder f = n.Root.getChildByName(Table.Stored.Path.Web);
                    if (f==null) {
                        f = n.Root.addChild(Table.Stored.Path.Web);
                        Entities.Save(f,Cascade);
                        AuDisk.makeFolder(
                                n.getDiskId(),
                                Namespace.Entities.Identify(File.class),
                                n.getDomainId(),
                                n.Id,
                                f.Id
                        );
                    }


                }
            }
            for (String sF:Table.Stored.Path.Default.Userland) {
                String[] slPath = sF.split("/");
                Folder fLcv = n.Root;
                Folder f = null;
                for (String sfC:slPath) {
                    f = fLcv.getChildByName(sfC);
                    if (f==null) {
                        f = fLcv.addChild(sfC);
                        Entities.Save(f, Cascade);
                        AuDisk.makeFolder(
                                n.getDiskId(),
                                Namespace.Entities.Identify(File.class),
                                n.getDomainId(),
                                n.Id,
                                f.Id
                        );
                    }
                    fLcv = f;
                }
            }
        }
    }
    public static void entityPurge(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Network) {
            // list all folders and delete
            Network n = (Network) Entity;
            Entities.Purge(n.Root, CascadeOn);

        } else if (Entity instanceof Folder){
            Folder f = (Folder) Entity;
            AuDisk.deleteFolder(
                    f.DiskId,
                    Namespace.Entities.Identify(File.class),
                    f.DomainId,
                    f.OwnerId,
                    f.Id
            );
            for (Folder c:f.Children){
                Entities.Purge(c,CascadeOn);
            }
        }
    }
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Folder) {

        } else if (Entity instanceof Domain) {
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst = Entities.Lookup(
                    Folder.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored h : lst) {
                // this is an entire list, don't cascade
                // children are included in this list.
                Entities.Delete(h, Entities.CascadeOff,UseCurrentTransaction);
            }
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Folder) {
            Folder f = (Folder) Entity;
            f.recalculatePath();
            for (Folder c : f.Children) {
                c.recalculatePath();
            }
        }
    }
}
