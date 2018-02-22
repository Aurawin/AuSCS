package com.aurawin.scs.stored.bootstrap;

import com.aurawin.scs.stored.bootstrap.roles.Administrator;
import com.aurawin.scs.stored.bootstrap.roles.PowerUser;
import com.aurawin.scs.stored.bootstrap.roles.User;

/**
 * Created by atbrunner on 3/22/17.
 */
public class Roles {
    public static void Initialize(){
        Administrator.Initialize();
        PowerUser.Initialize();
        User.Initialize();
    }
}
