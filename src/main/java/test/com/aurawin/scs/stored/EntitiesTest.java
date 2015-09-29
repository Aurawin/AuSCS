package test.com.aurawin.scs.stored;

import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.Roster;
import com.aurawin.scs.stored.domain.UserAccount;
import com.aurawin.scs.stored.domain.network.Network;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

public class EntitiesTest {
    private Entities entities;
    public Manifest manifest;

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testCheckEntitiesAsCreate() throws Exception {
        manifest = new Manifest(
                "Test",                                 // username
                "Test",                                 // password
                "172.16.1.1",                           // host
                5432,                                   // port
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
        entities = new Entities(manifest);

        Domain crD = new Domain("test.com","root");
        if (Entities.Create(entities,crD)==true) {
            Domain lD = (Domain) Entities.Lookup(Domain.class,entities, 1l);
            UserAccount lUA = (UserAccount) Entities.Lookup(UserAccount.class,entities, lD.getId(), lD.getRootId());
            Entities.Fetch(entities, lUA);
            Network lCAB = lUA.getCabinet();
            Roster lME = lUA.getMe();
        } else{
            throw new Exception("Create Domain Failed!");
        }
        Location lc = new Location();
        if (Entities.Create(entities,lc)==true) {
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
            Entities.Create(entities, gp);
            Entities.Update(entities,lc,Entities.CascadeOff);

            Resource rc = new Resource();
            rc.setGroup(gp);
            rc.setName("Phoenix");
            if (Entities.Create(entities,rc) ==true ){
                Node n = new Node();
                n.setResource(rc);
                if (Entities.Create(entities,n)==true) {
                    n.setName("phoenix");
                    n.setIP("172.16.1.1");
                    Entities.Update(entities, n, Entities.CascadeOff);
                } else {
                    throw new Exception("Create Node failed!");
                }
            } else {
                throw new Exception("Create Resource failed!");
            }
            Resource rcDataHouse= new Resource();
            rcDataHouse.setGroup(gp);
            rcDataHouse.setName("Datahouse");
            if (Entities.Create(entities,rcDataHouse)==true){
                Node n=new Node();
                n.setResource(rcDataHouse);
                n.setName("datahouse");
                n.setIP("172.16.1.2");
                if (Entities.Create(entities,n)==true){

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
        manifest = new Manifest(
                "Test",                                 // username
                "Test",                                 // password
                "172.16.1.1",                           // host
                5432,                                   // port
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
        entities = new Entities(manifest);
        Domain crD = (Domain) Entities.Lookup(Domain.class,entities,"test.com");
        if (crD!=null){
            if (Entities.Fetch(entities, crD)==true) {
                UserAccount lUA = (UserAccount) Entities.Lookup(UserAccount.class,entities,crD.getId(), crD.getRootId());
                Entities.Fetch(entities, lUA);
                Network lCAB = lUA.getCabinet();
                Roster lME = lUA.getMe();
            };
        } else {
            throw new Exception("Load Domain Failed!");
        }
        Location l = (Location) Entities.Lookup(Location.class, entities,"Pflugerville");
        Resource r = (Resource) Entities.Lookup(Resource.class, entities,1l);


    }

} 
