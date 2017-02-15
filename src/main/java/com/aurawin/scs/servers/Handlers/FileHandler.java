package com.aurawin.scs.servers.Handlers;

import com.aurawin.core.array.KeyPair;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.http.Field;
import com.aurawin.core.rsr.def.requesthandlers.RequestHandler;
import com.aurawin.core.rsr.def.requesthandlers.RequestHandlerState;
import com.aurawin.core.stream.MemoryStream;
import org.hibernate.Session;

public class FileHandler implements RequestHandler{
        @Override
        public RequestHandlerState Process(Session ssn, Item item, String Query, KeyPair Parameters) {
            MemoryStream payload = item.getResponsePayload();
            KeyPair Headers = item.getResponseHeaders();
            Headers.Update(Field.ContentType,"text/plain");
            payload.Write("FileHandler output");
            return RequestHandlerState.Ok;
        }
}
