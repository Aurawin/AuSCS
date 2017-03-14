package com.aurawin.scs.core;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.CommandInfo;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.Entities;
import com.google.gson.Gson;
import org.hibernate.Session;

import java.util.ArrayList;

/**
 * Created by atbrunner on 3/12/17.
 */
@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core",
        Name = "ACL",
        Namespace = "/core/acl",
        Title = "Access Contro Plugin",
        Prompt = "Enable access controls",
        Description = "Facilitates access controls to system information.",
        Vendor = "Aurawin LLC",
        Roles = {"Administrator", "Power User", "User"},
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
            Anonymous=true,
            Name = "DiscoverAll",
            Namespace = "/dsc",
            Title = "Discover All Plugin Commands",
            Prompt = "This feature enabled to discover all available plugin commands.",
            Description = "Allows for discovery of all plugin commands",
            Roles = {"Administrator", "Power User"},
            Format = FormatIO.None
    )
    public PluginState ListAll(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        ArrayList<Plug> pgs = s.Plugins.toArrayList();
        h.Response.Payload.Write(gson.toJson(pgs));
        h.Response.Headers.Update(Field.Code,CoreResult.Ok);
        return PluginState.PluginSuccess;
    }
}
