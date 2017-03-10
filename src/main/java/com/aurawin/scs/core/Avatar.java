package com.aurawin.scs.core;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.Entities;
import com.google.gson.Gson;
import org.hibernate.Session;

@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core",
        Name = "Avatar",
        Namespace = "/core/avatar",
        Title = "Avatar Plugin",
        Prompt = "Enable avatars",
        Description = "Facilitates avatars for contacts, networks, and accounts.",
        Vendor = "Aurawin LLC",
        Transport = HTTP_1_1.class
)
public class Avatar extends Plug{
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
            Anonymous=true,
            Name = "Read",
            Namespace = "/ar",
            Title = "Read",
            Prompt = "This feature enabled all to access avatar images.",
            Description = "Allows for avatar images for Networks and Contacts.",
            Format = FormatIO.None
    )
    public PluginState Read(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Search, -1l);
        if (id!=-1l) {
            Avatar a = Entities.Lookup(Avatar.class, id);
            if (a != null) {
                h.Response.Payload.Write(gson.toJson(a));
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
            Prompt = "This feature enables users to update avatar images.",
            Description = "Allows for avatar updating images for Networks and Contacts.",
            Format = FormatIO.None
    )
    public PluginState Write(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        String src = new String(h.Request.Payload.Read());
        Avatar a = gson.fromJson(src,Avatar.class);
        if (a.getId()>0) {
            a = Entities.Lookup(Avatar.class, a.getId());
            if (a != null) {
                h.Response.Payload.Write(gson.toJson(a));
                h.Response.Headers.Update(Field.Code,CoreResult.Ok);
            } else {
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }

        return PluginState.PluginSuccess;
    }

}
