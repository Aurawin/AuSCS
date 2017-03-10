package com.aurawin.scs.audisk.router;

import com.aurawin.core.stored.Stored;
import com.aurawin.scs.rsr.protocol.audisk.client.Client;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;

import java.util.ArrayList;

public class Route {
    public boolean Valid;
    public Node Node;
    public Service Service;
    public ArrayList<Stored>Disks;
    public Client Connector;
}
