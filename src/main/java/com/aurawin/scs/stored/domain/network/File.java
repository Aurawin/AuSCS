package com.aurawin.scs.stored.domain.network;

import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.solution.Namespace;
import com.aurawin.scs.stored.Entities;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.scs.stored.annotations.QueryByDomainId;
import com.aurawin.scs.stored.annotations.QueryByFolderId;
import com.aurawin.scs.stored.annotations.QueryByNetworkId;
import com.aurawin.scs.stored.domain.Domain;

import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.annotations.SerializedName;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;

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
                ),
                @NamedQuery(
                        name  = Database.Query.Domain.Network.File.ConsumptionByOwnerId.name,
                        query = Database.Query.Domain.Network.File.ConsumptionByOwnerId.value
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
    @SerializedName("ID")
    protected long Id;
    public long getId(){return Id;}

    @Column(name = Database.Field.Domain.Network.Files.DiskId)
    @SerializedName("DSK")
    protected long DiskId;
    public long getDiskId() {      return DiskId;   }
    public void setDiskIdId(long value) {        DiskId = value;    }

    @Column(name = Database.Field.Domain.Network.Files.DomainId)
    @SerializedName("DID")
    protected long DomainId;
    public long getDomainId(){return DomainId;}
    public void setDomainId(long domainId){ DomainId=domainId;}

    @Column(name = Database.Field.Domain.Network.Files.NetworkId)
    @SerializedName("NID")
    protected long NetworkId;
    public long getNetworkId(){return NetworkId;}
    public void setNetworkId(long networkId){ NetworkId = networkId;}

    @Column(name = Database.Field.Domain.Network.Files.OwnerId)
    @SerializedName("OID")
    protected long OwnerId;
    public long getOwnerId(){return OwnerId;}
    public void setOwnerId(long ownerId){ OwnerId = ownerId;}

    @Column(name = Database.Field.Domain.Network.Files.FolderId)
    @SerializedName("FID")
    protected long FolderId;
    public long getFolderId() {      return FolderId;   }
    public void setFolderId(long folderId) {        FolderId = folderId;    }

    @Column(name = Database.Field.Domain.Network.Files.Name)
    @SerializedName("NME")
    protected String Name;
    public String getName() {        return Name;    }
    public void setName(String name) {        Name = name;    }

    @Column(name = Database.Field.Domain.Network.Files.Digest, length=16)
    @SerializedName("DG")
    protected String Digest;
    public String getDigest() {  return Digest; }

    @Column(name =Database.Field.Domain.Network.Files.Created)
    @SerializedName("CTD")
    protected Instant Created;
    public Instant getCreated() {        return Created;    }
    public void setCreated(Instant created) {        Created = created;   }

    @Column(name = Database.Field.Domain.Network.Files.Modified)
    @SerializedName("MTD")
    protected Instant Modified;
    public Instant getModified() {        return Modified;    }
    public void setModified(Instant modified) {        Modified = modified;    }

    @Column(name = Database.Field.Domain.Network.Files.Size)
    @SerializedName("SZE")
    protected long Size;
    public long getSize() { return Size; }

    @Column(name = Database.Field.Domain.Network.Files.Summary, length = 1024*64)
    @SerializedName("SRY")
    protected String Summary;
    public String getSummary() {return Summary;  }
    public void setSummary(String summary) {Summary = summary;  }

    public void Assign(File Source){
        Id = Source.Id;
        DiskId = Source.DiskId;
        DomainId = Source.DomainId;
        OwnerId = Source.OwnerId;
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
         if (Entity instanceof File) {
             File f = (File) Entity;
             AuDisk.deleteFile(
                     ((File) f).DiskId,
                     Namespace.Stored.Domain.Network.File.getId(),
                     ((File) f).DomainId,
                     ((File) f).OwnerId,
                     ((File) f).FolderId,
                     ((File) f).Id
             );
         } else if (Entity instanceof Network){
            Network n = (Network) Entity;
            ArrayList<Stored> lst = Entities.Lookup(
                    File.class.getAnnotation(QueryByNetworkId.class),
                    n.getId()
            );
            for (Stored f : lst) {
                Entities.Delete(f, CascadeOn);
            }
        } else if (Entity instanceof Folder) {
            Folder f = (Folder) Entity;
            ArrayList<Stored> lst = Entities.Lookup(
                    File.class.getAnnotation(QueryByFolderId.class),
                    f.Id
            );
            for (Stored s: lst){
                Entities.Delete(s,CascadeOn);
            }
        }
    }
}
