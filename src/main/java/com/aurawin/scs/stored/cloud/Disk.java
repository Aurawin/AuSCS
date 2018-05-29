package com.aurawin.scs.stored.cloud;

import com.aurawin.core.log.Syslog;
import com.aurawin.core.stream.FileStream;
import com.aurawin.core.stream.MemoryStream;
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
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Entity
@Namespaced
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
                ),
                @NamedQuery(
                        name  = Database.Query.Cloud.Disk.ByServiceId.name,
                        query = Database.Query.Cloud.Disk.ByServiceId.value
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

    public String[] listFiles(long namespaceId, long domainId, long ownerId, long folderId){
        ArrayList<String> al = new ArrayList<String>();
        Path p =Settings.Stored.Domain.Network.File.buildPath(
            Mount,
            namespaceId,
            domainId,
            ownerId,
            folderId
        );
        File fp = p.toFile();
        File[] list = fp.listFiles();
        if (list!=null) {
            for (File f : list){
                if (f.isFile()) {
                    al.add(f.getName());
                }
            }
        }
        String[] a = new String[al.size()];
        return al.toArray(a);
    }
    private boolean deleteDirectory(File folder){
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
        return folder.delete();
    }
    public boolean deleteFolder(long namespaceId, long domainId, long ownerId, long folderId){
        Path p =Settings.Stored.Domain.Network.File.buildPath(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId
        );
        return deleteDirectory(p.toFile());
    }
    public boolean makeFolder(long namespaceId, long domainId, long ownerId, long folderId){
        Path p =Settings.Stored.Domain.Network.File.buildPath(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId
        );
        File dNewPath = p.toFile();
        if (!dNewPath.isDirectory()) {
            try {
                Files.createDirectories(p, Settings.Stored.Cloud.Disk.Attributes);
                return true;
            } catch (Exception e) {
                Syslog.Append(getClass().getCanonicalName(), "makeDirectory", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure, e.getMessage()));
                return false;
            }
        }else{
            return true;
        }
    }
    public boolean deleteFile(long namespaceId, long domainId, long ownerId, long folderId, long fileId){
        Path dPath = Settings.Stored.Domain.Network.File.buildPath(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId
        );
        File fPath = dPath.toFile();
        if (fPath.isDirectory()) {
            try {
                Path pFile = Settings.Stored.Domain.Network.File.buildFilename(
                        Mount,
                        namespaceId,
                        domainId,
                        ownerId,
                        folderId,
                        fileId
                );
                Files.delete(pFile);
                return true;
            } catch (Exception e){
                Syslog.Append(getClass().getCanonicalName(),"deleteFile", e.getMessage());
                return false;
            }
        } else{
            return false;
        }
    }
    public boolean makeFile(long namespaceId, long domainId, long ownerId, long folderId, long fileId){
        Path dPath = Settings.Stored.Domain.Network.File.buildPath(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId
        );
        File fPath = dPath.toFile();
        try {
            File f = new File(fPath,String.valueOf(fileId));
            f.createNewFile();
            f.setExecutable(false);
            f.setReadable(true);
            f.setWritable(true);
            FileStream fs = new FileStream(f,"rw");
            try {
                fs.truncate(0);
            } finally {
                fs.close();
            }

            return true;
        } catch (Exception e){
            Syslog.Append(getClass().getCanonicalName(),"makeFile", e.getMessage());
            return false;
        }

    }
    public boolean moveFile(long namespaceId, long domainId, long ownerId, long olderFolderId, long newFolderId, long fileId){
        Path OldPath = Settings.Stored.Domain.Network.File.buildPath(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                olderFolderId
        );
        Path NewPath = Settings.Stored.Domain.Network.File.buildPath(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                newFolderId
        );
        Path OldFile = Settings.Stored.Domain.Network.File.buildFilename(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                olderFolderId,
                fileId
        );
        Path NewFile = Settings.Stored.Domain.Network.File.buildFilename(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                newFolderId,
                fileId
        );
        Path pMount = Paths.get(Mount);
        File dMount = pMount.toFile();
        File dOldPath = OldPath.toFile();
        File dNewPath = NewPath.toFile();
        if (dMount.isDirectory()==true) {
            if (dOldPath.isDirectory()==true) {
                if (!dNewPath.isDirectory()) {
                    try {
                        Files.createDirectories(NewPath, Settings.Stored.Cloud.Disk.Attributes);
                    } catch (Exception e){
                        Syslog.Append(getClass().getCanonicalName(),"moveFile.createDirectories", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure,e.getMessage()));
                        return false;
                    }
                }
                try {
                    Files.move(OldFile, NewFile, REPLACE_EXISTING);
                    return true;
                } catch (Exception e){
                    Syslog.Append(getClass().getCanonicalName(),"moveFile.Files.move", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure,e.getMessage()));
                    return false;
                }
            } else {
                return false;
            }
        } else{
            return false;
        }
    }
    public boolean readFile(MemoryStream Data, long namespaceId, long domainId, long ownerId, long folderId, long fileId){
        Path dPath = Settings.Stored.Domain.Network.File.buildFilename(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId,
                fileId
        );
        try {
            Data.LoadFromFile(dPath.toFile());
            return true;
        } catch (Exception e) {
            Syslog.Append(getClass().getCanonicalName(), "readFile", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure, e.getMessage()));
            return false;
        }
    }

    public boolean readPartialFile(MemoryStream Data, long namespaceId, long domainId, long ownerId, long folderId, long fileId, long position, int size){
        Path dPath = Settings.Stored.Domain.Network.File.buildFilename(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId,
                fileId
        );
        try {
            RandomAccessFile f = new RandomAccessFile(dPath.toString(), "r");
            try{
                byte[] bData = new byte[size];
                f.seek(position);
                Data.Clear();
                Data.Write(bData);
                return true;
            }finally{
                f.close();
            }

        } catch (Exception e) {
            Syslog.Append(getClass().getCanonicalName(), "readPartialFile", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure, e.getMessage()));
            return false;
        }
    }
    public boolean writeFile(MemoryStream Data, long namespaceId, long domainId, long ownerId, long folderId, long fileId) {
        Path dPath = Settings.Stored.Domain.Network.File.buildFilename(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId,
                fileId
        );
        try {
            Data.SaveToFile(dPath.toFile());
            return true;
        } catch (Exception e) {
            Syslog.Append(getClass().getCanonicalName(), "writeFile", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure, e.getMessage()));
            return false;
        }
    }
    public boolean writePartialFile(MemoryStream Data, long namespaceId, long domainId, long ownerId, long folderId, long fileId, long position, int size){
        Path dPath = Settings.Stored.Domain.Network.File.buildFilename(
                Mount,
                namespaceId,
                domainId,
                ownerId,
                folderId,
                fileId
        );
        try {
            RandomAccessFile f = new RandomAccessFile(dPath.toString(), "rw");
            try{
                f.seek(position); // need testing
                f.write(Data.Read());
                return true;
            }finally{
                f.close();
            }

        } catch (Exception e) {
            Syslog.Append(getClass().getCanonicalName(), "writePartialFile", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure, e.getMessage()));
            return false;
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade){ }
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
