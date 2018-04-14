package com.aurawin.scs.rsr;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.ContentType;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static com.aurawin.scs.solution.Settings.Stored.ContentType.LoadDelay;

public class ContentTypes extends ConcurrentLinkedQueue<ContentType> {
    private Instant LastLoaded = null;
    public void Invalidate() {
        stream().forEach(c -> c.Verified = false);
    }

    public void Purge() {
        stream().
                filter(c -> c.Verified).
                collect(Collectors.toList()).
                stream().forEach(c -> remove(c));
    }
    public ContentType getContentType(long Id){
        return stream().filter(c -> c.getId()==Id).findFirst().orElse(null);

    }

    public ContentType getContentType(String ext) {
        return stream().filter(c -> c.getExt().equalsIgnoreCase(ext)).findFirst().orElse(null);
    }

    public String getStamp(String ext) {
        ContentType ct = getContentType(ext);
        return (ct == null) ? "" : ct.getStamp();
    }

    public void Load(){
        Invalidate();

        ArrayList<Stored> cts = Entities.Lookup(ContentType.class.getAnnotation(QueryAll.class));
        ContentType ct = null;
        ContentType cLcv = null;
        for (Stored lcv:cts){
            cLcv = (ContentType) lcv;
            ct = getContentType(cLcv.getId());
            if (ct==null){
                ct = new ContentType();
                ct.Assign(cLcv);
                add(ct);
                ct.Verified=true;
            } else {
                ct.Assign(cLcv);
            }
        }


        Purge();

        LastLoaded = Instant.now();
    }
    public boolean loadNeeded(){
        return ((LastLoaded==null) || (LastLoaded.plusMillis(LoadDelay).isAfter(Instant.now())));
    }
}
