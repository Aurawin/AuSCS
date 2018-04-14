package com.aurawin.scs.core;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.Entities;
import com.google.gson.Gson;
import org.hibernate.Session;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;

@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core",
        Name = "Account",
        Namespace = "/core/account",
        Title = "Account Plugin",
        Prompt = "Enable account access",
        Description = "Facilitates access to account information.",
        Vendor = "Aurawin LLC",
        Roles = {"Administrator", "Power User", "User"},
        Transport = HTTP_1_1.class
)
public class Account extends Plug {


    @Override
    public PluginState Setup(Session ssn){

        return super.Setup(ssn);
    }
    @Override
    public PluginState Teardown(Session ssn){

        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous=true,
            Name = "Read",
            Namespace = "/ar",
            Title = "Read",
            Prompt = "This feature enabled to read account information.",
            Description = "Allows for updating account information.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.None
    )
    public PluginState Read(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            com.aurawin.scs.stored.domain.user.Account a = Entities.Lookup(com.aurawin.scs.stored.domain.user.Account.class, id);
            if (a != null) {
                writeObject(h.Response.Payload, a);
                h.Response.Headers.Update(Field.Code,CoreResult.Ok);
            } else {
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandInvalidSearch);
        }

        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous=false,
            Name = "Write",
            Namespace = "/aw",
            Title = "Write",
            Prompt = "This feature enables users to update account information.",
            Description = "Allows for account updating.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.None
    )
    public PluginState Write(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        com.aurawin.scs.stored.domain.user.Account a = readObject(h.Request.Payload, com.aurawin.scs.stored.domain.user.Account.class);
        if (a.getId()>0) {
            com.aurawin.scs.stored.domain.user.Account e= Entities.Lookup(com.aurawin.scs.stored.domain.user.Account.class, a.getId());
            if (e != null) {
                e.Assign(a);
                writeObject(h.Response.Payload, e);
                h.Response.Headers.Update(Field.Code,CoreResult.Ok);
            } else {
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }

        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous=false,
            Name = "DiskConsumption",
            Namespace = "/dc",
            Title = "Disk Consumption",
            Prompt = "This feature enables users to calculate disk consumption.",
            Description = "Allows for account quota calulation.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.None
    )
    public PluginState DiskConsumption(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        com.aurawin.scs.stored.domain.user.Account a = Entities.Lookup(com.aurawin.scs.stored.domain.user.Account.class,id);
        if (a!=null) {
            long c = (long) ssn.getNamedQuery(Database.Query.Domain.Network.File.ConsumptionByOwnerId.name)
                    .setParameter("OwnerId",a.getId())
                    .uniqueResult();

            a.setConsumption(c);
            try {
                Entities.Update(a,CascadeOn);
                writeObject(h.Response.Payload, a);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);

            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
            }

        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }

        return PluginState.PluginSuccess;
    }
}
