package com.aurawin.scs.core.admin;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.domain.user.Account;
import com.aurawin.scs.core.CoreResult;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.google.gson.Gson;
import org.hibernate.Session;

import java.util.ArrayList;

/**
 * Created by atbrunner on 3/12/17.
 */
@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core.admin",
        Name = "ACL",
        Namespace = "/core/admin/acl",
        Title = "ACL Plugin",
        Prompt = "Enable administration of access controls.",
        Description = "Facilitates access control administration.",
        Roles = {"Administrator"},
        Vendor = "Aurawin LLC",
        Transport = HTTP_1_1.class
)
public class ACL extends Plug {
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
            Name = "Grant",
            Namespace = "/g",
            Method = "PUT",
            Title = "Grant access to a particular kind of entity.",
            Prompt = "Enables access control of entities.",
            Description = "Provides access approval of core methods and entities.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState Grant(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        com.aurawin.scs.stored.security.ACL acl = gson.fromJson(
                new String(h.Request.Payload.Read()),
                com.aurawin.scs.stored.security.ACL.class
        );
        if ( (acl!=null) && (acl.NamespaceId!=0) && (acl.getOwnerId()!=0)){
            Account ua = Entities.Lookup(Account.class,acl.getOwnerId());
            if (ua!=null) {
                com.aurawin.scs.stored.security.ACL acl2 = (com.aurawin.scs.stored.security.ACL) ssn.getNamedQuery(Database.Query.Security.ACL.ByNamespaceIdAndOwnerId.name)
                        .setParameter("NamespaceId", acl.NamespaceId)
                        .setParameter("OwnerId", acl.getOwnerId())
                        .uniqueResult();
                if (acl2 == null) {
                    acl2 = new com.aurawin.scs.stored.security.ACL(ua,acl.NamespaceId);
                    acl2.Identify(ssn);
                }
                h.Response.Payload.Write(gson.toJson(acl2));
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } else {
                h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandMissingFields);
            }
        } else {
            h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandMissingFields);
        }

        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous = false,
            Name = "Revoke",
            Namespace = "/r",
            Method = "DELETE",
            Title = "Revoke access to a particular kind of entity.",
            Prompt = "Enables access control of entities.",
            Description = "Provides access denial of core methods and entities.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState Revoke(Session ssn, Item Transport) {
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        com.aurawin.scs.stored.security.ACL acl = gson.fromJson(
                new String(h.Request.Payload.Read()),
                com.aurawin.scs.stored.security.ACL.class
        );
        if ( (acl!=null) && (acl.NamespaceId!=0) && (acl.getOwnerId()!=0) ){
            ArrayList<Stored> acls =Entities.toArrayList(
                    ssn.getNamedQuery(Database.Query.Security.ACL.ByNamespaceIdAndOwnerId.name)
                    .setParameter("NamespaceId",acl.NamespaceId)
                    .setParameter("OwnerId",acl.getOwnerId())
                    .list()
            );
            for (Stored a:acls){
                try {
                    Entities.Delete(a, Entities.CascadeOn);
                } catch (Exception ex){
                    h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                }
            }

            h.Response.Headers.Update(Field.Code, CoreResult.Ok);

        } else {
            h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandMissingFields);
        }

        return PluginState.PluginSuccess;
    }
}
