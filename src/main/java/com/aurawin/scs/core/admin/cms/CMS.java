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

@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core.admin.cms",
        Name = "CMS",
        Namespace = "/core/admin/cms",
        Title = "Content Management Plugin",
        Prompt = "Enable content management.",
        Description = "Facilitates content management administration.",
        Vendor = "Aurawin LLC",
        Roles = {"Administrator", "Power User"},
        Transport = HTTP_1_1.class
)
public class CMS extends Plug {
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
            Anonymous = true,
            Name = "ID",
            Namespace = "/id",
            Title = "Identify File",
            Method = "GET",
            Prompt = "Enables anonymous identification of resources.",
            Description = "Provides access to identify domain level files and folders.",
            Format = FormatIO.JSON
    )
    public PluginState ID(Session ssn, Item Transport) {
        return PluginState.PluginSuccess;
    }

    @Command(
            Anonymous = true,
            Name = "Read",
            Namespace = "/r",
            Method = "GET",
            Title = "Read Files or Folders",
            Prompt = "Enables anonymous access to read files and folders.",
            Description = "Provides access to read domain level files and folders.",
            Format = FormatIO.JSON
    )
    public PluginState Read(Session ssn, Item Transport) {

        return PluginState.PluginSuccess;
    }

    @Command(
            Anonymous = false,
            Name = "Write",
            Namespace = "/w",
            Method = "GET",
            Title = "Write Files or Folders",
            Prompt = "Enables access to write files and folders.",
            Description = "Provides write access to domain level files and folders.",
            Roles = {"Administrator", "Power User"},
            Format = FormatIO.JSON
    )
    public PluginState Write(Session ssn, Item Transport) {

        return PluginState.PluginSuccess;
    }

    @Command(
            Anonymous = false,
            Name = "Editor",
            Namespace = "/edtr",
            Method = "GET",
            Title = "Display Editor",
            Prompt = "Enables access to the WYSIWYG editor.",
            Description = "Provides access to edit domain level files.",
            Roles = {"Administrator", "Power User"},
            Format = FormatIO.JSON
    )
    public PluginState Editor(Session ssn, Item Transport) {

        return PluginState.PluginSuccess;
    }
}
