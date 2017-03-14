package com.aurawin.scs.stored.bootstrap.roles;

import com.aurawin.core.plugin.Plugins;
import com.aurawin.core.stored.entities.UniqueId;

import java.util.ArrayList;

/**
 * Created by atbrunner on 3/13/17.
 */
public class PowerUser{
    public static ArrayList<UniqueId> Manifest = new ArrayList<>();
    public static void Initialize(){
        String sRole = com.aurawin.scs.lang.Table.String(com.aurawin.scs.lang.Table.Entities.Domain.User.Role.PowerUser);
        Plugins.discoverRole(sRole,Manifest);
    }
}
