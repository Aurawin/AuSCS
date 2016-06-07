package com.aurawin.scs.stored;

import com.aurawin.lang.Database;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.Roster;
import com.aurawin.scs.stored.domain.UserAccount;
import com.aurawin.scs.stored.domain.network.Network;
import com.aurawin.scs.stored.domain.vendor.Vendor;

import com.aurawin.scs.stored.domain.vendor.hawker.Hawker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

public class EntitiesTest {
    private DBMS db;

    @Before
    public void before() throws Exception {
        db=new DBMS();
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testDomainDelete() throws Exception{
        Manifest mf=db.createManifest(
                "Test",                                 // username
                "Test",                                 // password
                "172.16.1.1",                           // host
                5432,                                   // port
                Database.Config.Automatic.Commit.On,    // autocommit
                2,                                      // Min Poolsize
                20,                                     // Max Poolsize
                1,                                      // Pool Acquire Increment
                50,                                     // Max statements
                10,                                     // timeout
                Database.Config.Automatic.Update,       //
                "Test",                                 // database
                Dialect.Postgresql.getValue(),          // Dialect
                Driver.Postgresql.getValue()            // Driver
        );
        db.setManifest(mf);

        Domain vD = (Domain) db.Entities.Lookup(Domain.class,"repository.foo.com");
        if (vD==null) {
            vD = new Domain("repository.foo.com", "root");
            db.Entities.Save(vD);
        }

        UserAccount vA = (UserAccount) db.Entities.Lookup(UserAccount.class,vD.getId(), vD.getRootId());
        Vendor vV = new Vendor();
        vV.setDomainId(vD.getId());
        vV.setOwnerId(vA.getId());
        vV.setNamespace("net.some.vendor");
        db.Entities.Save(vV);
        Hawker vH = new Hawker();
        vH.setNamespace("collage");
        vH.setDomainId(vD.getId());
        vH.setVendorId(vV.getId());
        db.Entities.Save(vH);

        db.Entities.Delete(vD,Entities.CascadeOff);
    }
    @Test
    public void testCheckEntitiesAsCreate() throws Exception {
        Manifest mf=db.createManifest(
                "Test",                                 // username
                "Test",                                 // password
                "172.16.1.1",                           // host
                5432,                                   // port
                Database.Config.Automatic.Commit.On,    // autocommit
                2,                                      // Min Poolsize
                20,                                     // Max Poolsize
                1,                                      // Pool Acquire Increment
                50,                                     // Max statements
                10,                                     // timeout
                Database.Config.Automatic.Create,       //
                "Test",                                 // database
                Dialect.Postgresql.getValue(),          // Dialect
                Driver.Postgresql.getValue()            // Driver
        );
        db.setManifest(mf);

        Domain crD = new Domain("test.com","root");
        if (db.Entities.Save(crD)==true) {
            Domain lD = (Domain) db.Entities.Lookup(Domain.class,1l);
            UserAccount lUA = (UserAccount) db.Entities.Lookup(UserAccount.class,lD.getId(), lD.getRootId());
            db.Entities.Fetch(lUA);

            final GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            final Gson gson = builder.create();

            String jsUA = gson.toJson(lUA);
            Network lCAB = lUA.getCabinet();
            Roster lME = lUA.getMe();
            if (lME==null) throw new Exception ("UserAccount get Me Failed!");
        } else{
            throw new Exception("Create Domain Failed!");
        }
        Location lc = new Location();
        if (db.Entities.Save(lc)==true) {
            lc.setBuilding("19309");
            lc.setStreet("Stage Line Trail");
            lc.setRegion("Southwest");
            lc.setArea("Austin");
            lc.setLocality("Pflugerville");
            lc.setCountry("USA");
            lc.setFloor("1st");
            lc.setRoom("Office");
            lc.setZip("78660");


            Group gp = new Group();
            gp.setName("Office");
            gp.setRack("Primary");
            gp.setRow("Primary");
            gp.setLocation(lc);
            db.Entities.Save(gp);
            db.Entities.Update(lc,Entities.CascadeOff);

            Resource rc = new Resource();
            rc.setGroup(gp);
            rc.setName("Phoenix");
            if (db.Entities.Save(rc) ==true ){
                Node n = new Node();
                n.setResource(rc);
                if (db.Entities.Save(n)==true) {
                    n.setName("phoenix");
                    n.setIP("172.16.1.1");
                    db.Entities.Update(n, Entities.CascadeOff);
                } else {
                    throw new Exception("Create Node failed!");
                }
            } else {
                throw new Exception("Create Resource failed!");
            }
            Resource rcDataHouse= new Resource();
            rcDataHouse.setGroup(gp);
            rcDataHouse.setName("Datahouse");
            if (db.Entities.Save(rcDataHouse)==true){
                Node n=new Node();
                n.setResource(rcDataHouse);
                n.setName("datahouse");
                n.setIP("172.16.1.2");
                if (db.Entities.Save(n)==true){

                } else {
                    throw new Exception("Create DataHouse Node failed!");
                }
            } else {
                throw new Exception("Create Datahouse Resource failed!");
            }


        } else {
            throw new Exception("Create Location failed!");
        }

    }

    @Test
    public void testCheckEntitiesAsUpdate() throws Exception {
        Manifest mf = db.createManifest(
                "Test",                                 // username
                "Test",                                 // password
                "172.16.1.1",                           // host
                5432,                                   // port
                Database.Config.Automatic.Commit.On,    // autocommit
                2,                                      // Min Poolsize
                20,                                     // Max Poolsize
                1,                                      // Pool Acquire Increment
                50,                                     // Max statements
                10,                                     // timeout
                Database.Config.Automatic.Update,       //
                "Test",                                 // database
                Dialect.Postgresql.getValue(),          // Dialect
                Driver.Postgresql.getValue()            // Driver
        );
        db.setManifest(mf);

        Domain crD = (Domain) db.Entities.Lookup(Domain.class,"test.com");
        if (crD!=null){
            if (db.Entities.Fetch(crD)==true) {
                UserAccount lUA = (UserAccount) db.Entities.Lookup(UserAccount.class,crD.getId(), crD.getRootId());
                db.Entities.Fetch(lUA);
                Network lCAB = lUA.getCabinet();
                Roster lME = lUA.getMe();
            };
        } else {
            throw new Exception("Load Domain Failed!");
        }
        Location l = (Location) db.Entities.Lookup(Location.class, "Pflugerville");
        Resource r = (Resource) db.Entities.Lookup(Resource.class, 1l);

    }

} 
