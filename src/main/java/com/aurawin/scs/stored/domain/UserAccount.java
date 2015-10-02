package com.aurawin.scs.stored.domain;

import com.aurawin.core.lang.*;
import com.aurawin.core.lang.Table;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;

import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.core.time.Time;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import com.google.gson.Gson;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@javax.persistence.Table(name = Database.Table.Domain.UserAccount.Items)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.UserAccount.ByName.name,
                        query = Database.Query.Domain.UserAccount.ByName.value

                ),
                @NamedQuery(
                        name  = Database.Query.Domain.UserAccount.ByAuth.name,
                        query = Database.Query.Domain.UserAccount.ByAuth.value
                ),
                @NamedQuery(
                        name = Database.Query.Domain.UserAccount.ByDomainId.name,
                        query= Database.Query.Domain.UserAccount.ByDomainId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.UserAccount.ById.name,
                        query = Database.Query.Domain.UserAccount.ById.value
                )
        }
)
@QueryById(
        Name = Database.Query.Domain.UserAccount.ById.name,
        Fields = {
                "Id",
                "DomainId"
        }
)
@QueryByDomainId(
        Name = Database.Query.Domain.UserAccount.ByDomainId.name
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
                )
        }



)
public class UserAccount extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.UserAccount.Id)
    protected long Id;
    public long getId() {
        return Id;
    }

    @OneToMany(mappedBy = "Owner",fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    public List<Network> Networks= new ArrayList<Network>();

    @OneToMany(mappedBy = "Owner",fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    public List<Roster>Contacts = new ArrayList<Roster>();

    @Column(name = Database.Field.Domain.UserAccount.DomainId)
    protected long DomainId;
    public long getDomainId() {   return DomainId; }

    @Column(name = Database.Field.Domain.UserAccount.CabinetId)
    protected long CabinetId;
    protected long getCabinetId(long id){
        return CabinetId;
    }
    public void setCabinetId(long id){
        CabinetId=id;
    }

    @Column(name = Database.Field.Domain.UserAccount.RosterId)
    protected long RosterId;
    public long getRosterId(){return RosterId;}
    public void setRosterId(long id){RosterId = id; }

    @Column(name = Database.Field.Domain.UserAccount.AvatarId)
    protected long AvatarId;
    public long getAvatarId() { return AvatarId; }
    public void setAvatarId(long id){ AvatarId= id;}

    @Column(name = Database.Field.Domain.UserAccount.User)
    protected String User;
    public String getUser() { return User; }
    public void setUser(String user) { this.User = user;}

    @Column(name = Database.Field.Domain.UserAccount.Pass)
    protected String Pass;
    public String getPass() {return Pass; }
    public void setPass(String pass) {
        Pass = pass;
    }

    @Column(name = Database.Field.Domain.UserAccount.Auth, length = 16)
    protected String Auth;
    public String getAuth() {
        return Auth;
    }
    public void setAuth(String auth) {
        Auth = auth;
    }


    @Column(name = Database.Field.Domain.UserAccount.FirstIP)
    protected long FirstIP;
    public long getFirstIP() {
        return FirstIP;
    }
    public void setFirstIP(long firstIP) {
        FirstIP = firstIP;
    }


    @Column(name = Database.Field.Domain.UserAccount.LastIP)
    protected long LastIP;
    public long getLastIP() {
        return LastIP;
    }
    public void setLastIP(long lastIP) {
        LastIP = lastIP;
    }

    @Column(name = Database.Field.Domain.UserAccount.LockCount)
    protected int Lockcount;
    public int getLockcount() {
        return Lockcount;
    }
    public void setLockcount(int lockcount) {
        Lockcount = lockcount;
    }

    @Column(name = Database.Field.Domain.UserAccount.Created)
    protected long Created;
    public long getCreated() {        return Created;    }
    public void setCreated(long created) {        Created = created;    }

    @Column(name = Database.Field.Domain.UserAccount.Modified)
    protected long Modified;
    public long getModified() {        return Modified;    }
    public void setModified(long modified) {        Modified = modified;    }

    @Column(name = Database.Field.Domain.UserAccount.LastLogin)
    protected long LastLogin;
    public long getLastLogin() {
        return LastLogin;
    }
    public void setLastLogin(long lastLogin) {
        LastLogin = lastLogin;
    }

    @Column(name = Database.Field.Domain.UserAccount.LastConsumptionCalc)
    protected long LastConsumptionCalculation;
    public long getLastConsumptionCalculation() {
        return LastConsumptionCalculation;
    }
    public void setLastConsumptionCalculation(long lastConsumptionCalculation) {
        LastConsumptionCalculation = lastConsumptionCalculation;
    }

    @Column(name = Database.Field.Domain.UserAccount.Consumption)
    protected long Consumption;
    public long getConsumption(){return Consumption;}
    public void setConsumption(long consumption){Consumption = consumption;}

    @Column(name = Database.Field.Domain.UserAccount.Quota)
    protected long Quota;
    public long getQuota(){ return Quota;}
    public void setQuota(long quota){ Quota = quota;}

    @Column(name = Database.Field.Domain.UserAccount.AllowLogin)
    protected boolean AllowLogin;
    public boolean isAllowLogin() {        return AllowLogin;    }
    public void setAllowLogin(boolean allowLogin) {        AllowLogin = allowLogin;    }

    public UserAccount(long domainId, String user) {
        this.DomainId=domainId;
        this.User = user;
        this.Created = Time.dtUTC();
        this.Modified = this.Created;
    }
    public UserAccount() {
        this.Id=0;
        this.DomainId=0;
        this.AvatarId=0;
        this.CabinetId=0;
        this.User = "";
        this.Pass = "";
        this.Auth = "";
        this.Created = Time.dtUTC();
        this.Modified = this.Created;
    }

    public static UserAccount fromJSON(Gson Parser, String Data){
        return (UserAccount) Parser.fromJson(Data,UserAccount.class);
    }
    @Override
    public boolean equals(Object o){
        return (
                (o instanceof UserAccount) &&
                Id==((UserAccount) o).getId() &&
                DomainId==((UserAccount) o).DomainId &&
                CabinetId==((UserAccount) o).CabinetId &&
                RosterId==((UserAccount) o).RosterId &&
                User.compareTo(((UserAccount) o).User)==0 &&
                Pass.compareTo(((UserAccount) o).Pass)==0 &&
                Auth.compareTo(((UserAccount) o).Auth)==0 &&
                FirstIP==((UserAccount) o).FirstIP &&
                LastIP==((UserAccount) o).LastIP &&
                Lockcount==((UserAccount) o).Lockcount &&
                LastLogin==((UserAccount) o).LastLogin &&
                Quota == ((UserAccount) o).Quota&&
                Consumption == ((UserAccount) o).Consumption
        );

    }
    public void Assign(UserAccount src){
        Id=src.Id;
        DomainId=src.DomainId;
        RosterId=src.RosterId;
        CabinetId=src.CabinetId;
        User=src.User;
        Pass=src.Pass;
        Auth=src.Auth;
        FirstIP=src.FirstIP;
        LastIP=src.LastIP;
        Lockcount=src.Lockcount;
        LastLogin=src.LastLogin;
        Quota=src.Quota;
        Consumption=src.Consumption;
    }
    public Roster getMe(){
        if (Contacts.isEmpty()==true) return null;
        return Contacts.stream().filter( (r) -> r.getId()==RosterId).findFirst().get();
    }

    public Network getCabinet(){
        if (Networks.isEmpty()==true) return null;
        return Networks.stream().filter((n) -> n.getId()==CabinetId).findFirst().get();
    }

    public static void entityCreated(Entities List,Stored Entity) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            UserAccount ua = new UserAccount(d.getId(),Table.String(Table.Entities.Domain.Root));
            Entities.Create(List,ua);
            d.setRootId(ua.getId());
            Entities.Update(List,d, Entities.CascadeOff);
        }
    }
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade){}
    public static void entityDeleted(Entities List,Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            Session ssn = List.Sessions.openSession();
            try {
                Transaction tx = ssn.beginTransaction();
                try {
                    ArrayList<Stored> lst = Entities.Lookup(
                            UserAccount.class.getAnnotation(QueryByDomainId.class),
                            List,
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
