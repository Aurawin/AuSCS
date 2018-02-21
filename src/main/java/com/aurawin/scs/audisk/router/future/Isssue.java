package com.aurawin.scs.audisk.router.future;

import com.aurawin.scs.audisk.router.Route;

public class Isssue {

    Route Route;
    long DiskId;
    long NamespaceId;
    long DomainId;
    long OwnerId;
    long FolderId;
    long FileId;

    public Isssue() {

    }

    public Isssue(Route route, long diskId, long namespaceId, long domainId, long ownerId, long folderId) {
        Route = route;
        DiskId = diskId;
        NamespaceId = namespaceId;
        DomainId = domainId;
        OwnerId = ownerId;
        FolderId = folderId;
        FileId = 0;
    }

    public Isssue(Route route, long diskId, long namespaceId, long domainId, long ownerId, long folderId, long fileId) {
        Route = route;
        DiskId = diskId;
        NamespaceId = namespaceId;
        DomainId = domainId;
        OwnerId = ownerId;
        FolderId = folderId;
        FileId = fileId;
    }
}
