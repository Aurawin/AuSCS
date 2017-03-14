package com.aurawin.scs.core.admin.cms;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.google.gson.Gson;
import org.hibernate.Session;

/**
 * Created by atbrunner on 3/12/17.
 */
@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core.admin.cms",
        Name = "Domain",
        Namespace = "/core/admin/cms/dp",
        Title = "Domain Plugin",
        Prompt = "Enable domain management.",
        Description = "Facilitates domain administration.",
        Roles = {"Administrator"},
        Vendor = "Aurawin LLC",
        Transport = HTTP_1_1.class
)
public class Domain extends Plug {
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
            Name = "Read",
            Namespace = "/r",
            Title = "Read Domain",
            Prompt = "Enables read access to system domains.",
            Description = "Provides read access domain information.",
            Roles = {"Administrator", "Power User"},
            Format = FormatIO.JSON
    )
    public PluginState Read(Session ssn, Item Transport) {
        return PluginState.PluginSuccess;
    }

    @Command(
            Anonymous = false,
            Name = "Write",
            Namespace = "/w",
            Title = "Write Domain",
            Prompt = "Enables write access to system domains.",
            Description = "Provides write access domain information.",
            Roles = {"Administrator"},
            Format = FormatIO.JSON
    )
    public PluginState Write(Session ssn, Item Transport) {
        return PluginState.PluginSuccess;
    }
}
