package com.aurawin.scs.stored.domain.network;


import com.aurawin.core.stored.annotations.*;
import com.aurawin.lang.Database;
import com.aurawin.core.lang.Table;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;

import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.UserAccount;
import com.aurawin.core.time.Time;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@javax.persistence.Table(name = Database.Table.Domain.Network.Folder)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ByPath.name,
                        query = Database.Query.Domain.Network.Folder.ByPath.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ByDomainId.name,
                        query = Database.Query.Domain.Network.Folder.ByDomainId.value
                ),
                @NamedQuery(
                        name = Database.Query.Domain.Network.Folder.ByNetworkId.name,
                        query = Database.Query.Domain.Network.Folder.ByNetworkId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.Folder.ById.name,
                        query = Database.Query.Domain.Network.Folder.ById.value
                )
        }
)
@QueryByDomainId(Name = Database.Query.Domain.Network.Folder.ByDomainId.name)
@QueryByNetworkId(Name = Database.Query.Domain.Network.Folder.ByNetworkId.name)
@QueryById(
        Name = Database.Query.Domain.Network.Folder.ById.name,
        Fields = {
                "Id",
                "DomainId"
        }
)
@QueryByName(
        Name = Database.Query.Domain.Network.Folder.ByPath.name,
        Fields = {
                "DomainId",
                "NetworkId",
                "Path"
        }
)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
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

    @Column(name = Database.Field.Domain.Network.Folders.NetworkId)
    private long NetworkId;

    @Column(name = Database.Field.Domain.Network.Folders.Exposition)
    private byte Exposition;

    @Column(name = Database.Field.Domain.Network.Folders.Created)
    private Instant Created;

    @Column(name = Database.Field.Domain.Network.Folders.Modified)
    private Instant Modified;

    @Column(name = Database.Field.Domain.Network.Folders.Path)
    private String Path;

    @Transient
    private boolean Validated;

    @Transient
    private String Name;

    @Transient
    private Folder Parent;

    @Transient
    private List<Folder> Children = new ArrayList<Folder>();

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
        Path=buildPath();
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
        Modified=Source.Modified;
        Created = Source.Created;
        Exposition = Source.Exposition;
        Path = Source.Path;
        Name = Source.Name;
    }
    public Folder addChild(String name){
        Folder c = new Folder(DomainId,OwnerId,NetworkId,name);
        c.Parent=this;
        c.DomainId =DomainId;
        c.OwnerId = OwnerId;
        c.NetworkId=NetworkId;
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
    public String getPath(){ return Path;}
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
        Path="";
    }
    public Folder(long domainId, long ownerId, long networkId, String name) {
        Parent = null;
        DomainId = domainId;
        OwnerId = ownerId;
        NetworkId = networkId;
        Name = name;
        Path = buildPath();
        Created=Time.instantUTC();
        Modified = Created;
    }
    public void Force(Folder child) {
        Folder Root = getRoot(), CLcv = null, FLcv = null;
        String[] Path = child.Path.split("/");
        FLcv=Root;
        for (int iLcv = 1; iLcv<Path.length; iLcv++) {
            CLcv = FLcv.getChildByName(Path[iLcv]);
            if (CLcv==null){
                CLcv=FLcv.addChild(Path[iLcv]);
            }
            FLcv=CLcv;
        }
        if (CLcv!=null) {
            CLcv.Assign(child);
            CLcv.Validated=true;
        }
    }
    @SuppressWarnings("unchecked")
    public void Refresh(Session ssn){
        List<Folder> lst = ssn.getNamedQuery(Database.Query.Domain.Network.Folder.ByLevel.name)
                .setParameter("DomainId",DomainId)
                .setParameter("NetworkId",NetworkId)
                .setParameter("Path",Path)
                .list();
        Folder Root = getRoot();
        for (Folder f : lst) {
            Root.Force(f);
        }

    }
    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Folder f = null;
            try {
                f = (Folder) ssn.getNamedQuery(Database.Query.Domain.Network.Folder.ByPath.name)
                        .setParameter("DomainId", DomainId)
                        .setParameter("NetworkId", NetworkId)
                        .setParameter("Path", Path)
                        .uniqueResult();
                if (f == null) {
                    ssn.save(this);
                } else {
                    Assign(f);
                }
                ssn.getTransaction().commit();
            } catch (Exception e){
                ssn.getTransaction().rollback();
                throw e;
            }
        }
    }
    public static void entityCreated(Entities List,Stored Entity){
        if (Entity instanceof UserAccount){
            // new user account is created
            UserAccount a = (UserAccount) Entity;
            Network cab = a.getCabinet();
        } else if (Entity instanceof Network){
            // the user is new.
            // Add Mailboxes
            // Add Trash bin
            // Add Documents
        }
    }

    public static void entityDeleted(Entities List,Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Folder) {
            Folder f = (Folder) Entity;
            for (Folder c : f.Children) {
                List.Delete(c, Entities.CascadeOn);
            }
        } else if (Entity instanceof Domain) {
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst = List.Lookup(
                    Folder.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored h : lst) {
                List.Delete(h, Entities.CascadeOn);
            }
        }
    }
    public static void entityUpdated(Entities List,Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Folder) {
            Folder f = (Folder) Entity;
            if (Cascade == true) {
                // note, just Update all children re-entrant will handle their children
                for (Folder c : f.Children) {
                    //todo Entities.Domain.Folder.
                }
            }
        }
    }
}
