package com.aurawin.scs.stored.domain.user;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.scs.lang.Database;
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

import static org.hibernate.engine.spi.CascadeStyles.ALL;

/**
 * Created by atbrunner on 3/13/17.
 */
@Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value=true)
@Table( name = Database.Table.Domain.User.RoleMap)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.User.RoleMap.ByOwnerId.name,
                        query = Database.Query.Domain.User.RoleMap.ByOwnerId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.User.RoleMap.ById.name,
                        query = Database.Query.Domain.User.RoleMap.ById.value
                )
        }
)
@QueryByOwnerId(
        Name = Database.Query.Domain.User.RoleMap.ByOwnerId.name
)
@QueryById(
        Name = Database.Query.Domain.User.RoleMap.ById.name,
        Fields = {"Id"}
)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
public class RoleMap extends Stored {
    @Expose(serialize = true, deserialize = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = com.aurawin.scs.lang.Database.Field.Domain.User.RoleMap.Id)
    protected long Id;
    public long getId() {
        return Id;
    }
    public void setId(long id){ Id=id;}

    @ManyToOne()
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = Database.Field.Domain.User.RoleMap.OwnerId)
    @Fetch(value=FetchMode.JOIN)
    public Role Owner;

    @Column (name= Database.Field.Domain.User.RoleMap.NamespaceId)
    @Expose(serialize = true, deserialize = true)
    public long NamespaceId;

    public RoleMap() {
    }

    public RoleMap(Role owner, long namespaceId) {
        Owner = owner;
        NamespaceId = namespaceId;
        owner.Map.add(this);
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
    public static void entityCreated(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Caascade) {}
    public static void entityDeleted(Stored Entity, boolean Cascade) {}

}
