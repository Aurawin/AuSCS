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
    public static class Stored{
        public static class Cloud{
            public static class Disk{
                public static final String Root = "AuraDisk";
                public static final Set<PosixFilePermission> Permissions = PosixFilePermissions.fromString("rwxrwx---");
                public static final FileAttribute<Set<PosixFilePermission>> Attributes = PosixFilePermissions.asFileAttribute(Permissions);
            }
        }
        public static class Domain{
            public static class Network {
                public static class File {
                    public static final String fmtRouteFull  ="%2$s%1$s%3$s%1$s%4$s%1$s%5$s%1s$%6$s%1$s%7$s%1$s%8$s";
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
