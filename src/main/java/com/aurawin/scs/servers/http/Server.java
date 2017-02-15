package com.aurawin.scs.servers.http;

import com.aurawin.core.array.KeyPair;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.ItemKind;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.core.rsr.def.requesthandlers.RequestHandler;
import com.aurawin.core.rsr.def.requesthandlers.RequestHandlerState;
import com.aurawin.core.rsr.transport.http_1_1;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.servers.Handlers.FileHandler;
import com.aurawin.scs.servers.Handlers.PluginHandler;
import org.hibernate.Session;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.aurawin.core.rsr.def.ResolveResult.rrFile;
import static com.aurawin.core.rsr.def.ResolveResult.rrPlugin;

public class Server extends com.aurawin.core.rsr.server.Server{
    FileHandler rhFile;
    PluginHandler rhPlugin;

    public Server(InetSocketAddress sa,  String aHostName) throws IOException, NoSuchMethodException {
        super(sa, new http_1_1(null, ItemKind.Server), false, aHostName);
        rhFile=new FileHandler();
        rhPlugin=new PluginHandler();
        Managers.addRequestHandler(rrFile,rhFile);
        Managers.addRequestHandler(rrPlugin,rhPlugin);
    }
}
