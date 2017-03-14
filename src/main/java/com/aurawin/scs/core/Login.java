package com.aurawin.scs.core;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.scs.core.def.login.CredentialChange;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.user.Account;
import com.google.gson.Gson;
import org.hibernate.Session;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;


@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core",
        Name = "Login",
        Namespace = "/core/login",
        Title = "Login Plugin",
        Prompt = "Enable authentication",
        Description = "Facilitates authentication for users and administrators.",
        Vendor = "Aurawin LLC",
        Roles = {"Administrator", "Power User", "User"},
        Transport = HTTP_1_1.class
)

public class Login extends Plug {
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
            Name = "Auth",
            Namespace = "/ah",
            Title = "Authenicate",
            Prompt = "Enable this feature to validate prior authentication.",
            Description = "Allows for quick verification of existing login using cookies",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.None
    )
    public PluginState Auth(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;

        Account a = (Account)
                ssn.getNamedQuery(com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByAuth.name)
                        .setParameter("DomainId",s.Domain.getId())
                        .setParameter("Name",h.Request.Cookies.ValueAsString(Field.User))
                        .setParameter("Auth",h.Request.Cookies.ValueAsString(Field.Auth))

                .uniqueResult();
        if (a!=null) {
            h.Response.Headers.Update(Field.Code, CoreResult.Ok);
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.Authenticate);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous=true,
            Name = "Login",
            Namespace = "/ln",
            Title = "Login",
            Prompt = "Enable this feature to enable users to login.",
            Description = "Allows users to login to domain.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.None
    )
    public PluginState Login(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        Account a = (Account)
                ssn.getNamedQuery(com.aurawin.scs.lang.Database.Query.Domain.User.Account.ByAuth.name)
                        .setParameter("DomainId",s.Domain.getId())
                        .setParameter("Name",h.Request.Cookies.ValueAsString(Field.User))
                        .setParameter("Auth",h.Request.Cookies.ValueAsString(Field.Auth))
                        .uniqueResult();
        if (a!=null) {
            h.User=a;
            h.Credentials.Digest=a.getAuth();
            h.Credentials.Id=a.getId();
            h.Credentials.Password=a.getPass();
            h.Credentials.ACLUIds=a.ACL.stream().map(acl -> acl.NamespaceId).collect(Collectors.toList());
            h.Credentials.Username=a.getName();
            h.Response.Headers.Update(Field.Auth,h.Credentials.Digest);
            h.Response.Headers.Update(Field.Code, CoreResult.Ok);

        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.Authenticate);
        }
        return PluginState.PluginSuccess;

    }
    @Command(
            Anonymous=false,
            Name = "Change",
            Namespace = "/lc",
            Title = "Change",
            Prompt = "Enable this feature to change passwords.",
            Description = "Allows users to change their password.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState Change(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        CredentialChange cc = gson.fromJson(
                new String(h.Request.Payload.Read()),
                CredentialChange.class
        );
        if (cc!=null) {
            if (h.User.getPass()==cc.PasswordOld ) {
                h.User.setPass(cc.PasswordNew);
                try {
                    Entities.Update(h.User, CascadeOff);
                    h.Response.Headers.Update(Field.Code, CoreResult.Ok);
                } catch (Exception ex){
                    h.Response.Headers.Update(Field.Code, CoreResult.CoreCommandDMSFailure);
                }

            } else {
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.Authenticate);
        }
        return PluginState.PluginSuccess;

    }

}
