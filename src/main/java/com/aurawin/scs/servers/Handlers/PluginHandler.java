package com.aurawin.scs.servers.Handlers;

import com.aurawin.core.array.KeyItem;
import com.aurawin.core.array.KeyPair;
import com.aurawin.core.plugin.MethodState;
import com.aurawin.core.plugin.Plugin;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.requesthandlers.RequestHandler;
import com.aurawin.core.rsr.def.requesthandlers.RequestHandlerState;
import org.hibernate.Session;

public class PluginHandler implements RequestHandler{
    @Override
    public RequestHandlerState Process(Session ssn, Item item, String Query, KeyPair Parameters) {
        Plugin Plugin = item.getPlugin();
        KeyItem PluginMethod = item.getPluginMethod();
        MethodState methodState = Plugin.Execute(ssn, PluginMethod.Name, item);
        switch (methodState) {
            case msSuccess:
                return RequestHandlerState.Ok;
            case msFailure:
                return RequestHandlerState.Failed;
            case msException:
                return RequestHandlerState.Exception;
            case msNotFound:
                return RequestHandlerState.Missing;
        }
        return RequestHandlerState.None;
    }
}
