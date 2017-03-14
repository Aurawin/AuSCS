package com.aurawin.scs.stored.bootstrap.roles;

import com.aurawin.core.plugin.Plugins;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.lang.Table;

import java.util.ArrayList;

/**
 * Created by atbrunner on 3/13/17.
 */
public class Administrator{
    public static ArrayList<UniqueId> Manifest = new ArrayList<>();

    public static void Initialize(){
        String sRole = com.aurawin.scs.lang.Table.String(Table.Entities.Domain.User.Role.Administrator);
        Plugins.discoverRole(sRole,Manifest);
    }
}
