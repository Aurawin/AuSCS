package com.aurawin.scs.stored.domain.network;

import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.stored.domain.Domain;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
@javax.persistence.Entity
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Network.File.ByName.name,
                        query = Database.Query.Domain.Network.File.ByName.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.File.ByDomainId.name,
                        query = Database.Query.Domain.Network.File.ByDomainId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.File.ByFolderId.name,
                        query = Database.Query.Domain.Network.File.ByFolderId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.File.ByNetworkId.name,
                        query = Database.Query.Domain.Network.File.ByNetworkId.value
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.File.ById.name,
                        query = Database.Query.Domain.Network.File.ById.value
                )
        }
)
@EntityDispatch(
        onCreated = false,
        onDeleted = true,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.Domain.Network.File.ById.name,
        Fields = {
                "DomainId",
                "NetworkId",
                "Id"
        }
)
@QueryByDomainId(Name=Database.Query.Domain.Network.File.ByDomainId.name)
@QueryByNetworkId(Name=Database.Query.Domain.Network.File.ByNetworkId.name)
@QueryByFolderId(Name=Database.Query.Domain.Network.File.ByFolderId.name)

@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = Database.Table.Domain.Network.File)
public class File extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Network.Files.Id)
    protected long Id;
    public long getId(){return Id;}

    @Column(name = Database.Field.Domain.Network.Files.DiskId)
    protected long DiskId;
    public long getDiskId() {      return DiskId;   }
    public void setDiskIdId(long value) {        DiskId = value;    }

    @Column(name = Database.Field.Domain.Network.Files.DomainId)
    protected long DomainId;
    public long getDomainId(){return DomainId;}
    public void setDomainId(long domainId){ DomainId=domainId;}

    @Column(name = Database.Field.Domain.Network.Files.NetworkId)
    protected long NetworkId;
    public long getNetworkId(){return NetworkId;}
    public void setNetworkId(long networkId){ NetworkId = networkId;}

    @Column(name = Database.Field.Domain.Network.Files.FolderId)
    protected long FolderId;
    public long getFolderId() {      return FolderId;   }
    public void setFolderId(long folderId) {        FolderId = folderId;    }

    @Column(name = Database.Field.Domain.Network.Files.Name)
    protected String Name;
    public String getName() {        return Name;    }
    public void setName(String name) {        Name = name;    }

    @Column(name = Database.Field.Domain.Network.Files.Digest, length=16)
    protected String Digest;
    public String getDigest() {  return Digest; }

    @Column(name =Database.Field.Domain.Network.Files.Created)
    protected Instant Created;
    public Instant getCreated() {        return Created;    }
    public void setCreated(Instant created) {        Created = created;   }

    @Column(name = Database.Field.Domain.Network.Files.Modified)
    protected Instant Modified;
    public Instant getModified() {        return Modified;    }
    public void setModified(Instant modified) {        Modified = modified;    }

    @Column(name = Database.Field.Domain.Network.Files.Size)
    protected long Size;
    public long getSize() { return Size; }

    @Column(name = Database.Field.Domain.Network.Files.Summary, length = 1024*64)
    protected String Summary;
    public String getSummary() {return Summary;  }
    public void setSummary(String summary) {Summary = summary;  }

    public void Assign(File Source){
        Id = Source.Id;
        DiskId = Source.DiskId;
        DomainId = Source.DomainId;
        NetworkId = Source.NetworkId;
        FolderId = Source.FolderId;

        Name = Source.Name;
        Digest = Source.Digest;
        Created = Source.Created;
        Modified = Source.Modified;
        Size = Source.Size;
        Summary = Source.Summary;
    }
    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            File f = null;
            Transaction tx = (ssn.isJoinedToTransaction())? ssn.getTransaction() : ssn.beginTransaction();
            try {
                f = (File) ssn.getNamedQuery(Database.Query.Domain.Network.File.ByName.name)
                        .setParameter("DomainId", DomainId)
                        .setParameter("NetworkId", NetworkId)
                        .setParameter("FolderId", FolderId)
                        .setParameter("Name",Name)
                        .uniqueResult();
                if (f == null) {
                    ssn.save(this);
                } else {
                    Assign(f);
                }
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                throw e;
            }
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade) { }
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
    public static void entityDeleted(Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            ArrayList<Stored> lst = Entities.Lookup(
                    File.class.getAnnotation(QueryByDomainId.class),
                    d.getId()
            );
            for (Stored f : lst) {
                Entities.Delete(f,Entities.CascadeOn);
                // todo delete cloud disk file
            }
        } else if (Entity instanceof Network){
            Network n = (Network) Entity;
            ArrayList<Stored> lst = Entities.Lookup(
                    File.class.getAnnotation(QueryByNetworkId.class),
                    n.getId()
            );
            for (Stored f : lst) {
                Entities.Delete(f,Entities.CascadeOn);
                // todo delete cloud disk file
            }

        }
    }
}
