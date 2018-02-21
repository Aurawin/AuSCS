package com.aurawin.scs.rsr.protocol.transport;

import com.aurawin.core.array.Bytes;
import com.aurawin.core.json.Builder;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.Items;
import com.aurawin.core.rsr.def.CredentialResult;
import com.aurawin.core.rsr.def.ItemKind;

import static com.aurawin.core.rsr.def.ItemKind.Server;
import static com.aurawin.core.rsr.def.ItemKind.Client;

import com.aurawin.core.rsr.def.handlers.*;

import com.aurawin.core.rsr.def.http.SecurityMechanismBasic;
import com.aurawin.core.rsr.def.rsrResult;
import com.aurawin.core.rsr.security.Security;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.annotations.Protocol;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.core.solution.Settings;
import com.aurawin.scs.rsr.protocol.audisk.def.Response;
import com.aurawin.scs.rsr.protocol.audisk.def.SecurityMechanismExclusive;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.audisk.method.command.*;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.stored.cloud.Resource;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Node;
import com.google.gson.Gson;
import org.hibernate.Session;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;

import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.aurawin.core.rsr.def.CredentialResult.None;
import static com.aurawin.core.rsr.def.rsrResult.rFailure;
import static com.aurawin.core.rsr.def.rsrResult.rPostpone;
import static com.aurawin.core.rsr.def.rsrResult.rSuccess;
import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.scs.rsr.protocol.audisk.def.Status.Ok;
import static com.aurawin.scs.rsr.protocol.audisk.def.Status.Fail;

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

    public AUDISK() throws InstantiationException, IllegalAccessException{
        super(null,ItemKind.None);
    }

    public AUDISK(Items aOwner, ItemKind aKind) throws InstantiationException, IllegalAccessException {
        super(aOwner,aKind);
        Response = new Response();
        Request = new Request();

        Methods.registerMethod(new cDeleteFile());
        Methods.registerMethod(new cDeleteFolder());
        Methods.registerMethod(new cListAllFiles());
        Methods.registerMethod(new cMakeFolder());
        Methods.registerMethod(new cMoveFile());
        Methods.registerMethod(new cReadFile());
        Methods.registerMethod(new cWriteFile());
    }
    @Override
    public AUDISK newInstance(Items aOwner) throws InstantiationException, IllegalAccessException{
        return new AUDISK(aOwner,ItemKind.Client);
    }
    @Override
    public AUDISK newInstance(Items aOwner, SocketChannel aChannel, ItemKind aKind)throws InstantiationException, IllegalAccessException{
        AUDISK itm = new AUDISK(aOwner, aKind);
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
                        Request = gson.fromJson(sLine, com.aurawin.scs.rsr.protocol.audisk.def.Request.class);
                        break;
                    case Client:
                        Response = gson.fromJson(sLine, com.aurawin.scs.rsr.protocol.audisk.def.Response.class);
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
                            Response.Code=Fail;
                            break;

                    }
                    Respond();
                    break;
                case Client:
                    Methods.Process(Response.Method,ssn,this);
                    break;
            }
        } else {
            switch (Kind) {
                case Server:
                    Response.Code = Fail;
                    Respond();
                    break;
                case Client:
                    break;
            }
        }
        return r;
    }

    public void Respond() {
        String sHeader=gson.toJson(Response);
        Buffers.Send.position(Buffers.Send.size());
        Buffers.Send.Write(sHeader);
        Buffers.Send.Write(Settings.RSR.Items.Header.Separator);
        if (Response.Payload.size()>0) {
            Response.Payload.Move(Buffers.Send);
        }
        queueSend();
    }
    public void Query(){
        String sHeader=gson.toJson(Request);
        Buffers.Send.position(Buffers.Send.size());
        Buffers.Send.Write(sHeader);
        Buffers.Send.Write(Settings.RSR.Items.Header.Separator);
        if (Request.Payload.size()>0) {
            Request.Payload.Move(Buffers.Send);
        }

        queueSend();
    }
    @Override
    public CredentialResult validateCredentials(Session ssn){
        return None;
    }

}
