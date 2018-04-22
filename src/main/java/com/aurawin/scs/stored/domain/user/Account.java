package com.aurawin.scs.stored.domain.user;

import com.aurawin.core.enryption.MD5;
import com.aurawin.core.solution.Namespace;
import com.aurawin.scs.lang.Table;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.stored.Entities;
import com.aurawin.core.stored.Stored;

import com.aurawin.scs.stored.annotations.QueryByDomainId;
import com.aurawin.scs.stored.annotations.QueryByDomainIdAndId;
import com.aurawin.scs.stored.annotations.QueryByDomainIdAndName;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.core.time.Time;
import com.aurawin.scs.stored.security.ACL;
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

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@javax.persistence.Table(name = com.aurawin.scs.lang.Database.Table.Domain.User.Account.Items)
@NamedQueries(
        {
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.User.Account.All.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Account.All.value

                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByName.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByName.value

                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByAuth.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByAuth.value
                ),
                @NamedQuery(
                        name = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainId.name,
                        query= com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainId.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ById.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ById.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainIdAndId.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainIdAndId.value
                ),
                @NamedQuery(
                        name  = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainIdAndName.name,
                        query = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainIdAndName.value
                )
        }
)
@QueryById(
        Name = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ById.name,
        Fields = {"Id"}
)
@QueryByDomainId(
        Name = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainId.name
)
@QueryByDomainIdAndId(
        Name = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainIdAndId.name
)
@QueryAll(
        Name = com.aurawin.scs.lang.Database.Query.Domain.User.Account.All.name
)
@QueryByDomainIdAndName(
        Name = com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByDomainIdAndName.name
)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
@FetchFields(
        {
                @FetchField(
                        Class = Account.class,
                        Target = "Networks"
                ),
                @FetchField(
                        Class = Account.class,
                        Target = "Contacts"
                ),
                @FetchField(
                        Class = Account.class,
                        Target = "Cabinet"
                ),
                @FetchField(
                        Class = Account.class,
                        Target = "Avatar"
                )
        }
)
public class Account extends Stored {
    @Expose(serialize = true, deserialize = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Id)
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

    @Expose(serialize = false, deserialize = false)
    @Cascade(CascadeType.ALL)
    @Fetch(value=FetchMode.SUBSELECT)
    @OneToMany(targetEntity = ACL.class, mappedBy = "Owner")
    public List<ACL>ACL = new ArrayList<ACL>();
    public boolean isGranted(long namespaceId){
        for (ACL acl:ACL){
            if (acl.NamespaceId==namespaceId)
                return true;
        }
        return false;
    }

    @Expose(serialize = false, deserialize = false)
    @Cascade(CascadeType.ALL)
    @Fetch(value=FetchMode.SUBSELECT)
    @OneToMany(targetEntity = ACL.class, mappedBy = "Owner")
    public List<ACL>Roles = new ArrayList<ACL>();
    public boolean isMember(long namespaceId){
        for (ACL acl:ACL){
            if (acl.NamespaceId==namespaceId)
                return true;
        }
        return false;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.DomainId)
    protected long DomainId;
    public long getDomainId() {
        return DomainId;
    }
    public void setDomainId(long domainId) {DomainId = domainId;    }


    @Cascade({CascadeType.ALL})
    @ManyToOne(targetEntity = Network.class)
    @Fetch(value=FetchMode.JOIN)
    @JoinColumn(nullable=true, name  = com.aurawin.scs.lang.Database.Field.Domain.User.Account.CabinetId)
    @Expose(serialize = false, deserialize = false)
    public Network Cabinet;



    @Cascade({CascadeType.ALL})
    @ManyToOne(targetEntity = Roster.class)
    @Fetch(value=FetchMode.JOIN)
    @JoinColumn(nullable=true, name  = com.aurawin.scs.lang.Database.Field.Domain.User.Account.RosterId)
    @Expose(serialize = false, deserialize = false)
    public Roster Me;


    @Cascade({CascadeType.ALL})
    @ManyToOne(targetEntity = com.aurawin.scs.stored.domain.user.Avatar.class)
    @JoinColumn(nullable=true, name  = com.aurawin.scs.lang.Database.Field.Domain.User.Account.AvatarId)
    @Fetch(value=FetchMode.JOIN)
    @Expose(serialize = false, deserialize = false)
    public Avatar Avatar;


    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Name)
    protected String Name;
    public String getName() { return Name; }
    public void setName(String user) { this.Name = user;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Pass)
    protected String Pass;
    public String getPass() {return Pass; }
    public void setPass(String pass) {
        Pass = pass;
        Auth = MD5.Encode(Name,":",Pass);
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Auth, length = 32)
    protected String Auth;
    public String getAuth() {
        return Auth;
    }
    public void setAuth(String auth) {
        Auth = auth;
    }


    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.FirstIP)
    protected long FirstIP;
    public long getFirstIP() {
        return FirstIP;
    }
    public void setFirstIP(long firstIP) {
        FirstIP = firstIP;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.LastIP)
    protected long LastIP;
    public long getLastIP() {
        return LastIP;
    }
    public void setLastIP(long lastIP) {
        LastIP = lastIP;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Created)
    protected Instant Created;
    public Instant getCreated() {        return Created;    }
    public void setCreated(Instant created) {        Created = created;    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Modified)
    public Instant Modified;
    public Instant getModified() {        return Modified;    }
    public void setModified(Instant modified) {        Modified = modified;    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.LastLogin)
    protected Instant LastLogin;
    public Instant getLastLogin() {
        return LastLogin;
    }
    public void setLastLogin(Instant lastLogin) {
        LastLogin = lastLogin;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.LastConsumptionCalc)
    protected Instant LastConsumptionCalculation;
    public Instant getLastConsumptionCalculation() {
        return LastConsumptionCalculation;
    }
    public void setLastConsumptionCalculation(Instant lastConsumptionCalculation) {
        LastConsumptionCalculation = lastConsumptionCalculation;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Consumption)
    protected long Consumption;
    public long getConsumption(){return Consumption;}
    public void setConsumption(long consumption){Consumption = consumption;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.Quota)
    protected long Quota;
    public long getQuota(){ return Quota;}
    public void setQuota(long quota){ Quota = quota;}

    @Expose(serialize = true, deserialize = true)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Account.AllowLogin)
    protected boolean AllowLogin;
    public boolean isAllowLogin() {        return AllowLogin;    }
    public void setAllowLogin(boolean allowLogin) {        AllowLogin = allowLogin;    }

    public Account(Domain domain, String user) {
        this.Id=0;
        this.DomainId=domain.getId();
        this.Name = user;
        this.Pass = "";
        this.Auth = "";
        this.Created = Time.instantUTC();
        this.Modified = this.Created;
    }
    public Account() {
        this.Id=0;
        this.DomainId=0;
        this.Name = "";
        this.Pass = "";
        this.Auth = "";
        this.Created = Time.instantUTC();
        this.Modified = this.Created;
    }

    @Override
    public boolean equals(Object o){
        return (
                (o instanceof Account) &&
                Id==((Account) o).getId() &&
                DomainId==((Account) o).DomainId

        );

    }
    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Account ua = null;
            Transaction tx = (ssn.isJoinedToTransaction()) ? ssn.getTransaction() : ssn.beginTransaction();
            try {
                ua = (Account) ssn.getNamedQuery(com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByName.name)
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
    public void Assign(Account src){
        Id=src.Id;
        DomainId=src.DomainId;
        Me=src.Me;
        Cabinet=src.Cabinet;
        Name=src.Name;
        Pass=src.Pass;
        Auth=src.Auth;
        FirstIP=src.FirstIP;
        LastIP=src.LastIP;
        LastLogin=src.LastLogin;
        Quota=src.Quota;
        Consumption=src.Consumption;
    }
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            Account ua = null;
            ACL acl = null;
            ua = new Account(d,Table.String(Table.Entities.Domain.Root));

            acl = new ACL(ua,Namespace.Entities.Identify(com.aurawin.scs.stored.security.role.User.class));
            Entities.Save(acl, CascadeOff);
            ua.Roles.add(acl);
            Entities.Save(ua,Cascade);
            d.Root=ua;


            Entities.Update(d, CascadeOff);


            ua = new Account(d,Table.String(Table.Entities.Domain.Default));
            acl = new ACL(ua,Namespace.Entities.Identify(com.aurawin.scs.stored.security.role.User.class));
            Entities.Save(acl, CascadeOff);
            ua.Roles.add(acl);
            ua.AllowLogin=false;
            Entities.Save(ua,Cascade);
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade){}
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst =Entities.Lookup(
                    Account.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored itm : lst) {
                Entities.Delete(itm, Entities.CascadeOn);
            }
        }
    }

}
