package com.aurawin.scs.stored.domain.network;

import com.aurawin.core.lang.Database;
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
import java.util.ArrayList;

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
@Entity
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
    protected long Created;
    public long getCreated() {        return Created;    }
    public void setCreated(long created) {        Created = created;   }

    @Column(name = Database.Field.Domain.Network.Files.Modified)
    protected long Modified;
    public long getModified() {        return Modified;    }
    public void setModified(long modified) {        Modified = modified;    }

    @Column(name = Database.Field.Domain.Network.Files.Size)
    protected long Size;
    public long getSize() { return Size; }

    @Column(name = Database.Field.Domain.Network.Files.Summary, length = 1024*64)
    protected String Summary;
    public String getSummary() {return Summary;  }
    public void setSummary(String summary) {Summary = summary;  }

    public static void entityCreated(Entities List,Stored Entity) { }
    public static void entityUpdated(Entities List,Stored Entity, boolean Cascade) {}
    public static void entityDeleted(Entities List,Stored Entity, boolean Cascade) throws Exception{
        if (Entity instanceof Domain){
            Domain d = (Domain) Entity;
            Session ssn = List.Sessions.openSession();
            try {
                Transaction tx = ssn.beginTransaction();
                try {
                    ArrayList<Stored> lst = Entities.Lookup(
                            File.class.getAnnotation(QueryByDomainId.class),
                            List,
                            d.getId()
                    );
                    for (Stored h : lst) {
                        ssn.delete(h);
                    }
                } finally {
                    tx.commit();
                }
            } finally {
                ssn.close();
            }
        }
    }
}
