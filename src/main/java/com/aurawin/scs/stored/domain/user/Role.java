package com.aurawin.scs.stored.domain.user;

import com.aurawin.core.plugin.Plugins;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByName;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.domain.Domain;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value=true)
@Table( name = Database.Table.Domain.User.Role)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.User.Role.ById.name,
                        query = Database.Query.Domain.User.Role.ById.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.User.Role.ByName.name,
                        query = Database.Query.Domain.User.Role.ByName.value
                )
        }
)
@QueryById(
        Name = Database.Query.Domain.User.Role.ById.name,
        Fields = {"Id"}
)
@QueryByName(
        Name = Database.Query.Domain.User.Role.ByName.name,
        Fields = {"Name"}

)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
public class Role extends Stored {
    @Expose(serialize = true, deserialize = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.Role.Id)
    protected long Id;
    public long getId() {
        return Id;
    }
    public void setId(long id){ Id=id;}

    @Column (name = Database.Field.Domain.User.Role.Name)
    @Expose(serialize = true, deserialize = true)
    public String Name;

    @Column (name = Database.Field.Domain.User.Role.Description)
    @Expose(serialize = true, deserialize = true)
    public String Description;

    public void Assign(Role src){
        Id = src.Id;
        Description = src.Description;
        Name = src.Name;
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


    public static void entityCreated(Stored Entity, boolean Cascade) throws Exception {
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            String sName;
            String sDescription;
            Role r;

            sName = com.aurawin.scs.lang.Table.String(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.Administrator);
            sDescription = com.aurawin.scs.lang.Table.Bundle.Domain.getString(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.Description.Administrator);

            r = Entities.Lookup(Role.class,sName);
            if (r==null) {
                Bootstrap.addRole(
                        sName,
                        sDescription
                );
            }

            sName = com.aurawin.scs.lang.Table.String(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.PowerUser);
            sDescription = com.aurawin.scs.lang.Table.Bundle.Domain.getString(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.Description.PowerUser);
            r = Entities.Lookup(Role.class,sName);
            if (r==null) {
                Bootstrap.addRole(
                        sName,
                        sDescription
                );
            }

            sName = com.aurawin.scs.lang.Table.String(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.ContentManagement);
            sDescription=com.aurawin.scs.lang.Table.Bundle.Domain.getString(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.Description.ContentManagement);
            r = Entities.Lookup(Role.class,sName);
            if (r==null){
                Bootstrap.addRole(
                        sName,
                        sDescription
                );
            }

            sName = com.aurawin.scs.lang.Table.String(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.User);
            sDescription=com.aurawin.scs.lang.Table.Bundle.Domain.getString(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.Description.User);
            r = Entities.Lookup(Role.class,sName);
            if (r==null){
                Bootstrap.addRole(
                        sName,
                        sDescription
                );
            }
            sName = com.aurawin.scs.lang.Table.String(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.Guest);
            sDescription=com.aurawin.scs.lang.Table.Bundle.Domain.getString(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.Description.Guest);
            r = Entities.Lookup(Role.class,sName);
            if (r==null){
                Bootstrap.addRole(
                        sName,
                        sDescription
                );
            }

        }
    }
    public static void entityUpdated(Stored Entity, boolean Caascade) {}
    public static void entityDeleted(Stored Entity, boolean Cascade) {}

}
