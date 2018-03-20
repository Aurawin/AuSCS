package com.aurawin.scs.rsr.protocol.transport;

import com.aurawin.core.array.Bytes;
import com.aurawin.core.json.Builder;

import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.Items;
import com.aurawin.core.rsr.def.CredentialResult;
import com.aurawin.core.rsr.def.ItemKind;

import static com.aurawin.core.rsr.def.EngineState.esFinalize;

import com.aurawin.core.rsr.def.rsrResult;
import com.aurawin.core.rsr.security.Security;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.annotations.Protocol;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.scs.solution.Settings;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.rsr.protocol.audisk.def.Response;
import com.aurawin.scs.rsr.protocol.audisk.def.SecurityMechanismExclusive;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.audisk.method.command.*;
import com.aurawin.core.rsr.transport.methods.Method;
import com.google.gson.Gson;

import org.hibernate.Session;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.aurawin.core.rsr.def.CredentialResult.None;
import static com.aurawin.core.rsr.def.rsrResult.rFailure;
import static com.aurawin.core.rsr.def.rsrResult.rPostpone;
import static com.aurawin.core.rsr.def.rsrResult.rSuccess;
import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;


@Protocol(
        Version = Version.class
)
public class AUDISK extends Item implements Transport
{
    protected Builder bldr = new Builder();
    public Gson gson = bldr.Create();
    public Request Request;
    public Response Response;
    public Queue<Response>Responses = new ConcurrentLinkedQueue<Response>();
    public Queue<Request> Requests = new ConcurrentLinkedQueue<Request>();

    public AUDISK() throws NoSuchMethodException,InvocationTargetException,InstantiationException, IllegalAccessException{
        super(null,ItemKind.None);
    }

    public AUDISK(Items aOwner, ItemKind aKind) throws NoSuchMethodException,InvocationTargetException,InstantiationException, IllegalAccessException {
        super(aOwner,aKind);
        Response = new Response(this);
        Request = new Request(this);

        Methods.registerMethod(new cReadFile());
        Methods.registerMethod(new cWriteFile());
        Methods.registerMethod(new cMakeFile());
        Methods.registerMethod(new cDeleteFile());
        Methods.registerMethod(new cDeleteFolder());
        Methods.registerMethod(new cListFiles());
        Methods.registerMethod(new cMakeFolder());
        Methods.registerMethod(new cMoveFile());

    }
    @Override
    public AUDISK newInstance(Items aOwner) throws InvocationTargetException,NoSuchMethodException,InstantiationException, IllegalAccessException{
        return new AUDISK(aOwner,ItemKind.Client);
    }
    @Override
    public AUDISK newInstance(Items aOwner, SocketChannel aChannel, ItemKind aKind)throws InvocationTargetException,NoSuchMethodException,InstantiationException, IllegalAccessException{
        AUDISK itm = new AUDISK(aOwner, aKind);
        itm.Address= new InetSocketAddress(aChannel.socket().getInetAddress(),aChannel.socket().getPort());
        itm.SocketHandler.Channel=aChannel;
        return itm;
    }

    @Override public void registerSecurityMechanisms(){
        Security.registerMechanism(new SecurityMechanismExclusive());
    };
    @Override
    public void Initialized(){

    }
    @Override
    public void Finalized(){

    }
    @Override
    public void Error(){

    }
    @Override
    public void Connected(){
        // Connection Esablished... Authenticate
        try {
            CredentialResult cr = Security.Peer(Table.Security.Mechanism.AURADISK.Exclusive, getRemoteIp(), Owner.Engine.Root, Owner.Engine.rootDigest);
            if (cr == CredentialResult.Passed) {

            } else {
                // todo log intruder
                this.Error();
            }
        } catch (Exception ex){
            this.Error();
        }

    }
    @Override
    public void Disconnected(){

    }
    @Override
    public void Reset(){
        Request.Reset();
        Response.Reset();
    }
    @Override
    public rsrResult onPeek(){
        rsrResult r = rSuccess;
        long iLoc=Buffers.Recv.Find(Settings.RSR.Items.Header.Separator);
        if (iLoc>0) {
            r = Read(Buffers.Recv.Read(0,iLoc+Settings.RSR.Items.Header.SeparatorLength,true ));
            if (r==rSuccess){
                r =  ( (Request.Size==0) || ( (Request.Size+iLoc+Settings.RSR.Items.Header.SeparatorLength)<=Buffers.Recv.Size) ) ? rSuccess : rPostpone;
            } else{
                r = rPostpone;
            }
        } else if (Buffers.Recv.Size<Settings.RSR.Items.Header.MaxSize) {
            r  =  rPostpone;
        } else {
            r = rFailure;
        }
        return r;
    }

    public rsrResult Read(){
        Reset();
        long iLoc=Buffers.Recv.Find(Settings.RSR.Items.Header.Separator);
        if (iLoc>0) {
            if (Read(Buffers.Recv.Read(0,iLoc+Settings.RSR.Items.Header.SeparatorLength,false ))==rSuccess){
                switch (Kind) {
                    case Server:
                        Buffers.Recv.Move(Request.Payload, Request.Size);
                        break;
                    case Client:
                        Buffers.Recv.Move(Response.Payload, Response.Size);
                        break;
                }

                return rSuccess;
            } else {
                return rPostpone;
            }
        } else {
            return rFailure;
        }
    }

    public rsrResult Read(byte[] input) {
        int iOffset = 0;
        int idxHeadersEnd = 0;
        int idxLineEnd = 0;
        int len = input.length;
        int iChunk = 0;
        byte[] aLine;
        byte[] aHeaders;
        String[] saLine;
        String sLine;
        idxLineEnd = Bytes.indexOf(input, Settings.RSR.Items.Header.Separator.getBytes(), 0);
        if (idxLineEnd > -1) {
            iChunk = iOffset + idxLineEnd;
            aLine = new byte[iChunk];
            System.arraycopy(input, iOffset, aLine, 0, iChunk);
            sLine = Bytes.toString(aLine);
            try {
                switch (Kind) {
                    case Server:
                        Request.Assign(gson.fromJson(sLine, com.aurawin.scs.rsr.protocol.audisk.def.Request.class));
                        break;
                    case Client:
                        Response.Assign(gson.fromJson(sLine, com.aurawin.scs.rsr.protocol.audisk.def.Response.class));
                        break;
                }
                return rSuccess;
            } catch (Exception e) {
                return rFailure;
            }
        } else {
            return rPostpone;
        }
    }
    @Override
    public rsrResult onProcess(Session ssn) {
        rsrResult r = Read();
        Result mr =Failure;
        if (r==rSuccess) {
            switch (Kind) {
                case Server:
                    mr = Methods.Process(Request.Method,ssn,this);
                    switch (mr){
                        case Ok:
                            Response.Code=Ok;
                            break;
                        default :
                            Response.Code=Failure;
                            break;

                    }
                    Respond();
                    break;
                case Client:

                    mr =Methods.Process(Response.Method,ssn,this);
                    switch (mr){
                        case Ok:
                            Response.Code=Ok;
                            break;
                        default :
                            Response.Code=Failure;
                            break;

                    }

                    Response res = new Response(this);
                    res.Assign(Response);

                    break;
            }
        } else {
            switch (Kind) {
                case Server:
                    Response.Code = Failure;
                    Respond();
                    break;
                case Client:
                    break;
            }
        }
        return r;
    }

    public void Respond() {
        Response.Id=Request.Id;
        Response.Size = Response.Payload.Size;
        Response.Protocol=Version.toString();
        String sHeader=gson.toJson(Response);
        Buffers.Send.position(Buffers.Send.size());
        Buffers.Send.Write(sHeader);
        Buffers.Send.Write(Settings.RSR.Items.Header.Separator);
        if (Response.Payload.size()>0) {
            Response.Payload.Move(Buffers.Send);
        }
        queueSend();
    }

    public Response Query(Method cmd, MemoryStream Payload) {
        Response res = null;
        Request req = new Request(this);
        try {
            req.Assign(Request);
            req.Id = Id.Spin();
            req.Protocol = Version.toString();
            req.Method = cmd.getKey();
            req.Command = gson.toJson(cmd);
            if (Payload != null) {
                req.Size = Payload.Size;
                req.Payload = Payload;
            } else {
                req.Size = 0;
            }

            String sHeader = gson.toJson(req);

            Buffers.Send.position(Buffers.Send.size());
            Buffers.Send.Write(sHeader);
            Buffers.Send.Write(Settings.RSR.Items.Header.Separator);
            if (req.Payload.size() > 0) {
                req.Payload.Move(Buffers.Send);
            }

            queueSend();

            Instant ttl = Instant.now().plusMillis(Settings.RSR.ResponseToQueryDelay);
            while ((Owner.Engine.State != esFinalize) && (res == null) && Instant.now().isBefore(ttl)) {
                res = Responses.stream()
                        .filter(r -> r.Id == req.Id)
                        .findFirst()
                        .orElse(null);
                try {
                    if (res == null)
                        Thread.sleep(Settings.RSR.TransportConnect.ResponseDelay);

                } catch (InterruptedException ie) {
                    return null;
                }

            }
        } finally {
            req.Release();
        }


    return res;


    }
    @Override
    public CredentialResult validateCredentials(Session ssn){
        return None;
    }

}
