package com.aurawin.scs.stored.domain;


import com.aurawin.core.lang.Table;
import com.aurawin.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.CascadeType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate( value = true)
@javax.persistence.Table(name = Database.Table.Domain.UserAccount.Roster.Items)
@NamedQueries(
        {
                @NamedQuery(
                        name = Database.Query.Domain.UserAccount.Roster.ByDomainId.name,
                        query = Database.Query.Domain.UserAccount.Roster.ByDomainId.value
                )
        }
)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
public class Roster extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Roster.Id)
    protected long Id;
    public long getId() {
        return Id;
    }

    @ManyToOne()
    @JoinColumn(name = Database.Field.Domain.Roster.OwnerId)
    protected UserAccount Owner;
    public long getOwnerId(){ return Owner.getId();}

    @OneToMany(
            targetEntity = RosterField.class,
            mappedBy = "Owner",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    protected List<RosterField> Custom = new ArrayList<RosterField>();

    @Column(name = Database.Field.Domain.Roster.DomainId)
    protected long DomainId;
    public long getDomainId(){ return DomainId;}

    @Column(name = Database.Field.Domain.Roster.AvatarId)
    protected long AvatarId;
    public long getAvatarId(){ return AvatarId;}
    public void setAvatarId(long id){ AvatarId=id;}

    @Column(name = Database.Field.Domain.Roster.FirstName)
    protected String FirstName;
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @Column(name = Database.Field.Domain.Roster.MiddleName)
    protected String MiddleName;
    public String getMiddleName() {
        return MiddleName;
    }
    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    @Column(name = Database.Field.Domain.Roster.FamilyName)
    protected String FamilyName;
    public String getFamilyName() {        return FamilyName;    }
    public void setFamilyName(String familyName) {        FamilyName = familyName;    }

    @Column(name = Database.Field.Domain.Roster.Alias)
    protected String Alias;
    public String getAlias() {        return Alias;    }
    public void setAlias(String alias) {        Alias = alias;    }

    @Column(name = Database.Field.Domain.Roster.Addresses)
    protected String Addresses;
    public String getAddresses() {        return Addresses;    }
    public void setAddresses(String addresses) {        Addresses = addresses;    }

    @Column(name = Database.Field.Domain.Roster.City)
    protected String City;
    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }

    @Column(name = Database.Field.Domain.Roster.State)
    protected String State;
    public String getState() {
        return State;
    }
    public void setState(String state) {
        State = state;
    }

    @Column(name = Database.Field.Domain.Roster.Postal)
    protected String Postal;
    public String getPostal() {
        return Postal;
    }
    public void setPostal(String postal) {
        Postal = postal;
    }

    @Column(name = Database.Field.Domain.Roster.Country)
    protected String Country;
    public String getCountry() {
        return Country;
    }
    public void setCountry(String country) {
        Country = country;
    }

    @Column(name = Database.Field.Domain.Roster.Websites)
    protected String Websites;
    public String getWebsites() {
        return Websites;
    }
    public void setWebsites(String websites) {
        Websites = websites;
    }

    public Roster() {
        Owner = null;
        DomainId=0;
        Alias="";
    }
    public Roster(UserAccount owner,String alias){
        Owner = owner;
        DomainId = owner.getDomainId();
        Alias=alias;
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
    public static void entityCreated(Entities List,Stored Entity) throws Exception{
        if (Entity instanceof UserAccount){
            UserAccount ua = (UserAccount) Entity;
            if (ua.getMe()==null) {
                Roster me = new Roster(ua,Table.String(Table.Entities.Domain.Roster.Me));
                List.Save(me);
                ua.Contacts.add(me);
                ua.setRosterId(me.Id);
            }
        }
    }
    public static void entityUpdated(Entities List,Stored Entity, boolean Cascade) throws Exception {}
    public static void entityDeleted(Entities List,Stored Entity, boolean Cascade) throws Exception {}

}
