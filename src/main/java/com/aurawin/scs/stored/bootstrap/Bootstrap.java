package com.aurawin.scs.stored.bootstrap;


import com.aurawin.core.Package;
import com.aurawin.core.ClassScanner;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.annotations.Plugin;
import com.aurawin.core.rsr.IpHelper;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.AnnotatedList;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.FetchKind;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.stored.DNS;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;
import com.aurawin.scs.stored.domain.user.Role;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Set;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;


public class Bootstrap {


    public static ArrayList<Plug> buildPlugins(Class<? extends Package>... args){
        ArrayList<Plug> al = new ArrayList<>();
        Annotation ed;
        Plug plug;
        ClassScanner cs= new ClassScanner();
        Set<Class<? extends Plug>> sa = null;
        for (Class<? extends Package> p : args){
            try {
                sa = cs.scanPackageForPlugins(p);
                for (Class c : sa) {
                    try {
                        plug = (Plug) c.getConstructor().newInstance();
                        al.add(plug);
                    } catch (Exception ex) {

                    }

                }
            } catch (Exception x) {

            }
        }

        return al;
    }
    public static AnnotatedList buildAnnotations(Class<? extends Package>... args){
        AnnotatedList al = new AnnotatedList();
        ClassScanner cs= new ClassScanner();
        try {
            Annotation ed = null;
            Set<Class<?>> sa = null;
            for (Class<? extends Package> p : args){
                sa = cs.scanPackageForNamespaced(p);
                for (Class c : sa) al.add(c);

            }

        } catch (Exception ex){

        }
        return al;
    }
    public static DNS addDNS(String ip) throws Exception{
        DNS d = new DNS();
        d.setHost(IpHelper.toLong(ip));
        Entities.Save(d,CascadeOn);
        return d;
    }
    public static Domain addDomain(String name) throws Exception{
        Domain d = new Domain(name, Table.String(Table.Entities.Domain.Root));

        Entities.Save(d, CascadeOn);

        return d;
    }
    public static Account addUser(Domain domain, String user, String password, UniqueId Role) throws Exception{
        Account a = new Account(domain,user);
        a.setPass(password);
        a.Roles.add(Role.getId());

        Entities.Save(a,CascadeOn);
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
        Certificate c = Certificate.createSelfSigned(
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

        Entities.Save(c,CascadeOn);
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
            Entities.Save(l, CascadeOn);
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
            Entities.Save(g,CascadeOn);
            return g;
        }
        public static Service addService(Node node, UniqueId namespace, int port, int scaleMin, int scaleMax, int scaleStart)
                throws Exception
        {

            Service svc = new Service();
            svc.setNamespace(namespace);
            svc.setOwner(node);
            svc.setPort(port);
            svc.setScaleMax(scaleMax);
            svc.setScaleMin(scaleMin);
            svc.setScaleStart(scaleStart);

            Entities.Save(svc,CascadeOn);
            return svc;
        }
        public static Resource addResource(Group group, String name)throws Exception{
            Resource r = new Resource();
            r.setGroup(group);
            r.setName(name);
            Entities.Save(r,CascadeOn);

            return r;
        }
        public static Node addNode(Resource resource, String name, String ip)throws Exception{
            Node n = new Node();
            n.setResource(resource);
            n.setName(name);
            n.setIP(IpHelper.toLong(ip));
            Entities.Save(n,CascadeOn);
            return n;
        }
        public static Disk addDisk(Node node, Service svc, String mount) throws Exception{
            Disk d = new Disk();
            d.setOwnerId(node.getId());
            d.setServiceId(svc.getId());
            d.setMount(mount);
            Entities.Save(d,CascadeOn);
            return d;
        }
    }

    public static void applyRole(Role role, Account user) throws Exception{
        user.Roles.add(role.getId());
    }
    public static void removeRole(Role role, Account user) throws Exception{
        user.Roles.remove(role.getId());
    }

    public static Role addRole(String Name, String Description) throws Exception{
        Role r = null;
        r = Entities.Lookup(Role.class,Name);
        if (r==null) {
            r = new Role();
            r.Name=Name;
            r.Description=Description;
            Entities.Save(r, CascadeOn);

        } else {
            Entities.Fetch(r,FetchKind.Infinite);
        }
        return r;
    }

}
