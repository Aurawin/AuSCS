package com.aurawin.scs.rsr.protocol.audisk.method;


import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.rsr.protocol.audisk.method.command.*;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;
import org.hibernate.Session;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;

public class MakeFolder extends com.aurawin.scs.rsr.protocol.audisk.method.Method {
    public MakeFolder(Item owner){
        super(owner, Table.AuDisk.Method.MakeFolder);
    }

    @Override
    public Result onProcess(Session ssn, Transport transport) throws IllegalAccessException,InvocationTargetException {
        AUDISK t = (AUDISK) transport;
        Server s = (Server) t.Owner.Engine;

        cMakeFolder cmd = t.gson.fromJson(t.Request.Command,cMakeFolder.class);
        Disk disk = s.getDisk(cmd.DiskId);
        if (disk!=null) {
            Path Mount = Settings.Stored.Domain.Network.File.buildMount(disk.getMount());
            Path newPath = Settings.Stored.Domain.Network.File.buildPath(
                    disk.getMount(),
                    cmd.NamespaceId,
                    cmd.DomainId,
                    cmd.OwnerId,
                    cmd.FolderId
            );
            File dNewPath = newPath.toFile();
            if (!dNewPath.isDirectory()) {
                try {
                    Files.createDirectories(newPath, Settings.Stored.Cloud.Disk.Attributes);
                } catch (Exception e){
                    Syslog.Append(getClass().getCanonicalName(),"onProcess.createDirectories", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure,e.getMessage()));
                    return Failure;
                }
            }
            return Result.Ok;
        } else {
            return Result.Failure;
        }
    }
}
