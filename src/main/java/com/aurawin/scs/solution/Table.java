package com.aurawin.scs.solution;

public class Table extends com.aurawin.core.solution.Table {
    public static class Stored{
        public static class Cloud{
            public static class Service {
                public enum Kind {
                    svcNone(0,"",""),
                    svcAUDISK(Settings.Stored.Cloud.Service.Port.AuDisk, "Disk Server", "Aurawin NOSQL Disk Server"),
                    svcHTTP(Settings.Stored.Cloud.Service.Port.HTTP, "Web Sever", "Aurawin Web Server"),
                    svcHTTPS(Settings.Stored.Cloud.Service.Port.HTTPS, "Secure Web Server", "Aurawin Secure Web Server"),
                    svcIMAPS(Settings.Stored.Cloud.Service.Port.IMAPS, "Secure IMAP Server", "Aurawin Secure Imap Mail Server"),
                    svcSMTP(Settings.Stored.Cloud.Service.Port.SMTP, "Mail Server", "Aurawin Send Mail Server"),
                    svcSMTPS(Settings.Stored.Cloud.Service.Port.SMTPS, "Secure Mail Server", "Aurawin Secure Send Mail Server");

                    Kind(int Port, String Value, String Description) {
                        this.port = Port;
                        this.value = Value;
                        this.description = Description;
                    }

                    private final int port;
                    private final String value;
                    private final String description;

                    public int getPort() {
                        return port;
                    }

                    public String getValue() {
                        return value;
                    }

                    public String getDescription() {
                        return description;
                    }
                }
            }
        }
    }
}
