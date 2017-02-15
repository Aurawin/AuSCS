package com.aurawin.scs.stored;

import com.aurawin.core.Environment;
import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.def.CertSelfSigned;
import com.aurawin.core.stored.entities.Certificate;
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
                Environment.getString(Table.DBMS.Username), // username
                Environment.getString(Table.DBMS.Password),  // password
                Environment.getString(Table.DBMS.Host),     // host
                Environment.getInteger(Table.DBMS.Port),     // port
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

        Domain vD = db.Entities.Lookup(Domain.class,"chump.aurawin.com");
        if (vD==null) {
            vD = new Domain("chump.aurawin.com", "root");
            db.Entities.Save(vD);
        }

        UserAccount vA = db.Entities.Lookup(UserAccount.class,vD.getId(), vD.getRootId());
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
                Environment.getString(Table.DBMS.Username), // username
                Environment.getString(Table.DBMS.Password),  // password
                Environment.getString(Table.DBMS.Host),     // host
                Environment.getInteger(Table.DBMS.Port),     // port
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

        Domain crD = new Domain("chump.aurawin.com","root");
        if (db.Entities.Save(crD)==true) {
            Domain lD = db.Entities.Lookup(Domain.class,1l);
            UserAccount lUA = db.Entities.Lookup(UserAccount.class,lD.getId(), lD.getRootId());
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

        Certificate cert = new Certificate();
        CertSelfSigned ssc = new CertSelfSigned(
                "chump.aurawin.com",
                "NOC",
                "Aurawin LLC",
                "19309 Stage Line Trail",
                "Pflugerville",
                "TX",
                "78660",
                "US",
                "support@aurawin.com",
                365
        );
        cert.Request=Table.Security.Certificate.Request.SelfSigned;
        cert.DerKey=ssc.getPrivateKeyAsDER();
        cert.TextKey=ssc.PrintPrivateKey();

        cert.DerCert1=ssc.getCertificateAsDER();
        cert.TextCert1 = ssc.PrintCertificate();

        cert.ChainCount=1;
        cert.Expires=ssc.ToDate.toInstant();
        cert.DomainId = crD.getId();

        db.Entities.Save(cert);
        crD.setCertId(cert.Id);
        db.Entities.Update(crD,Entities.CascadeOff);

        Location lc = new Location();
        if (db.Entities.Save(lc)==true) {
            lc.setBuilding("19309");
            lc.setStreet("Stage Line Trail");
            lc.setRegion("Central");
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

            if (db.Entities.Save(rc) ==true ) {
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

            Resource rcChump= new Resource();
            rcChump.setGroup(gp);
            rcChump.setName("Chump");

            if (db.Entities.Save(rcChump)==true){
                Node n=new Node();
                n.setResource(rcChump);
                n.setName("chump");
                n.setIP("172.16.1.2");
                if (db.Entities.Save(n)==true){

                } else {
                    throw new Exception("Create Chump Node failed!");
                }
            } else {
                throw new Exception("Create Chump Resource failed!");
            }


        } else {
            throw new Exception("Create Location failed!");
        }

    }

    @Test
    public void testCheckEntitiesAsUpdate() throws Exception {
        Manifest mf = db.createManifest(
                Environment.getString(Table.DBMS.Username), // username
                Environment.getString(Table.DBMS.Password),  // password
                Environment.getString(Table.DBMS.Host),     // host
                Environment.getInteger(Table.DBMS.Port),     // port
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

        Domain crD = db.Entities.Lookup(Domain.class,"test.com");
        if (crD!=null){
            if (db.Entities.Fetch(crD)==true) {
                UserAccount lUA = db.Entities.Lookup(UserAccount.class,crD.getId(), crD.getRootId());
                db.Entities.Fetch(lUA);
                Network lCAB = lUA.getCabinet();
                Roster lME = lUA.getMe();
            }
        } else {
            throw new Exception("Load Domain Failed!");
        }
        Location l = db.Entities.Lookup(Location.class, "Pflugerville");
        Resource r = db.Entities.Lookup(Resource.class, 1l);

    }

} 
