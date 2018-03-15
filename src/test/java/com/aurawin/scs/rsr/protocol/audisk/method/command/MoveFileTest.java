package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.json.Builder;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.solution.Namespace;
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
        String sJSON = "";

        cMoveFile mf = new cMoveFile();

        mf.DiskId=1;
        mf.DomainId=1;
        mf.FileId=1;
        mf.OwnerId=1;
        mf.NamespaceId=Kind.getId();
        mf.NewFolderId=2;
        mf.OldFolderId=1;
        sJSON = gson.toJson(mf);
        System.out.println(sJSON);

    }

}