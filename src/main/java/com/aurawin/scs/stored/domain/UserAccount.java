package com.aurawin.scs.stored.domain;

import com.aurawin.core.lang.Table;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;

import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.core.time.Time;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@javax.persistence.Table(name = com.aurawin.scs.lang.Database.Table.Domain.UserAccount.Items)
@NamedQueries(
        {
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByName.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByName.value

                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByAuth.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByAuth.value
                ),
                @NamedQuery(
                        name = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainId.name,
                        query= com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainId.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ById.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ById.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainIdAndId.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainIdAndId.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainIdAndName.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainIdAndName.value
                )
        }
)
@QueryById(
        Name = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ById.name,
        Fields = {"Id"}
)
@QueryByDomainId(
        Name = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainId.name
)
@QueryByDomainIdAndId(
        Name = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainIdAndId.name
)
@QueryByDomainIdAndName(
        Name = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByDomainIdAndName.name
)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
@FetchFields(
        {
                @FetchField(
                        Class = UserAccount.class,
                        Target = "Networks"
                ),
                @FetchField(
                        Class = UserAccount.class,
                        Target = "Contacts"
                ),
                @FetchField(
                        Class = UserAccount.class,
                        Target = "Cabinet"
                ),
                @FetchField(
                        Class = UserAccount.class,
                        Target = "Avatar"
                )
        }
)
public class UserAccount extends Stored {
    @Expose(serialize = true, deserialize = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Id)
    protected long Id;
    public long getId() {
        return Id;
    }
    public void setId(long id){ Id=id;}

    @Expose(serialize = false, deserialize = false)
    @Cascade(CascadeType.MERGE)
    @Fetch(value=FetchMode.SUBSELECT)
    @OneToMany(
            targetEntity = Network.class,
            mappedBy = "Owner"
    )
    public List<Network> Networks= new ArrayList<Network>();

    @Expose(serialize = false, deserialize = false)
    @Cascade(CascadeType.ALL)
    @Fetch(value=FetchMode.SUBSELECT)
    @OneToMany(targetEntity = Roster.class, mappedBy = "Owner")
    public List<Roster>Contacts = new ArrayList<Roster>();

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.DomainId)
    protected long DomainId;
    public long getDomainId() {
        return DomainId;
    }
    public void setDomainId(long domainId) {DomainId = domainId;    }


    @Cascade({CascadeType.ALL})
    @ManyToOne(targetEntity = Network.class)
    @Fetch(value=FetchMode.JOIN)
    @JoinColumn(nullable=true, name  = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.CabinetId)
    @Expose(serialize = false, deserialize = false)
    public Network Cabinet;
    public void setCabinet(Network cabinet){Cabinet = cabinet; }


    @Cascade({CascadeType.ALL})
    @ManyToOne(targetEntity = Roster.class)
    @Fetch(value=FetchMode.JOIN)
    @JoinColumn(nullable=true, name  = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.RosterId)
    @Expose(serialize = false, deserialize = false)
    protected Roster Me;
    public Roster getMe(){return Me;}
    public void setMe(Roster roster){Me = roster; }

    @Cascade({CascadeType.ALL})
    @ManyToOne(targetEntity = Avatar.class)
    @JoinColumn(nullable=true, name  = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.AvatarId)
    @Fetch(value=FetchMode.JOIN)
    @Expose(serialize = false, deserialize = false)
    protected Avatar Avatar;
    public Avatar getAvatar(){return Avatar;}
    public void setAvatar(Avatar avatar){ Avatar = avatar;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Name)
    protected String Name;
    public String getName() { return Name; }
    public void setName(String user) { this.Name = user;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Pass)
    protected String Pass;
    public String getPass() {return Pass; }
    public void setPass(String pass) {
        Pass = pass;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Auth, length = 16)
    protected String Auth;
    public String getAuth() {
        return Auth;
    }
    public void setAuth(String auth) {
        Auth = auth;
    }


    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.FirstIP)
    protected long FirstIP;
    public long getFirstIP() {
        return FirstIP;
    }
    public void setFirstIP(long firstIP) {
        FirstIP = firstIP;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.LastIP)
    protected long LastIP;
    public long getLastIP() {
        return LastIP;
    }
    public void setLastIP(long lastIP) {
        LastIP = lastIP;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.LockCount)
    protected int Lockcount;
    public int getLockcount() {
        return Lockcount;
    }
    public void setLockcount(int lockcount) {
        Lockcount = lockcount;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Created)
    protected Instant Created;
    public Instant getCreated() {        return Created;    }
    public void setCreated(Instant created) {        Created = created;    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Modified)
    public Instant Modified;
    public Instant getModified() {        return Modified;    }
    public void setModified(Instant modified) {        Modified = modified;    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.LastLogin)
    protected Instant LastLogin;
    public Instant getLastLogin() {
        return LastLogin;
    }
    public void setLastLogin(Instant lastLogin) {
        LastLogin = lastLogin;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.LastConsumptionCalc)
    protected Instant LastConsumptionCalculation;
    public Instant getLastConsumptionCalculation() {
        return LastConsumptionCalculation;
    }
    public void setLastConsumptionCalculation(Instant lastConsumptionCalculation) {
        LastConsumptionCalculation = lastConsumptionCalculation;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Consumption)
    protected long Consumption;
    public long getConsumption(){return Consumption;}
    public void setConsumption(long consumption){Consumption = consumption;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.Quota)
    protected long Quota;
    public long getQuota(){ return Quota;}
    public void setQuota(long quota){ Quota = quota;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.UserAccount.AllowLogin)
    protected boolean AllowLogin;
    public boolean isAllowLogin() {        return AllowLogin;    }
    public void setAllowLogin(boolean allowLogin) {        AllowLogin = allowLogin;    }

    public UserAccount(Domain domain, String user) {
        this.Id=0;
        this.DomainId=domain.Id;
        this.Name = user;
        this.Pass = "";
        this.Auth = "";
        this.Lockcount=0;
        this.Created = Time.instantUTC();
        this.Modified = this.Created;
    }
    public UserAccount() {
        this.Id=0;
        this.DomainId=0;
        this.Name = "";
        this.Pass = "";
        this.Auth = "";
        this.Lockcount=0;
        this.Created = Time.instantUTC();
        this.Modified = this.Created;
    }

    @Override
    public boolean equals(Object o){
        return (
                (o instanceof UserAccount) &&
                Id==((UserAccount) o).getId() &&
                DomainId==((UserAccount) o).DomainId

        );

    }
    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            UserAccount ua = null;
            Transaction tx = (ssn.isJoinedToTransaction()) ? ssn.getTransaction() : ssn.beginTransaction();
            try {
                ua = (UserAccount) ssn.getNamedQuery(com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByName.name)
                        .setParameter("DomainId",DomainId)
                        .setParameter("Name",Name)
                        .uniqueResult();
                if (ua == null) {
                    ssn.save(this);
                } else {
                    Assign(ua);
                }
                tx.commit();
            } catch (Exception e){
                tx.rollback();
            }
        }
    }
    public void Assign(UserAccount src){
        Id=src.Id;
        DomainId=src.DomainId;
        Me=src.Me;
        Cabinet=src.Cabinet;
        Name=src.Name;
        Pass=src.Pass;
        Auth=src.Auth;
        FirstIP=src.FirstIP;
        LastIP=src.LastIP;
        Lockcount=src.Lockcount;
        LastLogin=src.LastLogin;
        Quota=src.Quota;
        Consumption=src.Consumption;
    }
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            UserAccount ua = new UserAccount(d,Table.String(Table.Entities.Domain.Root));
            Entities.Save(ua,Cascade);
            d.setRootId(ua.Id);
            Entities.Update(ua,Entities.CascadeOff);
            Entities.Update(d,Entities.CascadeOff);
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade){}
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst =Entities.Lookup(
                    UserAccount.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored itm : lst) {
                Entities.Delete(itm, Entities.CascadeOn);
            }
        }
    }

}
