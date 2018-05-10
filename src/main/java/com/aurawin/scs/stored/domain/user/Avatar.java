package com.aurawin.scs.stored.domain.user;

import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;

import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.core.time.Time;

import com.google.gson.annotations.Expose;
import org.hibernate.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.core.stored.entities.Entities.UseCurrentTransaction;


@Entity
@Namespaced
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value=true)
@Table( name = Database.Table.Domain.User.Avatar)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.User.Avatar.ByOwnerAndKind.name,
                        query = Database.Query.Domain.User.Avatar.ByOwnerAndKind.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.User.Avatar.ById.name,
                        query = Database.Query.Domain.User.Avatar.ById.value
                )
        }
)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
public class Avatar extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.User.Avatar.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.Domain.User.Avatar.OwnerId)
    @Expose(serialize = true, deserialize = true)
    protected long OwnerId;

    @Column (name= Database.Field.Domain.User.Avatar.DomainId)
    @Expose(serialize = true, deserialize = true)
    private long DomainId;

    @Column (name = Database.Field.Domain.User.Avatar.Ext)
    @Expose(serialize = true, deserialize = true)
    private String Ext;

    @Column (name = Database.Field.Domain.User.Avatar.Created)
    @Expose(serialize = true, deserialize = true)
    private Instant Created;

    @Column (name = Database.Field.Domain.User.Avatar.Modified)
    @Expose(serialize = true, deserialize = true)
    private Instant Modified;

    @Column (name = Database.Field.Domain.User.Avatar.Data, length = 1024*256)
    @Expose(serialize = true, deserialize = true)
    private String Data;

    public Avatar(long domainId, long ownerId, long kind) {
        DomainId = domainId;
        OwnerId = ownerId;
        Created = Time.instantUTC();
        Modified= Created;
    }

    public Avatar() {

    }
    public void Assign(Avatar src){
        Id = src.Id;
        OwnerId = src.OwnerId;
        DomainId = src.DomainId;
        Ext = src.Ext;
        Created = src.Created;
        Modified = src.Modified;
        Data = src.Data;
    }
    public String getExt() {
        return Ext;
    }

    public void setExt(String ext) {
        Ext = ext;
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

    public static void entityCreated(Stored Entity, boolean Cascade)throws Exception {
        if (Entity instanceof Account) {
            Account ua = (Account) Entity;
            if (ua.Avatar == null) {
                ua.Avatar = new Avatar(
                        ua.getDomainId(),
                        ua.getId(),
                        Namespace.Entities.Identify(com.aurawin.scs.stored.domain.user.Avatar.class)
                );
                Entities.Save(ua.Avatar,Cascade);
                Entities.Update(ua,Entities.CascadeOff);
            }
        } else if (Entity instanceof Roster){
            Roster r = (Roster) Entity;
            if (r.getAvatarId()==0) {
                Avatar a = new Avatar(
                        r.getDomainId(),
                        r.getOwnerId(),
                        Namespace.Entities.Identify(com.aurawin.scs.stored.domain.user.Avatar.class)
                );
                Entities.Save(a,Cascade);
                r.setAvatarId(a.getId());
                Entities.Update(r,Entities.CascadeOff);

            }
        } else if (Entity instanceof Network){
            Network n = (Network) Entity;
            if (n.getAvatarId()==0){
                Avatar a = new Avatar(
                        n.getDomainId(),
                        n.getOwnerId(),
                        Namespace.Entities.Identify(com.aurawin.scs.stored.domain.network.Avatar.class)
                );
                Entities.Save(a,Cascade);
                n.setAvatarId(a.getId());
                Entities.Update(n,Entities.CascadeOff);
            }
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade)throws Exception {

    }
    public static void entityDeleted(Stored Entity, boolean Cascade)throws Exception {
        if (Entity instanceof Roster){
            Roster r = (Roster) Entity;
            Avatar a = Entities.Lookup(Avatar.class,r.getAvatarId());
            if (a!=null) Entities.Delete(a,CascadeOn,UseCurrentTransaction);
        } else if (Entity instanceof Network){
            Network n = (Network) Entity;
            Avatar a  = Entities.Lookup(Avatar.class,n.getAvatarId());
            if (a!=null) Entities.Delete(a,CascadeOn,UseCurrentTransaction);
        }
    }

}
