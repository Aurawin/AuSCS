package com.aurawin.scs.audisk;

import com.aurawin.core.log.Syslog;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.audisk.router.Router;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Resource;
import com.aurawin.scs.stored.domain.network.File;
import com.aurawin.scs.stored.domain.network.Folder;

import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static java.lang.Math.random;

/**
 * Created by atbrunner on 3/2/17.
 */
public class AuDisk {
    protected static ExecutorService Service = Executors.newCachedThreadPool();
    protected static Random randomInt;
    protected static Node Node;

    protected static volatile ArrayList<? extends Stored> Disks;

    public static void Initialize(Node node) throws Exception {
        randomInt = new Random();
        Node = node;
        Disks = Entities.Lookup(Disk.class.getAnnotation(QueryByOwnerId.class), node.getId());
    }

    public static Disk isDiskLocal(long Id) {
        return (Disk) Disks.stream().filter(d -> d.getId() == Id)
                .findFirst()
                .orElse(null);
    }

    public static Disk getNextAvailableDisk() {
        ArrayList<Stored> disks = Entities.Lookup(Disk.class.getAnnotation(QueryAll.class));
        int len = disks.size();
        return (len > 0) ? (Disk) disks.get(randomInt.nextInt(len)) : null;
    }

    public static long getNextAvailableDiskId() {
        ArrayList<Stored> disks = Entities.Lookup(Disk.class.getAnnotation(QueryAll.class));
        int len = disks.size();
        return (len > 0) ? ((Disk) disks.get(randomInt.nextInt(len))).getId() : 0;
    }

    public static void makeDirectory(long DiskId, long NamespaceId, long DomainId, long OwnerId, long FolderId) {
        Disk d = isDiskLocal(DiskId);
        if (d != null) {
            Path Mount = Settings.Stored.Domain.Network.File.buildMount(d.getMount());
            Path newPath = Settings.Stored.Domain.Network.File.buildPath(
                    d.getMount(),
                    NamespaceId,
                    DomainId,
                    OwnerId,
                    FolderId
            );
            java.io.File dNewPath = newPath.toFile();
            if (!dNewPath.isDirectory()) {
                try {
                    Files.createDirectories(newPath, Settings.Stored.Cloud.Disk.Attributes);
                } catch (Exception e) {
                    Syslog.Append(AuDisk.class.getCanonicalName(), "onProcess.createDirectories", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure, e.getMessage()));
                }
            }
        } else {
            Router.makeDirectory(DiskId, NamespaceId, DomainId, OwnerId, FolderId);
        }
    }

    public static ArrayList<String> listFiles(long DiskId, long NamespaceId, long DomainId, long OwnerId, long FolderId) {
        ArrayList<String> r = null;
        Disk d = isDiskLocal(DiskId);
        if (d != null) {
            return d.listFiles(NamespaceId,DomainId,OwnerId, FolderId);
        } else {
            return Router.listFiles(DiskId,NamespaceId,DomainId,OwnerId, FolderId);
        }
    }

    public static void deleteDirectory(Folder folder) {

    }

    public static void deleteFile(File file) {

    }

    public static void createFile(File file, MemoryStream data) {

    }

    public static void writeFile(File file, MemoryStream data) {

    }

    public static void readFile(File file, MemoryStream data) {

    }

    public static void moveFile(File file, long newFolderId) {

    }

    public static ArrayList<String> listFiles() {
        return null;
    }



}
