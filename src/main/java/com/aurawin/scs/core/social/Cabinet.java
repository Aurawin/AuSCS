package com.aurawin.scs.core.social;

import com.aurawin.core.json.Builder;
import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.ContentType;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.core.CoreResult;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.solution.Namespace;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.network.File;
import com.google.gson.Gson;
import org.hibernate.Session;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

@com.aurawin.core.plugin.annotations.Plugin(
        Package = "com.aurawin.scs.core.social",
        Name = "Cabinet",
        Namespace = "/core/social/cab",
        Title = "Cabinet Plugin",
        Prompt = "Enable social enabled files/folders/media access",
        Description = "Facilitates access to storage information.",
        Vendor = "Aurawin LLC",
        Roles = {"Administrator", "Power User", "User"},
        Transport = HTTP_1_1.class
)
public class Cabinet extends Plug {
    @Command(
            Anonymous=false,
            Name = "FileRead",
            Namespace = "/flr",
            Title = "File Read",
            Prompt = "This feature enabled to read files.",
            Description = "Allows for reading file meta data.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FileRead(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            com.aurawin.scs.stored.domain.network.File f = Entities.Lookup(com.aurawin.scs.stored.domain.network.File.class, id);
            if (f != null) {
                writeObject(h.Response.Payload,f);
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
            Name = "FileUpdate",
            Namespace = "/flu",
            Title = "File Update",
            Prompt = "This feature enabled to update files.",
            Description = "Allows for updating file meta data.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FileUpdate(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            File update = readObject(h.Request.Payload,File.class);
            File f = Entities.Lookup(File.class, id);
            if (f != null) {
                f.Assign(update);
                try {
                    Entities.Update(f, CascadeOff);
                    h.Response.Headers.Update(Field.Code,CoreResult.Ok);
                } catch (Exception ex){
                    h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
                }
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
            Name = "FileSetData",
            Namespace = "/fsd",
            Title = "File Set Data",
            Prompt = "This feature enabled to update file contents.",
            Description = "Allows for updating file data.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FileSetData(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            File f = Entities.Lookup(File.class, id);
            if (f != null) {
                AuDisk.writeFile(
                        h.Request.Payload,
                        f.getDiskId(),
                        com.aurawin.scs.solution.Namespace.Stored.Domain.Network.File.getId(),

                        h.User.getDomainId(),
                        h.User.getId(),
                        f.getFolderId(),
                        f.getId()
                );

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
            Name = "FileGetData",
            Namespace = "/fgd",
            Title = "File Update",
            Prompt = "This feature enabled to update files.",
            Description = "Allows for updating file meta data.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FileGetData(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            com.aurawin.scs.stored.domain.network.File f = Entities.Lookup(com.aurawin.scs.stored.domain.network.File.class, id);
            if (f != null) {
                AuDisk.readFile(
                        h.Response.Payload,
                        f.getDiskId(),
                        com.aurawin.scs.solution.Namespace.Stored.Domain.Network.File.getId(),

                        h.User.getDomainId(),
                        h.User.getId(),
                        f.getFolderId(),
                        f.getId()
                );
                h.Response.Headers.Update(
                        Field.ContentType,
                        s.ContentTypes.getContentType(
                                com.aurawin.core.file.Util.extractFileExtension(f.getName())
                        )
                );
                h.Response.Headers.Update(Field.Code,CoreResult.Ok);
            } else {
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandInvalidSearch);
        }

        return PluginState.PluginSuccess;
    }
}
