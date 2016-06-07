package com.aurawin.scs.stored.domain.network;

import com.aurawin.lang.Database;
import com.aurawin.core.lang.Table;

import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryByDomainId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.UserAccount;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


import com.aurawin.core.time.Time;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Network.Id)
    protected long Id;
    public long getId() {
        return Id;
    }


    @ManyToOne(fetch=FetchType.EAGER, targetEntity = UserAccount.class, cascade = CascadeType.ALL)
    @JoinColumn(name = Database.Field.Domain.Network.OwnerId)
    protected UserAccount Owner;

    @OneToMany(mappedBy = "Owner", fetch = FetchType.EAGER, targetEntity = Member.class, cascade = CascadeType.ALL)
    protected List<Member> Members = new ArrayList<Member>();

    @Column(name = Database.Field.Domain.Network.AvatarId)
    private long AvatarId;

    @Column(name = Database.Field.Domain.Network.DomainId)
    private long DomainId;

    @Column(name = Database.Field.Domain.Network.Exposition)
    private byte Exposition;

    @Column(name = Database.Field.Domain.Network.Flags)
    private int Flags;

    @Column(name = Database.Field.Domain.Network.Created)
    private Instant Created;

    @Column(name = Database.Field.Domain.Network.Modified)
    private Instant Modified;

    @Column(name = Database.Field.Domain.Network.Title)
    private String Title;

    @Column(name = Database.Field.Domain.Network.Description)
    private String Description;

    @Column(name = Database.Field.Domain.Network.CustomFolders, length = 1024*512)
    private String CustomFolders;

    @Transient
    public List<Folder> Folders = new ArrayList<Folder>();

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
        // don't copy over folders
    }

    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Network n = null;
            Transaction tx = ssn.beginTransaction();
            try {
                org.hibernate.Query q = Database.Query.Domain.Network.ByOwnerAndTitle.Create(ssn,DomainId,Owner.getId(),Title);
                n = (Network) q.uniqueResult();
                if (n == null) {
                    ssn.save(this);
                } else {
                    Assign(n);
                }
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                throw e;
            }
        }
    }
    public Network() {
        Id=0;
        DomainId=0;
        AvatarId=0;
        Exposition= Exposure.None;
        Created = Time.instantUTC();
        Modified = Created;
        Title = "";
        Description = "";
    }
    public Network(UserAccount owner, byte exposition, String title, String description){
        Id=0;
        DomainId=owner.getDomainId();
        Owner = owner;
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
    public static void entityCreated(Entities List,Stored Entity) throws Exception{
        if (Entity instanceof UserAccount) {
            UserAccount ua = (UserAccount) Entity;
            if (ua.getCabinet() == null) {
                Network cab = new Network(
                        ua,
                        Exposure.Private,
                        Table.String(Table.Entities.Domain.Network.Default.Title),
                        Table.Format(Table.Entities.Domain.Network.Default.Description, ua.getUser())
                );
                List.Save(cab);
            }
        } else if (Entity instanceof Network) {
            Network n = (Network) Entity;
            if (n.Owner.getCabinet() == null) {
                n.Owner.setCabinetId(n.getId());
                n.Owner.Networks.add(n);
                List.Update(n.Owner,Entities.CascadeOff);
            }
        }
    }
    public static void entityUpdated(Entities List,Stored Entity, boolean Cascade) { }
    public static void entityDeleted(Entities List,Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain) {
            Domain d = (Domain) Entity;
            Session ssn = List.Sessions.openSession();
            try {
                Transaction tx = ssn.beginTransaction();
                try {
                    ArrayList<Stored> lst = List.Lookup(
                            Network.class.getAnnotation(QueryByDomainId.class),
                            d.getId()
                    );
                    for (Stored h : lst) {
                        ssn.delete(h);
                    }
                } finally {
                    tx.commit();
                }
            } finally {
                ssn.close();
            }
        }
    }


}
