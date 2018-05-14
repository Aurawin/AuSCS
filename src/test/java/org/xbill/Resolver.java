package org.xbill;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xbill.DNS.*;

import static org.xbill.DNS.Lookup.SUCCESSFUL;
import static org.xbill.DNS.Type.*;

public class Resolver {
    public String Server1 = "172.16.1.1";
    public String Server2 = "216.87.155.33";
    public String Query1 = "aurawin.com";
    public String Query2 = "216.87.155.33"; // inverse
    public int Port = 53;
    public int Timeout = 4;
    public Cache Cache;

    public SimpleResolver Resolver;
    public Lookup Lookup;


    @Before
    public void before() throws Exception {
        Cache = new Cache();


        Resolver =new SimpleResolver(Server2);
        Resolver.setPort(Port);
        Resolver.setTimeout(Timeout);

    }

    @After
    public void after() throws Exception{

    }

    @Test
    public void SimpleLookup() throws Exception{
        Lookup = new Lookup(Query1,A);
        Lookup.setResolver(Resolver);
        Lookup.setCache(Cache);
        processResults();

        Lookup = new Lookup(Query1,MX);
        Lookup.setResolver(Resolver);
        Lookup.setCache(Cache);
        processResults();

        Lookup = new Lookup(Query1,CNAME);
        Lookup.setResolver(Resolver);
        Lookup.setCache(Cache);
        processResults();


        Name n = ReverseMap.fromAddress(Query2);
        Lookup = new Lookup(n,PTR);
        Lookup.setResolver(Resolver);
        Lookup.setCache(Cache);
        processResults();

    }

    public void processResults(){
        Record[] records = Lookup.run();
        if (Lookup.getResult()== SUCCESSFUL){
            for (Record r : records){
                if (r instanceof PTRRecord) {
                    System.out.println("PTR "+ ((PTRRecord)r).getTarget().toString() );
                } else if (r instanceof ARecord){
                    System.out.println("A "+ ((ARecord)r).getAddress().toString());
                } else if (r instanceof MXRecord){
                    System.out.println ("MX "+ ((MXRecord)r).getTarget().toString());
                } else if (r instanceof CNAMERecord){
                    System.out.println("CNAME "+ ( (CNAMERecord) r).toString());
                }

            }
        }
    }


}
