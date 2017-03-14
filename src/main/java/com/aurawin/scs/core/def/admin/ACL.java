package com.aurawin.scs.core.def.admin;

import com.google.gson.annotations.Expose;

/**
 * Created by atbrunner on 3/13/17.
 */
public class ACL {

    @Expose(serialize = true, deserialize = true)
    public long Id;

    @Expose(serialize = true, deserialize = true)
    public String Name;

    @Expose(serialize = true, deserialize = true)
    public String Title;

    @Expose(serialize = true, deserialize = true)
    public String Description;

}
