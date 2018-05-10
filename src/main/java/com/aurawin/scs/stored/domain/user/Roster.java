package com.aurawin.scs.stored.domain.user;


import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.lang.Table;
import com.aurawin.core.stored.annotations.FetchField;
import com.aurawin.core.stored.annotations.FetchFields;
import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.ArrayList;
import java.util.List;

@Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate( value = true)
@FetchFields(
        {
                @FetchField(
                        Class = RosterField.class,
                        Target = "Custom"
                )
        }
)
@javax.persistence.Table(name = Database.Table.Domain.User.Account.Roster.Items)
@NamedQueries(
        {
                @NamedQuery(
                        name = Database.Query.Domain.User.Roster.ByDomainId.name,
                        query = Database.Query.Domain.User.Roster.ByDomainId.value
                )
        }
)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
public class Roster extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.User.Roster.Id)
    protected long Id;
    public long getId() {
        return Id;
    }

    @Expose(serialize = false, deserialize = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne()
    @JoinColumn(name = Database.Field.Domain.User.Roster.OwnerId)
    @Fetch(value=FetchMode.JOIN)
    protected Account Owner;
    public long getOwnerId(){ return Owner.getId();}

    @Expose(serialize = true, deserialize = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @Fetch(value=FetchMode.SUBSELECT)
    @OneToMany(
            targetEntity = RosterField.class,
            mappedBy = "Owner",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true

    )
    protected List<RosterField> Custom = new ArrayList<>();

    @Column(name = Database.Field.Domain.User.Roster.DomainId)
    protected long DomainId;
    public long getDomainId(){ return DomainId;}

    @Column(name = Database.Field.Domain.User.Roster.AvatarId)
    protected long AvatarId;
    public long getAvatarId(){ return AvatarId;}
    public void setAvatarId(long id){ AvatarId=id;}

    @Column(name = Database.Field.Domain.User.Roster.FirstName)
    protected String FirstName;
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @Column(name = Database.Field.Domain.User.Roster.MiddleName)
    protected String MiddleName;
    public String getMiddleName() {
        return MiddleName;
    }
    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    @Column(name = Database.Field.Domain.User.Roster.FamilyName)
    protected String FamilyName;
    public String getFamilyName() {        return FamilyName;    }
    public void setFamilyName(String familyName) {        FamilyName = familyName;    }

    @Column(name = Database.Field.Domain.User.Roster.Alias)
    protected String Alias;
    public String getAlias() {        return Alias;    }
    public void setAlias(String alias) {        Alias = alias;    }

    @Column(name = Database.Field.Domain.User.Roster.Addresses)
    protected String Addresses;
    public String getAddresses() {        return Addresses;    }
    public void setAddresses(String addresses) {        Addresses = addresses;    }

    @Column(name = Database.Field.Domain.User.Roster.City)
    protected String City;
    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }

    @Column(name = Database.Field.Domain.User.Roster.State)
    protected String State;
    public String getState() {
        return State;
    }
    public void setState(String state) {
        State = state;
    }

    @Column(name = Database.Field.Domain.User.Roster.Postal)
    protected String Postal;
    public String getPostal() {
        return Postal;
    }
    public void setPostal(String postal) {
        Postal = postal;
    }

    @Column(name = Database.Field.Domain.User.Roster.Country)
    protected String Country;
    public String getCountry() {
        return Country;
    }
    public void setCountry(String country) {
        Country = country;
    }

    @Column(name = Database.Field.Domain.User.Roster.Phones)
    protected String Phones;
    public String getPhones() {        return Phones;    }
    public void setPhones(String phones) {        Phones = phones;    }

    @Column(name = Database.Field.Domain.User.Roster.Emails)
    protected String Emails;
    public String getEmails() {        return Emails;    }
    public void setEmails(String emails) {        Emails = emails;    }

    @Column(name = Database.Field.Domain.User.Roster.Websites)
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
    public Roster(Account owner, String alias){
        Owner = owner;
        DomainId = owner.getDomainId();
        Alias=alias;
        Owner.Contacts.add(this);
    }
    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Transaction tx = (ssn.isJoinedToTransaction()) ? ssn.getTransaction() : ssn.beginTransaction();
            try {
                ssn.save(this);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
            }
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Account){
            Account ua = (Account) Entity;
            if (ua.Me==null) {
                ua.Me = new Roster(ua,Table.String(Table.Entities.Domain.User.Roster.Me));
                Entities.Save(ua.Me,Cascade);
                Entities.Update(ua,Entities.CascadeOff);
            }
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade) throws Exception {}
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception {
        if (Entity instanceof Account) {
            Account ua = (Account) Entity;
            Roster r = ua.Me;
            //if (r!=null) Entities.Delete(r,Entities.CascadeOn);
        }
    }

}
