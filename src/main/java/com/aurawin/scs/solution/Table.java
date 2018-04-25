package com.aurawin.scs.solution;

public class Table extends com.aurawin.core.solution.Table {
    public static class Stored{
        public static class Cloud{
            public enum Service{
                svcHTTP("Web Sever", "Aurawin Web Server"),
                svcHTTPS("Secure Web Server", "Aurawin Secure Web Server");

                Service(String Value, String Description){
                    this.value = Value;
                    this.description= Description;
                }
                private final String value;
                private final String description;
                public String getValue(){return value;}
                public String getDescription(){ return description;}
            }
        }
    }
}
