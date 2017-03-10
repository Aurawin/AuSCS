package com.aurawin.scs.stored.bootstrap;


import com.aurawin.core.rsr.def.CertSelfSigned;
import com.aurawin.core.stored.annotations.AnnotatedList;
import com.aurawin.core.stored.entities.Certificate;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Namespace;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;

import java.util.ArrayList;

public class Bootstrap {

    public static AnnotatedList buildAnnotations(){
        AnnotatedList al = new AnnotatedList();
        al.addAll(Plugins.buildAnnotations());
        al.addAll(Stored.buildAnnotations());
        return al;
    }
    public static ArrayList<UniqueId> buildNamespaces(){
        return Namespace.Discover();
    }


    public static Domain addDomain(String name) throws Exception{
        Domain d = new Domain(name, Table.String(Table.Entities.Domain.Root));

        Entities.Save(d, Entities.CascadeOn);

        return d;
    }
    public static Account addUser(Domain domain, String user, String password) throws Exception{
        Account a = new Account(domain,user);
        a.setPass(password);
        Entities.Save(a,Entities.CascadeOff);
        return a;
    }
    public static Certificate addSelfSignedCertificate(
            Domain domain,
            String commonName,
            String organizationUnit,
            String organizationName,
            String street,
            String locality,
            String state,
            String postal,
            String country,
            String email,
            int days
    ) throws Exception{
        CertSelfSigned ssc = new CertSelfSigned(
                commonName,
                organizationName,
                organizationUnit,
                postal,
                locality,
                state,
                postal,
                country,
                email,
                days
        );

        Certificate c = new Certificate();
        c.Request= com.aurawin.core.lang.Table.Security.Certificate.Request.SelfSigned;
        c.DerKey=ssc.getPrivateKeyAsDER();
        c.TextKey=ssc.PrintPrivateKey();

        c.DerCert1=ssc.getCertificateAsDER();
        c.TextCert1 = ssc.PrintCertificate();

        c.ChainCount=1;
        c.Expires=ssc.ToDate.toInstant();
        c.DomainId = domain.getId();

        Entities.Save(c,Entities.CascadeOn);
        domain.setCertId(c.Id);
        Entities.Update(domain,Entities.CascadeOff);
        return c;
    }
    public static class Cloud {
        public static Location addLocation(
                String Area, String Building, String Street, String Floor, String Room, String Locality,
                String Region, String Postal, String Country

        ) throws Exception{
            Location l = new Location();
            l.setArea(Area);
            l.setBuilding(Building);
            l.setStreet(Street);
            l.setFloor(Floor);
            l.setRoom(Room);
            l.setLocality(Locality);
            l.setRegion(Region);
            l.setZip(Postal);
            l.setCountry(Country);
            Entities.Save(l, Entities.CascadeOn);
            return l;
        }
        public static Group addGroup(
                Location location,
                String name,
                String rack,
                String row
        ) throws Exception{
            Group g = new Group();
            g.setName(name);
            g.setRack(rack);
            g.setRow(row);
            g.setLocation(location);
            Entities.Save(g,Entities.CascadeOn);
            return g;
        }
        public static Service addService(Node node, UniqueId namespace, int port, int scaleMin, int scaleMax, int scaleStart)
                throws Exception
        {

            Service svc = new Service();
            svc.setNamespace(namespace);
            svc.setNode(node);
            svc.setPort(port);
            svc.setScaleMax(scaleMax);
            svc.setScaleMin(scaleMin);
            svc.setScaleStart(scaleStart);

            Entities.Save(svc,Entities.CascadeOn);
            return svc;
        }
        public static Resource addResource(Group group, String name)throws Exception{
            Resource r = new Resource();
            r.setGroup(group);
            r.setName(name);
            Entities.Save(r,Entities.CascadeOn);

            return r;
        }
        public static Node addNode(Resource resource, String name, String ip)throws Exception{
            Node n = new Node();
            n.setResource(resource);
            n.setName(name);
            n.setIP(ip);
            Entities.Save(n,Entities.CascadeOn);
            return n;
        }
        public static Disk addDisk(Node node, Service svc, String mount) throws Exception{
            Disk d = new Disk();
            d.setOwnerId(node.getId());
            d.setServiceId(svc.getId());
            d.setMount(mount);
            Entities.Save(d,Entities.CascadeOn);
            return d;
        }
    }
}
