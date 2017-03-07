package com.aurawin.scs.core;

import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import org.hibernate.Session;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;


@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core",
        Name = "Noid",
        Namespace = "/core/noid",
        Title = "Empty Plug",
        Prompt = "Cannot count on this plugin.",
        Description = "Plug does nothing",
        Vendor = "Aurawin LLC",
        Transport = HTTP_1_1.class
)

public class Noid extends Plug {
    @Override
    public PluginState Setup(Session ssn){
        return super.Setup(ssn);
    }
    @Override
    public PluginState Teardown(Session ssn){
        return PluginState.PluginSuccess;
    }
    @com.aurawin.core.plugin.annotations.Command(
            Anonymous=true,
            Name = "DoSomething",
            Namespace = "/ds",
            Title = "Something",
            Prompt = "Enable this feature to do something.",
            Description = "The command \"Something\" does something!",
            Format = FormatIO.JSON
    )
    public PluginState DoSomething(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        h.Response.Headers.Update(Field.ContentType,"text/plain");
        h.Response.Payload.Write("Plug output - something was done.");
        h.Response.Headers.Update(Field.Code,CoreResult.Ok);
        return PluginState.PluginSuccess;
    }
    @com.aurawin.core.plugin.annotations.Command(
            Anonymous=true,
            Name = "DoAnother",
            Namespace = "/da",
            Title = "Another",
            Prompt = "Enable this feature to do another.",
            Description = "The command \"Another\" does another!",
            Format = FormatIO.JSON
    )
    public PluginState DoAnother(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        h.Response.Headers.Update(Field.ContentType,"text/plain");
        h.Response.Payload.Write("Plug output - another something was done.");
        h.Response.Headers.Update(Field.Code,CoreResult.Ok);

        return PluginState.PluginSuccess;
    }

}
