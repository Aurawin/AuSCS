package com.aurawin.scs.rsr.protocol.transport;

import com.aurawin.core.rsr.def.ItemKind;
import com.aurawin.core.rsr.transport.annotations.Protocol;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cReadFile;

import java.lang.reflect.InvocationTargetException;

import static com.aurawin.core.rsr.def.ItemState.isEstablished;
import static org.junit.Assert.*;
@Protocol(
        Version = Version.class
)
public class AUDISKTest extends AUDISK {
    public static boolean issued = false;
    public AUDISKTest() throws InvocationTargetException,InstantiationException, NoSuchMethodException,IllegalAccessException{
        super(null,ItemKind.None);
    }

    @Override
    public void Connected() {
        super.Connected();

        if (State== isEstablished) {
            if (!issued) {
                issued = true;
                MemoryStream File = new MemoryStream();
                cReadFile cmd = new cReadFile();

                cmd.DiskId = 1;
                cmd.DomainId = 1;
                cmd.FileId = 1221312;
                cmd.FolderId = 13342300;
                cmd.NamespaceId = 1;
                cmd.OwnerId = 1234;

                Response = Query(cmd,null, File);
                try {

                }finally {
                    Response.Release();
                }
            } else {

            }

        }

    }


}