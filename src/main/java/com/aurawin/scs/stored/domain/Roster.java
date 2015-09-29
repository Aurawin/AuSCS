package com.aurawin.scs.stored.domain;


import com.aurawin.core.lang.Table;
import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.Stored;
import com.aurawin.scs.stored.domain.network.Exposure;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate( value = true)
@javax.persistence.Table(name = Database.Table.Domain.Roster)
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
    private UserAccount Owner;
    public long getOwnerId(){ return Owner.getId();}

    @OneToMany(
            targetEntity = RosterField.class,
            mappedBy = "Owner",
            cascade = javax.persistence.CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<RosterField> Custom = new ArrayList<RosterField>();

    @Column(name = Database.Field.Domain.Roster.DomainId)
    private long DomainId;
    public long getDomainId(){ return DomainId;}

    @Column(name = Database.Field.Domain.Roster.AvatarId)
    private long AvatarId;
    public long getAvatarId(){ return AvatarId;}
    public void setAvatarId(long id){ AvatarId=id;}

    @Column(name = Database.Field.Domain.Roster.FirstName)
    private String FirstName;
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @Column(name = Database.Field.Domain.Roster.MiddleName)
    private String MiddleName;
    public String getMiddleName() {
        return MiddleName;
    }
    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    @Column(name = Database.Field.Domain.Roster.FamilyName)
    private String FamilyName;
    public String getFamilyName() {        return FamilyName;    }
    public void setFamilyName(String familyName) {        FamilyName = familyName;    }

    @Column(name = Database.Field.Domain.Roster.Alias)
    private String Alias;
    public String getAlias() {        return Alias;    }
    public void setAlias(String alias) {        Alias = alias;    }

    @Column(name = Database.Field.Domain.Roster.Addresses)
    private String Addresses;
    public String getAddresses() {        return Addresses;    }
    public void setAddresses(String addresses) {        Addresses = addresses;    }

    @Column(name = Database.Field.Domain.Roster.City)
    private String City;
    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }

    @Column(name = Database.Field.Domain.Roster.State)
    private String State;
    public String getState() {
        return State;
    }
    public void setState(String state) {
        State = state;
    }

    @Column(name = Database.Field.Domain.Roster.Postal)
    private String Postal;
    public String getPostal() {
        return Postal;
    }
    public void setPostal(String postal) {
        Postal = postal;
    }

    @Column(name = Database.Field.Domain.Roster.Country)
    private String Country;
    public String getCountry() {
        return Country;
    }
    public void setCountry(String country) {
        Country = country;
    }

    @Column(name = Database.Field.Domain.Roster.Websites)
    private String Websites;
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
    public static void entityCreated(Entities List,Stored Entity) throws Exception{
        if (Entity instanceof UserAccount){
            UserAccount ua = (UserAccount) Entity;
            if (ua.getMe()==null) {
                Roster me = new Roster(ua,Table.String(Table.Entities.Domain.Roster.Me));
                Entities.Create(List,me);
                ua.Contacts.add(me);
                ua.setRosterId(me.Id);
                Entities.Update(List,ua,Entities.CascadeOff);
            }
        }
    }
    public static void entityUpdated(Entities List,Stored Entity, boolean Cascade) throws Exception {}
    public static void entityDeleted(Entities List,Stored Entity, boolean Cascade) throws Exception {}

}
