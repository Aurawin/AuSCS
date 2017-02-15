package com.aurawin.scs.core;

public enum CoreResult {
    Fail                            (  0),
    Ok                              (  1),
    Redirect                        (  2),
    Authenticate                    (  3),
    Reset                           (  4),
    Timeout                         (  5),
    NotFound                        (  6),
    BuffersExceeded                 (  7),
    Deferred                        (  8),

    Error                           (100),
    Exception                       (101),
    CoreObjectNotFound              (102),
    CoreObjectFolderNotFound        (103),
    CoreObjectResourceNotFound      (104),
    CoreObjectAccessDenied          (105),
    CoreObjectBeforeExecute         (106),
    CoreObjectNoDeviceId            (107),
    CoreObjectProxyDown             (108),
    CoreObjectReserved1             (109),
    CoreObjectReserved2             (110),
    CoreObjectReserved3             (111),

    CoreCommandNotFound             (200),
    CoreCommandInitialization       (201),
    CoreCommandFolderNotFound       (202),
    CoreCommandResourceNotFound     (203),
    CoreCommandAuthRequired         (204),
    CoreCommandRedirect             (205),
    CoreCommandDMSFailure           (206),
    CoreCommandNotUnderstood        (207),
    CoreCommandNotImplemented       (208),
    CoreCommandNotInitialized       (209),
    CoreCommandAccessDenied         (210),
    CoreCommandMissingFields        (211),
    CoreCommandInvalidSearch        (212),
    CoreCommandInvalidXML           (213),
    CoreCommandDuplicate            (214),
    CoreCommandMissingParameter     (215),
    CoreCommandMediaNotFound        (216),
    CoreCommandChannelNotFound      (217),
    CoreCommandInvalidExecution     (218),
    CoreCommandInvalidProperty      (219),
    CoreCommandInvalidMedia         (220),
    CoreCommandInvalidParameter     (221),
    CoreCommandDiskMissing          (222),
    CoreCommandDiskDataMissing      (223),
    CoreCommandRequiresSecurity     (224),
    CoreCommandNetworkReadFailed    (225),
    CoreCommandInvalidJSON          (226);

    CoreResult(int value){
        this.value = value;
    }
    private final int value;
    public int getValue(){return value;}

}
