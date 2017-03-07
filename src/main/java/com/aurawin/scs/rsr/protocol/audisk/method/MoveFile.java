package com.aurawin.scs.rsr.protocol.audisk.method;

import java.io.File;
import java.nio.file.Files;

import com.aurawin.core.lang.Table;
import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Result;

import com.aurawin.scs.rsr.protocol.audisk.method.command.cMoveFile;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;
import org.hibernate.Session;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MoveFile extends com.aurawin.scs.rsr.protocol.audisk.method.Method {


    public MoveFile(Item owner){
        super(owner,com.aurawin.scs.lang.Table.AuDisk.Method.MoveFile);
    }
    @Override
    public Result onProcess(Session ssn, Transport transport) throws IllegalAccessException,InvocationTargetException{
        AUDISK t = (AUDISK) transport;
        Server s = (Server) t.Owner.Engine;

        cMoveFile cmd = t.gson.fromJson(t.Request.Command, cMoveFile.class);

        Disk disk = s.getDisk(cmd.DiskId);
        if (disk!=null) {
            Path Mount = Settings.Stored.Domain.Network.File.buildMount(disk.getMount());
            Path OldPath = Settings.Stored.Domain.Network.File.buildPath(
                    disk.getMount(),
                    cmd.NamespaceId,
                    cmd.DomainId,
                    cmd.OwnerId,
                    cmd.OldFolderId
            );
            Path NewPath = Settings.Stored.Domain.Network.File.buildPath(
                    disk.getMount(),
                    cmd.NamespaceId,
                    cmd.DomainId,
                    cmd.OwnerId,
                    cmd.NewFolderId
            );
            Path OldFile = Settings.Stored.Domain.Network.File.buildFilename(
                    disk.getMount(),
                    cmd.NamespaceId,
                    cmd.DomainId,
                    cmd.OwnerId,
                    cmd.OldFolderId,
                    cmd.FileId
            );
            Path NewFile = Settings.Stored.Domain.Network.File.buildFilename(
                    disk.getMount(),
                    cmd.NamespaceId,
                    cmd.DomainId,
                    cmd.OwnerId,
                    cmd.NewFolderId,
                    cmd.FileId
            );
            File dMount = Mount.toFile();
            File dOldPath = OldPath.toFile();
            File dNewPath = NewPath.toFile();
            if (dMount.isDirectory()==true) {
                if (dOldPath.isDirectory()==true) {
                    if (!dNewPath.isDirectory()) {
                        try {
                            Files.createDirectories(NewPath, Settings.Stored.Cloud.Disk.Attributes);
                        } catch (Exception e){
                            Syslog.Append(getClass().getCanonicalName(),"onProcess.createDirectories",Table.Format(Table.Error.RSR.MethodFailure,e.getMessage()));
                            return Failure;
                        }
                    }
                    try {
                        Files.move(OldFile, NewFile, REPLACE_EXISTING);
                        return Ok;
                    } catch (Exception e){
                        Syslog.Append(getClass().getCanonicalName(),"onProcess.Files.move",Table.Format(Table.Error.RSR.MethodFailure,e.getMessage()));
                        return Failure;
                    }
                } else {
                    return Failure;
                }
            } else {
                return Failure;
            }
        } else {
            return Failure;
        }
    }
}
