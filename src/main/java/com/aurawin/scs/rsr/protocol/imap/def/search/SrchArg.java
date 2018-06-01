package com.aurawin.scs.rsr.protocol.imap.def.search;

public enum SrchArg {
    srchArgUnknown,
    srchArgAll,
    srchArgAnswered,     // 1
    srchArgBcc,          // 2
    srchArgBefore,       // 2
    srchArgBody,         // 2
    srchArgCC,           // 2
    srchArgDeleted,      // 1
    srchArgDraft,        // 1
    srchArgFlagged,      // 1
    srchArgFrom,         // 2
    srchArgHeader,       // 3
    srchArgKeyword,
    srchArgLarger,
    srchArgNew,
    srchArgNot,
    srchArgOld,
    srchArgOn,
    srchArgOr,
    srchArgRecent,
    srchArgSeen,
    srchArgSentBefore,
    srchArgSentOn,
    srchArgSentSince,
    srchArgSince,
    srchArgSmaller,
    srchArgSubject,
    srchArgMessageID,
    srchArgText,
    srchArgTo,
    srchArgUID,
    srchArgUnAnwered,
    srchArgUnDeleted,
    srchArgUnDraft,
    srchArgUnFlagged,
    srchArgUnKeyword,
    srchArgUnseen
}
