package com.aurawin.scs.stored.cloud;

import com.aurawin.core.log.Syslog;
import com.aurawin.scs.solution.Namespace;
import com.aurawin.scs.solution.Settings;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.*;

import com.aurawin.scs.lang.Database;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = Database.Table.Cloud.Disk)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.Cloud.Disk.ById.name,
        Fields = ("Id")
)
@QueryByOwnerId(
        Name = Database.Query.Cloud.Disk.ByOwnerId.name
)
@QueryAll(
        Name = Database.Query.Cloud.Disk.All.name
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Cloud.Disk.All.name,
                        query = Database.Query.Cloud.Disk.All.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Disk.ById.name,
                        query = Database.Query.Cloud.Disk.ById.value
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Disk.ByOwnerId.name,
                        query = Database.Query.Cloud.Disk.ByOwnerId.value
                )
        }
)
public class Disk extends Stored {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Cloud.Disk.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.Cloud.Disk.OwnerId)
    @Expose(serialize = true, deserialize = true)
    protected long OwnerId;
    public long getOwnerId(){return OwnerId;}
    public void setOwnerId(long ownerId){ OwnerId = ownerId;}

//    @Column(name = Database.Field.Cloud.Disk.ServiceId)
//    @Expose(serialize = true, deserialize = true)
//    protected long NamespaceId;
//    public long getServiceId(){return ServiceId;}
//    public void setServiceId(long serviceId){ ServiceId = serviceId;}

    @Column(name = Database.Field.Cloud.Disk.ServiceId)
    @Expose(serialize = true, deserialize = true)
    protected long ServiceId;
    public long getServiceId(){return ServiceId;}
    public void setServiceId(long serviceId){ ServiceId = serviceId;}

    @Column(name = Database.Field.Cloud.Disk.spaceTotal)
    @Expose(serialize = true, deserialize = true)
    protected long spaceTotal;
    public long getSpaceTotal(){ return spaceTotal;}
    public void setSpaceTotal(long value){spaceTotal=value;}

    @Column(name = Database.Field.Cloud.Disk.spaceAvailable)
    @Expose(serialize = true, deserialize = true)
    protected long spaceAvailable;
    public long getSpaceAvailable(){ return spaceAvailable;}
    public void setSpaceAvailable(long value){spaceAvailable=value;}

    @Column(name = Database.Field.Cloud.Disk.Mount)
    @Expose(serialize = true, deserialize = true)
    protected String Mount;
    public String getMount() {  return Mount; }
    public void setMount(String mount) {     Mount = mount; }


    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Transaction tx = (ssn.isJoinedToTransaction())? ssn.getTransaction() : ssn.beginTransaction();
            try {
                ssn.save(this);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                throw e;
            }
        }
    }

    public ArrayList<String> listFiles(long namespaceId, long domainId, long ownerId, long folderId){
        ArrayList<String> result = new ArrayList<String>();
        Path p =Settings.Stored.Domain.Network.File.buildPath(
            Mount,
            namespaceId,
            domainId,
            ownerId,
            folderId
        );
        File fp = p.toFile();
        File[] list = fp.listFiles();
        for (File f : list){
            if (f.isFile()) {
                result.add(f.getName());
            }
        }
        return result;
    }
    private void deleteDirectory(File folder){
        File [] list = folder.listFiles();
        for (File f:list){
            if (Files.isSymbolicLink(f.toPath())){
                f.delete();
            } else if (f.isDirectory()) {
                deleteDirectory(f);
            } else{
                f.delete();
            }
        }
    }
    public void deleteFolder(long namespaceId, long domainId, long ownerId, long folderId){
        Path p =Settings.Stored.Domain.Network.File.buildPath(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId
        );
        deleteDirectory(p.toFile());
    }
    public static void entityCreated(Stored Entity, boolean Cascade){ }
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
