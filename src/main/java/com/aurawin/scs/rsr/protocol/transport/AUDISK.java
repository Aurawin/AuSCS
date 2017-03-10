package com.aurawin.scs.rsr.protocol.transport;

import com.aurawin.core.array.Bytes;
import com.aurawin.core.json.Builder;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.Items;
import com.aurawin.core.rsr.def.CredentialResult;
import com.aurawin.core.rsr.def.ItemKind;
import com.aurawin.core.rsr.def.handlers.*;

import com.aurawin.core.rsr.def.rsrResult;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.annotations.Protocol;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.core.solution.Settings;
import com.aurawin.scs.rsr.protocol.audisk.def.Response;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.audisk.method.MoveFile;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.stored.cloud.Resource;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Node;
import com.google.gson.Gson;
import org.hibernate.Session;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;

import java.nio.channels.SocketChannel;

import static com.aurawin.core.rsr.def.CredentialResult.None;
import static com.aurawin.core.rsr.def.rsrResult.rFailure;
import static com.aurawin.core.rsr.def.rsrResult.rPostpone;
import static com.aurawin.core.rsr.def.rsrResult.rSuccess;
import static com.aurawin.scs.rsr.protocol.audisk.def.Status.Ok;
import static com.aurawin.scs.rsr.protocol.audisk.def.Status.Fail;

@Protocol(
        Version = Version.class
)
public class AUDISK extends Item implements Transport,ResourceUploadHandler,ResourceDeleteHandler,
        ResourceCopyHandler,ResourceMoveHandler, ResourceRequestedHandler, ResourceTransformHandler
{
    protected Builder bldr = new Builder();
    public Gson gson = bldr.Create();
    public Request Request;
    public Response Response;
    public Group Cluster;
    public Resource Resource;
    public Node Node;

    public AUDISK() throws InstantiationException, IllegalAccessException{
        super(null,ItemKind.None);
    }

    public AUDISK(Items aOwner, ItemKind aKind) throws InstantiationException, IllegalAccessException {
        super(aOwner,aKind);
        Response = new Response();
        Methods.registerMethod(new MoveFile(null));
    }
    @Override
    public AUDISK newInstance(Items aOwner) throws InstantiationException, IllegalAccessException{
        return new AUDISK(aOwner,ItemKind.Client);
    }
    @Override
    public AUDISK newInstance(Items aOwner, SocketChannel aChannel)throws InstantiationException, IllegalAccessException{
        AUDISK itm = new AUDISK(aOwner, ItemKind.Server);
        Server s = (Server) aOwner.Engine;
        itm.SocketHandler.Channel=aChannel;
        return itm;
    }
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
                Buffers.Recv.Move(Request.Payload, Request.Size);
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
                Request = gson.fromJson(sLine, com.aurawin.scs.rsr.protocol.audisk.def.Request.class);
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
        rsrResult r = rSuccess;
        if (Read()==rSuccess) {
            r = rSuccess;
            // check method
            Result mr = Methods.Process(Request.Method,ssn,this);
            switch (mr){
                case Ok:
                    Respond();
                    break;
                default :
                    Response.Code=Fail;
                    Respond();
                    break;

            }

            Response.Code=Ok;
            Respond();
        } else {
            r = rFailure;
            Response.Code= Fail;
            Respond();
        }
        return r;
    }

    private void Respond() {
        String sHeader=gson.toJson(Response);
        Buffers.Send.position(Buffers.Send.size());
        Buffers.Send.Write(sHeader);
        Buffers.Send.Write(Settings.RSR.Items.Header.Separator);
        if (Response.Payload.size()>0) {
            Response.Payload.Move(Buffers.Send);
        }
        queueSend();
    }

    @Override
    public Result resourceRequested(Session ssn){

        return Result.Ok;
    }
    @Override
    public CredentialResult validateCredentials(Session ssn){
        return None;
    }
    @Override
    public Result resourceUploaded(Session ssn){
        return Result.Ok;
    }
    @Override
    public Result resourceDeleted(Session ssn){
        return Result.Ok;
    }
    @Override
    public Result resourceCopied(Session ssn){
        return Result.Ok;
    }
    @Override
    public Result resourceMoved(Session ssn){
        return Result.Ok;
    }
    @Override
    public Result resourceTransform(Session ssn){
        return Result.Ok;
    }

}
