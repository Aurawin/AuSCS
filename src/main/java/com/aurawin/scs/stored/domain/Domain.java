package com.aurawin.scs.stored.domain;


import com.aurawin.core.stored.annotations.*;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.domain.network.Folder;
import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;

@javax.persistence.Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = Database.Table.Domain.Items)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.ByName.name,
                        query = Database.Query.Domain.ByName.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.ById.name,
                        query = Database.Query.Domain.ById.value
                )
        }
)
@EntityDispatch(
        onCreated = true,
        onDeleted = true,
        onUpdated = true
)
@QueryById(
        Name = Database.Query.Domain.ById.name,
        Fields = { "Id" }
)
@QueryByName(
        Name = Database.Query.Domain.ByName.name,
        Fields = {"Name"}
)

@FetchFields(
        {
                @FetchField(
                        Class = Domain.class,
                        Target = "Name"
                )
        }

)
public class Domain extends Stored {
    @javax.persistence.Id
    @Expose(serialize = true, deserialize = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Id)
    protected long Id;
    public long getId() {
        return Id;
    }

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.CertId)
    private long CertId;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Name, nullable = false, unique = true)
    private String Name;


    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.RootName, nullable = false)
    private String RootName;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.Organization, nullable = true)
    private String Organization;


    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.FriendlyName)
    private String FriendlyName;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.DefaultOptionCatchAll)
    private boolean DefaultOptionCatchAll;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.DefaultOptionFiltering)
    private boolean DefaultOptionFiltering;

    @Expose(serialize = true, deserialize = true)
    @Column(name = Database.Field.Domain.DefaultOptionQuota)
    private long DefaultOptionQuota;


    @Expose(serialize = false, deserialize = false)
    @ManyToOne()
    @JoinColumn(name = Database.Field.Domain.RootId)
    @Fetch(value= FetchMode.JOIN)
    public Account Root;

    public Domain() {
        Id = 0;
        CertId = 0;
        Name="";
        RootName = "";
        FriendlyName = "";
        DefaultOptionCatchAll = true;
        DefaultOptionFiltering = true;
        DefaultOptionQuota = 1024 * 1024 * 32; // todo create storage entity for end-user plans
        Root = null;
    }
    public Domain(String name, String rootName){
        Id=0;
        CertId=0;
        Name=name;
        RootName=rootName;
        FriendlyName=name;
        DefaultOptionCatchAll = true;
        DefaultOptionFiltering = true;
        DefaultOptionQuota  = 1024*1014*32; // todo get from system
        Root = null;
    }

    public static Domain fromJSON(Gson Parser, String Data) {
        return (Domain) Parser.fromJson(Data, Domain.class);
    }
    public static Domain fromJSON(Gson Parser, JsonElement Data){
        return (Domain) Parser.fromJson(Data,Domain.class);
    }
    @Override
    public boolean equals(Object o) {
        return (
                (o instanceof Domain) &&
                Id == ((Domain) o).Id &&
                RootName.compareTo( ((Domain) o).RootName) == 0 &&
                FriendlyName.compareTo(((Domain) o).FriendlyName) == 0 &&
                DefaultOptionCatchAll == ((Domain) o).DefaultOptionCatchAll &&
                DefaultOptionFiltering == ((Domain) o).DefaultOptionFiltering &&
                DefaultOptionQuota == ((Domain) o).DefaultOptionQuota
        );

    }

    public void Assign(Domain src) {
        Id = src.Id;
        CertId = src.CertId;
        Root = src.Root;
        FriendlyName = src.FriendlyName;
        DefaultOptionFiltering = src.DefaultOptionFiltering;
        DefaultOptionQuota = src.DefaultOptionQuota;
        DefaultOptionCatchAll = src.DefaultOptionCatchAll;
        RootName = src.RootName;
        Root = src.Root;
    }


    public long getCertId() {
        return CertId;
    }
    public void setCertId(long certId) {
        CertId = certId;
    }

    public String getRootName() {
        return RootName;
    }

    public void setRootName(String rootName) {
        RootName = rootName;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public void setOrganization(String organization){ Organization=organization;}
    public String getOrganization(){return Organization;}

    public String getFriendlyName() {
        return FriendlyName;
    }
    public void setFriendlyName(String friendlyName) {
        FriendlyName = friendlyName;
    }

    public boolean isDefaultOptionCatchAll() {
        return DefaultOptionCatchAll;
    }
    public void setDefaultOptionCatchAll(boolean defaultOptionCatchAll) {
        DefaultOptionCatchAll = defaultOptionCatchAll;
    }

    public boolean isDefaultOptionFiltering() {
        return DefaultOptionFiltering;
    }

    public void setDefaultOptionFiltering(boolean defaultOptionFiltering) {
        DefaultOptionFiltering = defaultOptionFiltering;
    }

    public long getDefaultOptionQuota() {
        return DefaultOptionQuota;
    }

    public void setDefaultOptionQuota(long defaultOptionQuota) {
        DefaultOptionQuota = defaultOptionQuota;
    }

    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Domain d = Entities.Lookup(Domain.class,Name);
            if (d == null) ssn.save(this);
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
    public static void entityDeleted(Stored Entity, boolean Cascade) {
    }


}
