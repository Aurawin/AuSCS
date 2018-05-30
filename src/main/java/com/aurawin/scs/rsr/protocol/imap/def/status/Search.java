package com.aurawin.scs.rsr.protocol.imap.def.status;

public enum Search {

    All("ALL"),
    Answered("ANSWERED"),
    BCC("BCC"),
    Before("BEFORE"),
    Body("BODY"),
    CC("CC"),
    Deleted("DELETED"),
    Draft("DRAFT"),
    Flagged("FLAGGED"),
    From("FROM"),
    Header("HEADER"),
    Keyword("KEYWORD"),
    Larger("LARGER"),
    New("NEW"),
    Not("NOT"),
    Old("OLD"),
    On("ON"),
    OR("OR"),
    Recent("RECENT"),
    Seen("SEEN"),
    SentBefore("SENTBEFORE"),
    SentOn("SENTON"),
    SentSince("SENTSINCE"),
    Since("SINCE"),
    Smaller("SMALLER"),
    Subject("SUBJECT"),
    MessgeId("MESSAGEID"),
    Text("TEXT"),
    To("TO"),
    UID("UID"),
    UnAnswered("UNANSWERED"),
    UnDeleted("UNDELETED"),
    UnDraft("UNDRAFT"),
    UnFlagged("UNFLAGGED"),
    UnKeyword("UNKEYWORD"),
    UnSeen("UNSEEN");

    String Value;
    Search (String value){ Value = value;}
}

