package com.aurawin.scs.core.transformation;


import com.aurawin.core.stream.MemoryStream;

public interface Transform {
    public Result Process(MemoryStream Original, MemoryStream Output);
}
