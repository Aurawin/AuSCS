package com.aurawin.scs.core.admin;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.scs.core.CoreResult;
import com.aurawin.scs.core.def.admin.Certificate;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.google.gson.Gson;
import org.hibernate.Session;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;

@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core.admin",
        Name = "Cloud",
        Namespace = "/core/admin/cloud",
        Title = "Cloud Plugin",
        Prompt = "Enable cloud computing.",
        Description = "Facilitates bootstrapping system administration.",
        Vendor = "Aurawin LLC",
        Transport = HTTP_1_1.class
)
public class Cloud extends Plug {
    private Builder bldr;
    public Gson gson;

    @Override
    public PluginState Setup(Session ssn){
        bldr = new Builder();
        gson = bldr.Create();
        return super.Setup(ssn);
    }
    @Override
    public PluginState Teardown(Session ssn){
        bldr = null;
        gson = null;
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "AddLocation",
            Namespace = "/al",
            Title = "Add Location",
            Prompt = "Enable to provide access to adding location",
            Description = "Allows for administrators to add cloud locations",
            Format = FormatIO.JSON
    )
    public PluginState AddLocation(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Location l = gson.fromJson(src,Location.class);
        if (l!=null) {
            try {
                Entities.Save(l,CascadeOn);
                src= gson.toJson(l);
                h.Response.Payload.Write(src);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "AddGroup",
            Namespace = "/ag",
            Title = "Add Group",
            Prompt = "Enable to provide access to adding groups.",
            Description = "Allows for administrators to add cloud groups for clustering.",
            Format = FormatIO.JSON
    )
    public PluginState AddGroup(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Group g = gson.fromJson(src, Group.class);
        if (g!=null) {
            try {
                Entities.Save(g,CascadeOn);
                src= gson.toJson(g);
                h.Response.Payload.Write(src);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "AddResource",
            Namespace = "/ar",
            Title = "Add Resource",
            Prompt = "Enable to provide access to adding resources.",
            Description = "Allows for administrators to add cloud resources for clustering.",
            Format = FormatIO.JSON
    )
    public PluginState AddResource(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Resource r = gson.fromJson(src, Resource.class);
        if (r!=null) {
            try {
                Entities.Save(r,CascadeOn);
                src= gson.toJson(r);
                h.Response.Payload.Write(src);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "AddNode",
            Namespace = "/an",
            Title = "Add Node",
            Prompt = "Enable to provide access to adding nodes.",
            Description = "Allows for administrators to add cloud nodes for clustering.",
            Format = FormatIO.JSON
    )
    public PluginState AddNode(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Node n = gson.fromJson(src, Node.class);
        if (n!=null) {
            try {
                Entities.Save(n,CascadeOn);
                src= gson.toJson(n);
                h.Response.Payload.Write(src);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "AddService",
            Namespace = "/as",
            Title = "Add Service",
            Prompt = "Enable to provide access to adding services.",
            Description = "Allows for administrators to add cloud services for clustering.",
            Format = FormatIO.JSON
    )
    public PluginState AddService(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Service svc = gson.fromJson(src, Service.class);
        if (svc!=null) {
            try {
                Entities.Save(svc,CascadeOn);
                src= gson.toJson(svc);
                h.Response.Payload.Write(src);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "AddDisk",
            Namespace = "/ad",
            Title = "Add Disk",
            Prompt = "Enable to provide access to adding disks.",
            Description = "Allows for administrators to add cloud disks for clustering.",
            Format = FormatIO.JSON
    )
    public PluginState AddDisk(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Disk d = gson.fromJson(src, Disk.class);
        if (d!=null) {
            try {
                Entities.Save(d,CascadeOn);
                src= gson.toJson(d);
                h.Response.Payload.Write(src);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "AddSelfSignedCertificate",
            Namespace = "/assc",
            Title = "Add Self Signed Certificate",
            Prompt = "Enable to provide access to add self signed certificates.",
            Description = "Allows for administrators to add self signed certificates.",
            Format = FormatIO.JSON
    )
    public PluginState AddSelfSignedCertificate(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Certificate c = gson.fromJson(src, Certificate.class);
        if (c!=null) {
            if ( (c.DomainId!=0) && (c.LocationId!=0)) {
                try {
                    Domain d = Entities.Lookup(Domain.class,c.DomainId);
                    Location l = Entities.Lookup(Location.class,c.LocationId);
                    if ( (d!=null) && (l!=null)){
                        com.aurawin.core.stored.entities.Certificate cert = com.aurawin.scs.stored.bootstrap.Bootstrap.addSelfSignedCertificate(
                                d,
                                d.getName(),
                                l.getRoom(),
                                d.getOrganization(),
                                l.getStreet(),
                                l.getLocality(),
                                l.getRegion(),
                                l.getZip(),
                                l.getCountry(),
                                c.Email,
                                c.Days
                        );
                        if (cert!=null) {
                            c.Id = cert.Id;
                            src = gson.toJson(c);
                            h.Response.Payload.Write(src);
                            h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                        } else {
                            h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                        }
                    } else {
                        h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                    }
                } catch (Exception ex) {
                    h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                }
            } else {
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }

}
