package com.aurawin.scs.lang;


public class Table extends com.aurawin.core.lang.Table {

    public static class Security extends com.aurawin.core.lang.Table.Security{
        public static class Mechanism extends com.aurawin.core.lang.Table.Security.Mechanism{
            public static class AURADISK{
                public static final String Exclusive = "AURADISK.EXCLUSIVE";
            }
        }
        public static class Method extends com.aurawin.core.lang.Table.Security.Mechanism{
            public static class AURADISK{
                public static final String Exclusive = "EXCLUSIVE";
            }
        }
    }
    public static class Entities extends com.aurawin.core.lang.Table.Entities{
        public static class Domain{
            public static final String Root = "table.entities.domain.root";
            public static final String Default = "table.entities.domain.default";

            public static class User {
                public static class Role{
                    public static final String Administrator = "table.entities.domain.user.role.administrator";
                    public static final String PowerUser = "table.entities.domain.user.role.poweruser";
                    public static final String User = "table.entities.domain.user.role.user";
                    public static final String ContentManagement = "table.entities.domain.user.role.cms";
                    public static final String Guest = "table.entities.domain.user.role.guest";
                }
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
