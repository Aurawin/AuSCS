package com.aurawin.scs.lang;


public class Table extends com.aurawin.core.lang.Table {
    public static class AuDisk{
        public static final String Protocol = "Version";
        public static class Method{
            public static class Command{
                public static final String MoveFile = "CMFE";
                public static final String MakeFolder = "CMFR";
            }
            public static final String MoveFile = "MMFE";
            public static final String MakeFolder = "MMFR";
        }
    }
    public static class Entities extends com.aurawin.core.lang.Table.Entities{
        public static class Domain{
            public static final String Root = "table.entities.domain.root";
            public static class User {
                public static class Roster {
                    public static final String Me = "table.entities.domain.roster.me";
                }
                public static class Network{
                    public static class Default {
                        public static final String Title = "table.entities.domain.network.title";
                        public static final String Description = "table.entities.domain.network.description";
                    }
                }
            }
        }
    }
    public static class Exception extends com.aurawin.core.lang.Table.Exception {
        public static class Entities extends com.aurawin.core.lang.Table.Exception {
            public static class Domain {
                public static String UnableToCreateExists = "table.exception.entities.domain.unable-to-create-domain-exists";

                public static class Network {
                }

                public static class Folder {
                    public static String UnableToCreateExists = "table.exception.entities.domain.folder.unable-to-create-exists";
                }

                public static class UserAccount {
                    public static String UnableToCreateExists = "table.exception.entities.domain.useraccount.unable-to-create-user-exists";

                }

                public static class Avatar {
                    public static final String UnableToCreateExists = "table.exception.entities.domain.avatar.unable-to-create-avatar-exists";
                }
            }

        }

    }
}
