package com.aurawin.scs.stored.domain.network;

import static com.aurawin.scs.stored.domain.network.Permission.*;
import static com.aurawin.scs.stored.domain.network.Permission.Write;
public enum Standing {
    Anonymous ((byte)0      , None                                      ),
    Administrator ((byte)1  , All                                       ),
    Affiliate ((byte)2      , List | Read | Write | Create              ),
    Assistant ((byte)3      , List | Read | Write | Create | Delete     ),
    Friend ((byte)4         , List | Read | Write | Create              ),
    Family ((byte)5         , List | Read | Write | Create | Delete     ),
    Acquaintance ((byte)6   , List | Read | Write | Create              ),
    Cohort ((byte)7         , List | Read | Write | Create | Delete     );



    public byte Level =0;
    public long Permission;

    Standing(byte level, long permission){
        this.Level=level;
        this.Permission=permission;
    }
}
