package com.aurawin.scs.stored;

import com.aurawin.core.Environment;
import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.def.CertSelfSigned;
import com.aurawin.core.stored.annotations.AnnotatedList;
import com.aurawin.core.stored.entities.Certificate;
import com.aurawin.core.stored.entities.FetchKind;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.lang.Namespace;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.bootstrap.Stored;
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

import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;

public class EntitiesTest {
    Manifest Manifest;

    @Before
    public void before() throws Exception {
        Manifest=new Manifest(
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
                "EntitiesTest",                                 // database
                Dialect.Postgresql.getValue(),          // Dialect
                Driver.Postgresql.getValue(),            // Driver
                Bootstrap.buildAnnotations()
        );
        Namespace.Merge(Manifest.Namespaces);
        Entities.Initialize(Manifest);

    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testDomainDelete() throws Exception{
        Domain vD = Entities.Lookup(Domain.class,"chump.aurawin.com");
        if (vD==null) {
            vD = new Domain("chump.aurawin.com", Table.String(Table.Entities.Domain.Root));
            Entities.Save(vD,Entities.CascadeOn);
        }

        UserAccount vA = Entities.Lookup(UserAccount.class,vD.getId(), vD.getRootId());
        Vendor vV = new Vendor();
        vV.setDomainId(vD.getId());
        vV.setOwnerId(vA.getId());
        vV.setNamespace("net.some.vendor");
        Entities.Save(vV,Entities.CascadeOn);
        Hawker vH = new Hawker();
        vH.setNamespace("collage");
        vH.setDomainId(vD.getId());
        vH.setVendorId(vV.getId());
        Entities.Save(vH,Entities.CascadeOn);

        Entities.Delete(vD, CascadeOff);
    }
    @Test
    public void testCheckEntitiesAsCreate() throws Exception {
        createNodalData();
        AuDisk.Initialize(Manifest,2l);

        Domain crD = new Domain("chump.aurawin.com",Table.String(Table.Entities.Domain.Root));
        if (Entities.Save(crD,CascadeOn)==true) {
            Domain lD = Entities.Lookup(Domain.class,crD.getId());
            UserAccount lUA = Entities.Lookup(UserAccount.class,lD.getId(), lD.getRootId());
            Entities.Fetch(lUA, FetchKind.Infinite);


            Roster r = new Roster(lUA,"Bestie");
            r.setAddresses("19309 Stage Line Trl.");
            r.setFirstName("Jax");
            r.setMiddleName("");
            r.setFamilyName("Brunner");
            r.setCity("Austin");
            r.setState("TX");
            r.setPostal("78660");


            Entities.Save(r,CascadeOn);



            final GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            final Gson gson = builder.create();

            String jsUA = gson.toJson(lUA);
            String jsD = gson.toJson(lD);

            Network lCAB = lUA.Cabinet;
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

        Entities.Save(cert,CascadeOn);
        crD.setCertId(cert.Id);
        Entities.Update(crD, CascadeOff);

    }

    @Test
    public void testCheckEntitiesAsUpdate() throws Exception {

        Domain crD = Entities.Lookup(Domain.class,"chump.aurawin.com");
        if (crD!=null){
            if (Entities.Fetch(crD,FetchKind.Infinite)==true) {
                UserAccount lUA = Entities.Lookup(UserAccount.class,crD.getId(), crD.getRootId());
                Entities.Fetch(lUA,FetchKind.Infinite);
                Network lCAB = lUA.Cabinet;
                Roster lME = lUA.getMe();
            }
        } else {
            throw new Exception("Load Domain Failed!");
        }
        Location l = Entities.Lookup(Location.class, "Pflugerville");
        Resource r = Entities.Lookup(Resource.class, 1l);

    }
    private void createNodalData() throws Exception {
        Location lc = new Location();
        if (Entities.Save(lc,CascadeOn)==true) {
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
            Entities.Save(gp,CascadeOn);
            Entities.Update(lc, CascadeOff);

            Resource rcPhoenix = new Resource();
            rcPhoenix.setGroup(gp);
            rcPhoenix.setName("Phoenix");

            if (Entities.Save(rcPhoenix,CascadeOn) ==true ) {
                Node n = new Node();
                n.setGroup(gp);
                n.setResource(rcPhoenix);
                n.setName("phoenix");
                n.setIP("172.16.1.1");
                if (Entities.Save(n,CascadeOn)==true) {
                    //db.Entities.Update(n, Entities.CascadeOff);
                } else {
                    throw new Exception("Create Node failed!");
                }
            } else {
                throw new Exception("Create Resource failed!");
            }

            Resource rcChump= new Resource();
            rcChump.setGroup(gp);
            rcChump.setName("Chump");

            if (Entities.Save(rcChump,CascadeOn)==true){
                Node n=new Node();
                n.setResource(rcChump);
                n.setGroup(gp);
                n.setName("chump");
                n.setIP("172.16.1.2");
                if (Entities.Save(n,CascadeOn)==true){
                    Disk d = new Disk();
                    d.setMount("/media/raid");
                    d.setOwnerId(n.getId());
                    if (Entities.Save(d,CascadeOn)==true) {

                    } else {
                        throw new Exception("Create Disk failed!");
                    }
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
} 
