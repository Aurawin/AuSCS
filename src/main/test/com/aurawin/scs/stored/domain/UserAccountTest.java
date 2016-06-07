package com.aurawin.scs.stored.domain;

import com.aurawin.core.VarString;
import com.aurawin.core.json.Builder;
import com.aurawin.core.time.Time;
import com.aurawin.lang.Database;
import com.google.gson.Gson;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import static com.aurawin.core.rsr.def.http.Media.State.Query;


public class UserAccountTest {
    Builder Parser;
    UserAccount Account1;
    UserAccount Account2;

    @Before
    public void before() throws Exception {
        Parser = new Builder();
        Account1=new UserAccount(1,"test");
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

    public void saveUserAccount1(Session ssn){
        Transaction tx= ssn.beginTransaction();
        try{
            Query query = Database.Query.Domain.UserAccount.ById.Create(ssn,Account1.getDomainId(),Account1.getId());
            UserAccount ua = (UserAccount) query.uniqueResult();
            if ( (ua!=null) && (ua.getId()== Account1.getId() ) )  {
                ua.Assign(Account1);
                ssn.update(ua);
            } else {
                ssn.save(Account1);
            }
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            throw e;
        }

    }
    public void lookupUserAccount1ByAuth(Session ssn) throws Exception {
        Query query = Database.Query.Domain.UserAccount.ByAuth.Create(ssn,Account1.getDomainId(),Account1.getAuth());
        UserAccount ua = (UserAccount) query.uniqueResult();
        if ( (ua==null) || (ua.getId()!= Account1.getId() ) ) throw new Exception("Unable to locate Account1");


    }

} 
