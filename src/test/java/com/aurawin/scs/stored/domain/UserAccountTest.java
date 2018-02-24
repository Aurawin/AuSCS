package com.aurawin.scs.stored.domain;

import com.aurawin.core.VarString;
import com.aurawin.core.json.Builder;
import com.aurawin.scs.lang.Table;
import com.aurawin.core.time.Time;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;


public class UserAccountTest {
    Builder Parser;
    Account Account1;
    Account Account2;
    Domain Domain;

    @Before
    public void before() throws Exception {
        Parser = new Builder();
        Domain = new Domain("chump.aurawin.com", Table.String(Table.Entities.Domain.Root));
        Account1=new Account(Domain,"test");
        Account1.setAuth("AuthString");
        Account1.setId(1);
        Account1.setFirstIP(3);
        Account1.setLastIP(49);
        Account1.setLastLogin(Time.instantUTC());
        Account1.setConsumption(1000);
        Account1.setQuota(50000);

        Gson gson = Parser.Create();
        String sCode = gson.toJson(Account1);
        if (sCode.length()==0) {
            throw new Exception("Unable to create JSON code for Account.");
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
        Account2 = gson.fromJson(sCode,Account.class);
        if (Account1.equals(Account2)==false){
            throw new Exception("Account1!=Account2");
        }

    }
    public void lookupUserAccount1ByAuth(Session ssn) throws Exception {
        Account ua = (Account) ssn.getNamedQuery(Database.Query.Domain.User.Account.ByAuth.name)
                .setParameter("DomainId",Account1.getDomainId())
                .setParameter("Auth",Account1.getAuth())
                .uniqueResult();
        if ( (ua==null) || (ua.getId()!= Account1.getId() ) ) throw new Exception("Unable to locate Account1");


    }

} 
