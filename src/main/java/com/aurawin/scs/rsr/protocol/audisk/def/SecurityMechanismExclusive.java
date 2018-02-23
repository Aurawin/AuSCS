package com.aurawin.scs.rsr.protocol.audisk.def;

import com.aurawin.core.enryption.Base64;
import com.aurawin.scs.lang.Database;
import com.aurawin.scs.lang.Table;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.CredentialResult;
import com.aurawin.core.rsr.def.rsrResult;
import com.aurawin.core.rsr.security.Security;
import com.aurawin.core.rsr.security.fetch.Mechanism;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.domain.user.Account;
import org.hibernate.Session;

import static com.aurawin.core.rsr.def.rsrResult.rAuthenticationNotSupported;
import static com.aurawin.core.rsr.def.rsrResult.rSuccess;

public class SecurityMechanismExclusive extends Mechanism {
    private String Method;

    public SecurityMechanismExclusive() {
        super (Table.Security.Mechanism.AURADISK.Exclusive);

        Method = Table.Security.Method.AURADISK.Exclusive;

        if (!Security.hasMechanism(Table.Security.Mechanism.AURADISK.Exclusive)){
            Security.registerMechanism(this);
        }
    }

    @Override
    public rsrResult decryptCredentials(Item RSR, String... Params){
        if (Params.length==1) {
            String s = Base64.Decode(Params[0]);
            String[] sa = s.split(":");
            if (sa.length==2) {
                RSR.Credentials.Passport.Realm=RSR.Owner.Engine.Realm;
                RSR.Credentials.Passport.Username=sa[0];
                RSR.Credentials.Passport.Password=sa[1];
                return  rSuccess;
            } else {
                return rAuthenticationNotSupported;
            }
        } else {
            return rAuthenticationNotSupported;
        }
    }
    @Override
    public String buildAuthorization(String User, String Pass){
        return Table.Security.Method.AURADISK.Exclusive+ " " +
                Base64.Encode(User+":"+Pass);
    }
    @Override
    public String buildChallenge(String realm){
        return Method+ " realm=\""+realm+"\"";
    }

    @Override
    public CredentialResult DoLogin(long DomainId, long Ip, String Username, String Password) {
        return CredentialResult.NotImplemented;
    }
    @Override
    public CredentialResult DoAuthenticate(long DomainId, long Ip, String User, String Digest) {
        return CredentialResult.NotImplemented;
    }
    @Override
    public CredentialResult DoPeer(long Ip){
        CredentialResult res = CredentialResult.None;
        Session ssn = Entities.acquireSession();
        try{
            Node n = (Node)
                    ssn.getNamedQuery(Database.Query.Cloud.Node.ByIP.name)
                            .setParameter("IP",Ip)
                            .uniqueResult();
            if (n!=null){
                return CredentialResult.Passed;
            } else {
                return CredentialResult.Failed;
            }
        } finally {
            ssn.close();
        }
    }
    public void Reset(){
        Method = Table.Security.Method.AURADISK.Exclusive;
    }
    public void Release(){
        Method = null;
    }
}
