package com.aurawin.scs.core.admin;

import com.aurawin.core.json.Builder;
import com.aurawin.scs.lang.Table;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.scs.core.CoreResult;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.Gson;
import org.hibernate.Session;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;

@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core.admin",
        Name = "Admin",
        Namespace = "/core/admin",
        Title = "Administration Plugin",
        Prompt = "Enable administration",
        Description = "Facilitates system administration.",
        Vendor = "Aurawin LLC",
        Roles = {"Administrator"},
        Transport = HTTP_1_1.class
)
public class Admin extends Plug {
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
            Name = "AddUser",
            Namespace = "/u",
            Method = "POST",
            Title = "Add User",
            Prompt = "Enable to provide access to adding users.",
            Description = "Allows for administrators to add new users.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState AddUser(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Account a = gson.fromJson(src,Account.class);
        if (a!=null) {
            if (a.getDomainId()==0) a.setDomainId(s.Domain.getId());
            Account e = Entities.Lookup(Account.class,a.getDomainId(),a.getName());
            if (e!=null){
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDuplicate);
            } else {
                try {
                    Entities.Save(a, CascadeOn);
                    h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                    h.Response.Payload.Write(gson.toJson(a));
                } catch (Exception ex){
                    h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                }
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "DeleteUser",
            Namespace = "/u",
            Method = "DELETE",
            Title = "Delete User",
            Prompt = "Enable to provide access to deleting users.",
            Description = "Allows for administrators to delete users.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState DeleteUser(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Account a = gson.fromJson(src,Account.class);
        if (a!=null) {
            if (a.getDomainId()==0) a.setDomainId(s.Domain.getId());
            Account e = Entities.Lookup(Account.class,a.getDomainId(),a.getName());
            if (e!=null){
                try {
                    Entities.Delete(e, CascadeOn);
                    h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                } catch (Exception ex){
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
    @Command(
            Anonymous = false,
            Name = "WriteUser",
            Namespace = "/u",
            Method = "PUT",
            Title = "Write User",
            Prompt = "Enable to provide access to updating users.",
            Description = "Allows for administrators to update users.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState WriteUser(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Account a = gson.fromJson(src,Account.class);
        if ( (a!=null) && (a.getId()!=0) ) {
            Account e = Entities.Lookup(Account.class,a.getId());
            if (e!=null){
                try {
                    e.Assign(a);
                    Entities.Update(e, CascadeOn);
                    h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                } catch (Exception ex){
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
    @Command(
            Anonymous = false,
            Name = "AddDomain",
            Namespace = "/d",
            Method = "POST",
            Title = "Add Domain",
            Prompt = "Enable to provide access to adding domains.",
            Description = "Allows for administrators to add new domains.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState AddDomain(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Domain d = gson.fromJson(src,Domain.class);
        if ( (d!=null) && (d.getName()!=null) && (d.getName().length()>0) ) {
            if ((d.getRootName().length()==0) ) d.setRootName(Table.String(Table.Entities.Domain.Root));
            Domain e = Entities.Lookup(Domain.class,d.getName());
            if (e!=null){
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDuplicate);
            } else {
                try {
                    Entities.Save(d, CascadeOn);
                    h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                    h.Response.Payload.Write(gson.toJson(d));
                } catch (Exception ex){
                    h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                }
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "DeleteDomain",
            Namespace = "/d",
            Method = "DELETE",
            Title = "Delete Domain",
            Prompt = "Enable to provide access to delete domains.",
            Description = "Allows for administrators to delete domains.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState DeleteDomain(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Domain d = gson.fromJson(src,Domain.class);
        if ( (d!=null) && (d.getId()!=0)) {
            Domain e = Entities.Lookup(Domain.class,d.getId());
            if (e!=null){
                try {
                    Entities.Delete(e, CascadeOn);
                    h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                } catch (Exception ex){
                    h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                }
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDuplicate);
            } else {
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "WriteDomain",
            Namespace = "/d",
            Method = "PUT",
            Title = "Write Domain",
            Prompt = "Enable to provide access to updating domain.",
            Description = "Allows for administrators to update domains.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState WriteDomain(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Domain d = gson.fromJson(src,Domain.class);
        if ( (d!=null) && (d.getId()!=0) ) {
            Domain e = Entities.Lookup(Domain.class,d.getId());
            if (e!=null){
                try {
                    e.Assign(d);
                    Entities.Update(e, CascadeOn);
                    h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                } catch (Exception ex){
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
