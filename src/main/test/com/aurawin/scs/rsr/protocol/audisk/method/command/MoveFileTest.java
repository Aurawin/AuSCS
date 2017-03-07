package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.json.Builder;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Namespace;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by atbrunner on 3/2/17.
 */
public class MoveFileTest {
    Builder bldr;
    Gson gson;
    UniqueId Kind;
    @Before
    public void setUp() throws Exception {
        bldr = new Builder();
        gson = bldr.Create();
        Kind=Namespace.Stored.Domain.Network.File;
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getJSON(){
        cMoveFile cmd = new cMoveFile();
        cmd.DiskId=1;
        cmd.DomainId=1;
        cmd.FileId=1;
        cmd.OwnerId=1;
        cmd.NamespaceId=Kind.getId();
        cmd.NewFolderId=2;
        cmd.OldFolderId=1;
        String sJSON = gson.toJson(cmd);
        System.out.println(sJSON);
    }

}