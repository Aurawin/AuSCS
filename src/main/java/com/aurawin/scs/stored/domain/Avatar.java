package com.aurawin.scs.stored.domain;

import com.aurawin.scs.lang.Database;
import com.aurawin.scs.lang.Namespace;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;

import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.core.time.Time;

import org.hibernate.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;


@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value=true)
@Table( name = Database.Table.Domain.Avatar)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Avatar.ByOwnerAndKind.name,
                        query = Database.Query.Domain.Avatar.ByOwnerAndKind.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Avatar.ById.name,
                        query = Database.Query.Domain.Avatar.ById.value
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
    @Column(name = Database.Field.Domain.Avatar.Id)
    protected long Id;
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.Domain.Avatar.OwnerId)
    protected long OwnerId;

    @Column (name= Database.Field.Domain.Avatar.DomainId)
    private long DomainId;

    @Column (name = Database.Field.Domain.Avatar.Ext)
    private String Ext;

    @Column (name = Database.Field.Domain.Avatar.Created)
    private Instant Created;

    @Column (name = Database.Field.Domain.Avatar.Modified)
    private Instant Modified;

    @Column (name = Database.Field.Domain.Avatar.Data, length = 1024*256)
    private String Data;

    public Avatar(long domainId, long ownerId, long kind) {
        DomainId = domainId;
        OwnerId = ownerId;
        Created = Time.instantUTC();
        Modified= Created;
    }

    public Avatar() {

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
        if (Entity instanceof UserAccount) {
            UserAccount ua = (UserAccount) Entity;
            if (ua.getAvatar() == null) {
                Avatar a = new Avatar(ua.getDomainId(),ua.Id,Namespace.Stored.Domain.Avatar.getId());
                Entities.Save(a,Cascade);
                ua.setAvatar(a);
                Entities.Update(ua,Entities.CascadeOff);
            }
        } else if (Entity instanceof Roster){
            Roster r = (Roster) Entity;
            if (r.getAvatarId()==0) {
                Avatar a = new Avatar(r.getDomainId(),r.getOwnerId(),Namespace.Stored.Domain.UserAccount.Avatar.getId());
                Entities.Save(a,Cascade);
                r.setAvatarId(a.getId());
                Entities.Update(r,Entities.CascadeOff);

            }
        } else if (Entity instanceof Network){
            Network n = (Network) Entity;
            if (n.getAvatarId()==0){
                Avatar a = new Avatar(n.getDomainId(),n.getOwnerId(),Namespace.Stored.Domain.Network.Avatar.getId());
                Entities.Save(a,Cascade);
                n.setAvatarId(a.getId());
                Entities.Update(n,Entities.CascadeOff);
            }
        }
    }
    public static void entityUpdated(Stored Entity, boolean Cascade)throws Exception {

    }
    public static void entityDeleted(Stored Entity, boolean Cascade)throws Exception {
        if (Entity instanceof UserAccount){
            UserAccount ua = (UserAccount) Entity;
            if (ua.Avatar!=null) Entities.Delete(ua.Avatar,Entities.CascadeOn);
        } else if (Entity instanceof Roster){
            Roster r = (Roster) Entity;
            Avatar a = Entities.Lookup(Avatar.class,r.getAvatarId());
            if (a!=null) Entities.Delete(a,Entities.CascadeOn);
        } else if (Entity instanceof Network){
            Network n = (Network) Entity;
            Avatar a  = Entities.Lookup(Avatar.class,n.getAvatarId());
            if (a!=null) Entities.Delete(a,Entities.CascadeOn);
        }
    }

}
