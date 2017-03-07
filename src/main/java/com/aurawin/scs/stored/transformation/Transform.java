package com.aurawin.scs.stored.transformation;


import com.aurawin.core.stream.MemoryStream;

public interface Transform {
    public Result Process(MemoryStream Original, MemoryStream Output);
}
