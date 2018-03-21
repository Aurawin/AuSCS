package com.aurawin.scs.stored.domain.network;

import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.lang.Table;

import com.aurawin.scs.stored.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.scs.stored.annotations.QueryByDomainId;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


import com.aurawin.core.time.Time;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.annotations.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;


@javax.persistence.Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@javax.persistence.Table( name = Database.Table.Domain.Network.List)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Network.ByDomainId.name,
                        query = Database.Query.Domain.Network.ByDomainId.value
                )
        }
)
@FetchFields(
        {
                @FetchField(
                        Class = Folder.class,
                        Target = "Root"
                )
        }
)
@QueryByDomainId(
        Name = Database.Query.Domain.Network.ByDomainId.name
)

public class Network extends Stored {
    public static class Default {
        public static class Flag {
            public static int None = 0;
            public static int Trash = 1 << 0;
            public static int Documents = 1 << 1;
            public static int Mail = 1 << 2;
            public static int Music = 1 << 3;
            public static int Pictures = 1 << 4;
            public static int Videos = 1 << 5;
            public static int CustomFolders = 1 << 6;
            public static int Standard(){
                return Trash | Documents | Mail | Music | Pictures | Videos;
            }
        }
    }

    @javax.persistence.Id
    @Expose(serialize = true, deserialize = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Network.Id)
    protected long Id;
    public long getId() {
        return Id;
    }
    @Expose(serialize = true, deserialize = true)
    @ManyToOne(fetch=FetchType.EAGER, targetEntity = Account.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = Database.Field.Domain.Network.OwnerId)
    protected Account Owner;

    @Expose(serialize = true, deserialize = true)
    @OneToMany(mappedBy = "Owner", fetch = FetchType.EAGER, targetEntity = Member.class, cascade = CascadeType.MERGE)
    protected List<Member> Members = new ArrayList<Member>();

    @Expose(serialize = false, deserialize = false)
    @ManyToOne(fetch=FetchType.EAGER, targetEntity = Folder.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = Database.Field.Domain.Network.FolderId)
    protected Folder Root;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.AvatarId)
    private long AvatarId;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.DiskId)
    private long DiskId;
    public long getDiskId(){return DiskId;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.DomainId)
    private long DomainId;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.Exposition)
    private byte Exposition;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.Flags)
    private int Flags;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.Created)
    private Instant Created;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.Modified)
    private Instant Modified;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.Title)
    private String Title;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.Description)
    private String Description;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Network.CustomFolders, length = 1024*512)
    private String CustomFolders;

    public void Assign(Network Source){
        Id = Source.Id;
        Owner = Source.Owner;
        Members.clear();
        Members.addAll(Source.Members);
        AvatarId = Source.AvatarId;
        DomainId = Source.DomainId;
        Exposition = Source.Exposition;
        Flags = Source.Flags;
        Created = Source.Created;
        Modified = Source.Modified;
        Title = Source.Title;
        Description = Source.Description;
        CustomFolders = Source.CustomFolders;
        DiskId = Source.DiskId;
        // don't copy over folders
    }

    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Network n = (Network) ssn.getNamedQuery(Database.Query.Domain.Network.ByOwnerAndTitle.name)
                    .setParameter("DomainId",DomainId)
                    .setParameter("OwnerId", Owner.getId())
                    .setParameter("Title",Title)
                    .getSingleResult();
            if (n == null) {
                ssn.save(this);
            } else {
                Assign(n);
            }
        }
    }
    public Network() {
        Id=0;
        DomainId=0;
        AvatarId=0;
        DiskId= AuDisk.getNextAvailableDiskId();
        Exposition= Exposure.None;
        Created = Time.instantUTC();
        Modified = Created;
        Title = "";
        Description = "";
    }
    public Network(Account owner, byte exposition, String title, String description){
        Id=0;
        DomainId=owner.getDomainId();
        Owner = owner;
        DiskId= AuDisk.getNextAvailableDiskId();
        Exposition = exposition;
        Created = Time.instantUTC();
        Modified = Created;
        Title = title;
        Description = description;
    }
    public long getOwnerId(){
        return (Owner==null)? 0 : Owner.getId();
    }
    public long getDomainId() {
        return DomainId;
    }
    public long getAvatarId() {  return AvatarId; }
    public void setAvatarId(long id){ AvatarId=id; }
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Account) {
            Account ua = (Account) Entity;
            if (ua.Cabinet == null) {
                ua.Cabinet = new Network(
                        ua,
                        Exposure.Private,
                        Table.String(Table.Entities.Domain.User.Network.Default.Title),
                        Table.Format(Table.Entities.Domain.User.Network.Default.Description, ua.getName())
                );
                ua.Networks.add(ua.Cabinet);
                Entities.Save(ua.Cabinet,Cascade);
            }
        } else if (Entity instanceof Network){
            Network n = (Network) Entity;
            if (n.getDiskId()==0){
                n.DiskId = AuDisk.getNextAvailableDiskId();
            }
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade) { }
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain) {
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst = Entities.Lookup(
                    Network.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            Account root = Entities.Lookup(Account.class,d.getRootId());
            for (Network n : root.Networks) {
                Entities.Delete(n, CascadeOn);
            }

        }
    }


}
