package com.aurawin.scs.core.social;

import com.aurawin.core.plugin.FormatIO;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.PluginState;
import com.aurawin.core.plugin.annotations.Command;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.Stored;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.core.CoreResult;
import com.aurawin.scs.rsr.protocol.http.Server;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.ContentType;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.network.File;
import com.aurawin.scs.stored.domain.network.Folder;
import org.hibernate.Session;

import java.util.ArrayList;

import static com.aurawin.core.file.Util.extractFileExtension;
import static com.aurawin.core.stored.entities.Entities.*;

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
            Name = "FileCreate",
            Method = "POST",
            Namespace = "/file",
            Title = "File Create",
            Prompt = "This feature enabled to create files.",
            Description = "Allows for creating file meta data.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FileCreate(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;

        File f = readObject(h.Request.Payload,File.class);
        if (f != null) {
            try {
                Entities.Save(f, CascadeOff);

                writeObject(h.Response.Payload, f);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous=false,
            Name = "FileRead",
            Method = "GET",
            Namespace = "/file",
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
            Namespace = "/file",
            Title = "File Update",
            Method = "PUT",
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
            Namespace = "/file/data",
            Method = "PUT",
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
                        com.aurawin.core.solution.Namespace.Entities.Identify(com.aurawin.scs.stored.domain.network.File.class),
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
            Namespace = "/file/data",
            Method = "GET",
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
                        com.aurawin.core.solution.Namespace.Entities.Identify(com.aurawin.scs.stored.domain.network.File.class),

                        h.User.getDomainId(),
                        h.User.getId(),
                        f.getFolderId(),
                        f.getId()
                );
                h.Response.Headers.Update(
                        Field.ContentType,
                        s.ContentTypes.stream()
                                .filter(c->c.getExt().equalsIgnoreCase(extractFileExtension(f.getName())))
                                .map(ContentType::getStamp)
                                .findFirst()
                                .orElse("")
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
            Name = "FileDelete",
            Namespace = "/file",
            Method = "DELETE",
            Title = "File Delete",
            Prompt = "This feature enabled to delete files.",
            Description = "Allows for deleting files.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FileDelete(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            com.aurawin.scs.stored.domain.network.File f = Entities.Lookup(com.aurawin.scs.stored.domain.network.File.class, id);
            if (f != null) {
                try {
                    Entities.Purge(f,CascadeOn);
                    Entities.Delete(f,CascadeOn,UseCurrentTransaction);
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
            Name = "FileList",
            Namespace = "/fie",
            Method = "LIST",
            Title = "File List",
            Prompt = "This feature enabled to list files.",
            Description = "Allows for listing files.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FileList(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            ArrayList<Stored> f = Entities.LookupByParentId(File.class, id);
            if (f != null) {
                try {
                    writeObjects(h.Response.Payload,f);
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
            Name = "FolderCreate",
            Method = "POST",
            Namespace = "/folder",
            Title = "Folder Create",
            Prompt = "This feature enabled to create folders.",
            Description = "Allows for creating folders.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FolderCreate(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;

        Folder f = readObject(h.Request.Payload,Folder.class);
        if (f != null) {
            try {
                Entities.Save(f, CascadeOff);
                writeObject(h.Response.Payload, f);
                h.Response.Headers.Update(Field.Code, CoreResult.Ok);
            } catch (Exception ex){
                h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandDMSFailure);
            }
        } else {
            h.Response.Headers.Update(Field.Code,CoreResult.CoreCommandMissingFields);
        }
        return PluginState.PluginSuccess;
    }
    @Command(
            Anonymous=false,
            Name = "FolderRead",
            Method = "GET",
            Namespace = "/folder",
            Title = "Folder Read",
            Prompt = "This feature enabled to read folders.",
            Description = "Allows for reading folder data.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FolderRead(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1L) {
            Folder f = Entities.Lookup(Folder.class, id);
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
            Name = "FolderUpdate",
            Namespace = "/folder",
            Title = "Folder Update",
            Method = "PUT",
            Prompt = "This feature enabled to update folders.",
            Description = "Allows for updating folders.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FoldereUpdate(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            Folder update = readObject(h.Request.Payload,Folder.class);
            Folder f = Entities.Lookup(Folder.class, id);
            if (f != null) {
                f.Assign(update);
                try {
                    Entities.Update(f, CascadeOn);
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
            Name = "FolderDelete",
            Namespace = "/folder",
            Method = "DELETE",
            Title = "Folder Delete",
            Prompt = "This feature enabled to delete folders.",
            Description = "Allows for deleting folders.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FolderDelete(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            Folder f = Entities.Lookup(Folder.class, id);
            if (f != null) {
                try {
                    Entities.Purge(f,CascadeOn);
                    Entities.Delete(f,CascadeOn,UseCurrentTransaction);
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
            Name = "FolderList",
            Namespace = "/folder",
            Method = "LIST",
            Title = "Folder List",
            Prompt = "This feature enabled to list folders.",
            Description = "Allows for listing folders.",
            Roles = {"Administrator", "Power User", "User"},
            Format = FormatIO.JSON
    )
    public PluginState FolderList(Session ssn, Item Transport){
        HTTP_1_1 h = (HTTP_1_1) Transport;
        Server s = (Server) h.Owner.Engine;
        long id =  h.Request.Headers.ValueAsLong(Field.Query, -1l);
        if (id!=-1l) {
            ArrayList<Stored> f = Entities.LookupByParentId(Folder.class, id);
            if (f != null) {
                try {
                    writeObjects(h.Response.Payload,f);
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
}
