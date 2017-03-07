package com.aurawin.scs.core;

import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.scs.stored.domain.UserAccount;
import org.hibernate.Session;
import org.hibernate.query.Query;


@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core",
        Name = "Login",
        Namespace = "/core/login",
        Title = "Login command",
        Prompt = "Enable authentication",
        Description = "Facilitates authentication for users and administrators.",
        Vendor = "Aurawin LLC",
        Transport = HTTP_1_1.class
)

public class Login extends Plug {
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
            Name = "Auth",
            Namespace = "/auth",
            Title = "Authenicate",
            Prompt = "Enable this feature to validate prior authentication.",
            Description = "Allows for quick verification of existing login using cookies",
            Format = FormatIO.JSON
    )
    public PluginState Auth(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;

        Query q = com.aurawin.scs.lang.Database.Query.Domain.UserAccount.ByAuth.Create(
                ssn,
                h.Domain.getId(),
                h.Request.Cookies.ValueAsString(Field.User),
                h.Request.Cookies.ValueAsString(Field.Auth)
        );
        UserAccount a = (UserAccount) q.uniqueResult();
        if (a!=null) {
            h.Response.Headers.Update(Field.Code, CoreResult.Ok);
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.Authenticate);
        }
        return PluginState.PluginSuccess;
    }

}
