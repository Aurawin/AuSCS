package com.aurawin.scs.solution;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class Settings extends com.aurawin.core.solution.Settings {
    public static class Security extends com.aurawin.core.solution.Settings.Security{
        public static class IpLog{
            public static long RecentItemsTimeWindowInSeconds=60*2; // 2 minutes
        }
    }
    public static class RSR extends com.aurawin.core.solution.Settings.RSR{
        public static class Items extends com.aurawin.core.solution.Settings.RSR.Items{
            public static class IMAP {
                public static class Tags{
                    public static boolean On = true;
                    public static boolean Off = false;
                }


                public static class Authenticate {
                    public static String Name = "Authenticate";
                    public static String Plain = "Plain";
                    public static String Login = "Login";
                    public static String DigestMD5 = "DIGEST-MD5";
                }
                public static class Search {
                    public static String Flags          = "flags";
                    public static String FlagsSilent    = "flags.silent";
                    public static String FlagsSilentOn  = "+flags.silent";
                    public static String FlagsSilentOff = "-flags.silent";
                    public static String FlagsOn        = "+flags";
                    public static String FlagsOff       = "-flags";
                    public static long MaxBytes = 1024;
                }
            }
        }
    }
    public static class AuDisk{

        public static final String Protocol = "AUDISK";
        public static class Method{
            public static class Command{
                public static final String Move = "MVE";
                public static final String List = "LST";
                public static final String Make = "MKE";
                public static final String Delete = "DEL";
                public static final String Read = "REA";
                public static final String PartialRead = "PRD";
                public static final String Write = "WRI";
                public static final String PartialWrite = "PWR";
            }
            public static final String File = "FLE";
            public static final String Folder = "FLDR";
        }
        public static class Router{
            public static final int ConnectionYield = 300;
            public static final int ScanTimerFastYield = 1000*60*1;
            public static final int ScanTimerYield = 1000*60*4;
            public static final int ObtainRouteYield = 250;
            public static final int EntitiesLoadingDelay = 1800;
        }
    }
    public static class Stored{
        public static class ContentType{
            public static final int LoadDelay = 1800;
        }
        public static class Cloud{
            public static class Service{
                public static class Port{
                  public static final int AuDisk = 7979;
                  public static final int HTTP = 80;
                  public static final int HTTPS = 443;
                  public static final int POP3 = 110;
                  public static final int POP3S = 995;
                  public static final int SMTP = 25;
                  public static final int SMTPS = 465;
                  public static final int IMAP = 143;
                  public static final int IMAPS = 993;
                }
            }
            public static class Disk{
                public static final String Root = "AuraDisk";
                public static final Set<PosixFilePermission> Permissions = PosixFilePermissions.fromString("rwxrwx---");
                public static final FileAttribute<Set<PosixFilePermission>> Attributes = PosixFilePermissions.asFileAttribute(Permissions);
            }
        }
        public static class Domain{
            public static class Network {
                public static class File {
                    public static final String fmtRouteFull  ="%2$s%1$s%3$s%1$s%4$s%1$s%5$s%1$s%6$s%1$s%7$s%1$s%8$s";
                    public static final String fmtRoutePath  ="%2$s%1$s%3$s%1$s%4$s%1$s%5$s%1$s%6$s%1$s%7$s";
                    public static final String fmtRouteMount ="%2$s%1$s%3$s";

                    public static Path buildMount(String Mount) {
                        return Paths.get(
                                String.format(fmtRouteMount, java.io.File.separator, Mount, Cloud.Disk.Root)
                        );
                    }

                    public static Path buildPath(String Mount,long namespaceId, long domainId,
                                                 long ownerId, long folderId)
                    {
                        return Paths.get(
                                String.format(fmtRoutePath,
                                    java.io.File.separator,
                                    Mount,
                                    Stored.Cloud.Disk.Root,
                                    String.valueOf(namespaceId),
                                    String.valueOf(domainId),
                                    String.valueOf(ownerId),
                                    String.valueOf(folderId)
                                )
                        );
                    }
                    public static Path buildFilename(String Mount,long namespaceId,long domainId,
                                                     long ownerId, long folderId, long fileId)
                    {
                        return Paths.get(
                                String.format(fmtRouteFull,
                                    java.io.File.separator,
                                    Mount,
                                    Stored.Cloud.Disk.Root,
                                    String.valueOf(namespaceId),
                                    String.valueOf(domainId),
                                    String.valueOf(ownerId),
                                    String.valueOf(folderId),
                                    String.valueOf(fileId)
                                )
                        );
                    }

                }
            }
        }


    }

}
