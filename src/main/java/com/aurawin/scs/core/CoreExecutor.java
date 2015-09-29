package com.aurawin.scs.core;

import com.aurawin.core.array.KeyPair;
import com.aurawin.core.lang.Table;
import com.aurawin.core.stream.MemoryStream;

public class CoreExecutor {
    public KeyPair Parameters = new KeyPair();
    public MemoryStream Input = new MemoryStream();
    public MemoryStream Output = new MemoryStream();

    public CoreExecutor(){
        Parameters.DelimiterField=": ";
        Parameters.DelimiterItem=Table.CRLF;
    }
    public void Reset(){
        Parameters.clear();
        Input.Clear();
        Output.Clear();
    }
}
