package com.aurawin.scs.stored.domain;

import com.aurawin.core.VarString;
import com.aurawin.core.json.Builder;
import com.aurawin.core.lang.Table;
import com.aurawin.core.time.Time;
import com.aurawin.scs.lang.Database;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;


public class UserAccountTest {
    Builder Parser;
    UserAccount Account1;
    UserAccount Account2;
    Domain Domain;

    @Before
    public void before() throws Exception {
        Parser = new Builder();
        Domain = new Domain("chump.aurawin.com", Table.String(Table.Entities.Domain.Root));
        Account1=new UserAccount(Domain,"test");
        Account1.setAuth("AuthString");
        Account1.setId(1);
        Account1.setFirstIP(3);
        Account1.setLastIP(49);
        Account1.setLockcount(10);
        Account1.setLastLogin(Time.instantUTC());
        Account1.setConsumption(1000);
        Account1.setQuota(50000);

        Gson gson = Parser.Create();
        String sCode = gson.toJson(Account1);
        if (sCode.length()==0) {
            throw new Exception("Unable to create JSON code for UserAccount.");
        }
    }

    @After
    public void after() throws Exception {
        Parser=null;
        Account1=null;
        Account2=null;
    }

    @Test
    public void testFromJSON() throws Exception {
        Gson gson = Parser.Create();
        String sCode = VarString.fromResource(Database.Test.Entities.Domain.UserAccount);
        Account2 = gson.fromJson(sCode,UserAccount.class);
        if (Account1.equals(Account2)==false){
            throw new Exception("Account1!=Account2");
        }

    }
    public void lookupUserAccount1ByAuth(Session ssn) throws Exception {
        UserAccount ua = (UserAccount) ssn.getNamedQuery(Database.Query.Domain.UserAccount.ByAuth.name)
                .setParameter("DomainId",Account1.DomainId)
                .setParameter("Auth",Account1.getAuth())
                .uniqueResult();
        if ( (ua==null) || (ua.getId()!= Account1.getId() ) ) throw new Exception("Unable to locate Account1");


    }

} 
