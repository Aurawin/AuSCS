package com.aurawin.scs.lang;


import org.hibernate.Session;

public class Database extends com.aurawin.core.lang.Database {
    public static class Table extends com.aurawin.core.lang.Database.Table{
        public static class Cloud{
            public static final String Group = "tbl_c_g";
            public static final String Location = "tbl_c_l";
            public static final String Node = "tbl_c_n";
            public static final String Disk = "tbl_c_d";
            public static final String Resource = "tbl_c_r";
            public static final String Transactions = "tbl_c_t";
            public static final String Uptime="tbl_c_u";
            public static final String Service="tbl_c_s";
        }
        public static class Domain{
            public static final String Items = "tbl_d_itm";
            public static final String Avatar = "tbl_d_avr";
            public static class Network{
                public static final String List = "tbl_d_ntk";
                public static final String Member = "tbl_d_ntk_m";
                public static final String Folder= "tbl_d_frs";
                public static final String File= "tbl_d_fls";
            }
            public static class UserAccount{
                public static final String Items = "tbl_d_uas";
                public static class Roster {
                    public static final String Items = "tbl_d_uas_rtr";
                    public static final String Field = "tbl_d_uas_rtf";
                }
            }
            public static class Vendor {
                public static final String Items = "tbl_d_v_itm";
                public static class Hawker {
                    public static final String Items = "tbl_d_v_h";
                    public static class Item {
                        public static final String Items = "tbl_d_v_h_i";
                        public static class Field{
                            public static final String Items = "tbl_d_v_h_i_f";
                        }
                    }

                }
            }
        }
    }
    public static class Query extends com.aurawin.core.lang.Database.Query{
        public static class Cloud{
            public static class Group{
                public static class ById{
                    public static final String name ="QueryCloudGroupById";
                    public static final String value = "from Group where Id=:Id";
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
                public static class ByName{
                    public static final String name ="QueryCloudGroupByName";
                    public static final String value = "from Group where Name=:Name";
                    
                    public static org.hibernate.query.Query Create(Session ssn, String Name){
                        return ssn.getNamedQuery(name)
                                .setParameter("Name",Name);
                    }
                }
            }
            public static class Service{
                public static class ById{
                    public static final String name ="QueryCloudServiceById";
                    public static final String value = "from Service where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
                public static class ByName{
                    public static final String name ="QueryCloudServiceByName";
                    public static final String value = "from Group where Namespace=:Namespace";
                    
                    public static org.hibernate.query.Query Create(Session ssn, String Namespace){
                        return ssn.getNamedQuery(name)
                                .setParameter("Namespace",Namespace);
                    }
                }
            }
            public static class Location{
                public static class ById{
                    public static final String name ="QueryCloudLocationById";
                    public static final String value = "from Location where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
                public static class ByName{
                    public static final String name ="QueryCloudLocationByName";
                    public static final String value = "from Location where Area like :Area or Locality like :Locality or Region like :Region";
                    
                    public static org.hibernate.query.Query Create(Session ssn, String Area, String Locality, String Region){
                        return ssn.getNamedQuery(name)
                                .setParameter("Area", "%" + Area + "%")
                                .setParameter("Locality","%"+Locality+"%")
                                .setParameter("Region","%"+Region+"%");

                    }
                }
            }
            public static class Disk{
                public static class All{
                    public static final String name ="QueryCloudDiskAll";
                    public static final String value = "from Disk";
                    public static org.hibernate.query.Query Create(Session ssn){
                        return ssn.getNamedQuery(name);
                    }
                }
                public static class ById{
                    public static final String name ="QueryCloudDiskById";
                    public static final String value = "from Disk where Id=:Id";
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
                public static class ByOwnerId{
                    public static final String name ="QueryCloudDiskByOwnerId";
                    public static final String value = "from Disk where OwnerId=:OwnerId";
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("OwnerId",Id);
                    }
                }
            }
            public static class Node{
                public static class ById{
                    public static final String name ="QueryCloudNodeById";
                    public static final String value = "from Node where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
                public static class ByName{
                    public static final String name ="QueryCloudNodeByName";
                    public static final String value = "from Node where Name=:Name";
                    
                    public static org.hibernate.query.Query Create(Session ssn, String Name){
                        return ssn.getNamedQuery(name)
                                .setParameter("Name",Name);
                    }
                }
            }
            public static class Resource{
                public static class ById{
                    public static final String name ="QueryCloudResourceById";
                    public static final String value = "from Resource where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
                public static class ByName{
                    public static final String name ="QueryCloudResourceByName";
                    public static final String value = "from Resource where Name=:Name";
                    
                    public static org.hibernate.query.Query Create(Session ssn, String Name){
                        return ssn.getNamedQuery(name)
                                .setParameter("Name", Name);
                    }
                }
            }
            public static class Transactions{
                public static class ById{
                    public static final String name ="QueryCloudTransactionsById";
                    public static final String value = "from Transactions where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
            }
            public static class Uptime{
                public static class ById{
                    public static final String name ="QueryCloudUptimeById";
                    public static final String value = "from Uptime where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("Id",Id);
                    }
                }
            }
        }
        public static class Domain{
            public static class ById {
                public static final String name = "QueryDomainById";
                public static final String value = "from Domain where Id=:Id";
                
                public static org.hibernate.query.Query Create(Session ssn, long Id){
                    return ssn.getNamedQuery(name)
                            .setParameter("Id",Id);
                }
            }
            public static class ByName {
                public static final String name = "QueryDomainByName";
                public static final String value = "from Domain where Name=:Name";
                
                public static org.hibernate.query.Query Create(Session ssn, String Name){
                    return ssn.getNamedQuery(name)
                            .setParameter("Name", Name);
                }
            }
            public static class Vendor{
                public static class ByNamespace{
                    public static final String name = "QueryDomainVendorByNamespace";
                    public static final String value = "from Vendor where DomainId=:DomainId and Namespace=:Namespace";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId, String Namespace){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId", DomainId)
                                .setParameter("Namespace", Namespace);
                    }
                }
                public static class ById {
                    public static final String name = "QueryDomainVendorById";
                    public static final String value = "from Vendor where DomainId=:DomainId and Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId,long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId",DomainId)
                                .setParameter("Id", Id);
                    }
                }
                public static class ByDomainId {
                    public static final String name = "QueryDomainVendorByDomainId";
                    public static final String value = "from Vendor where DomainId=:DomainId";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId", DomainId);
                    }
                }
                public static class Hawker {
                    public static class ByNamespace{
                        public static final String name = "QueryDomainVendorHawkerByNamespace";
                        public static final String value = "from Hawker where DomainId=:DomainId and VendorId=:VendorId and Namespace=:Namespace";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long VendorId, String Namespace){
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId)
                                    .setParameter("VendorId", VendorId)
                                    .setParameter("Namespace", Namespace);
                        }
                    }
                    public static class ById{
                        public static final String name = "QueryDomainVendorHawkerById";
                        public static final String value = "from Hawker where DomainId=:DomainId and VendorId=:VendorId and Id=:Id";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long VendorId,long Id){
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId)
                                    .setParameter("VendorId", VendorId)
                                    .setParameter("Id", Id);
                        }
                    }
                    public static class ByDomainId{
                        public static final String name = "QueryDomainVendorHawkerByDomainId";
                        public static final String value = "from Hawker where DomainId=:DomainId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId){
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId);
                        }
                    }
                    public static class Item {
                        public static class ById {
                            public static final String name = "QueryDomainVendorHawkerItemById";
                            public static final String value = "from HawkItem where DomainId=:DomainId and Id=:Id";
                            
                            public static org.hibernate.query.Query Create(Session ssn, long DomainId, long Id) {
                                return ssn.getNamedQuery(name)
                                        .setParameter("DomainId", DomainId)
                                        .setParameter("Id", Id);
                            }
                        }
                        public static class ByDomainId {
                            public static final String name = "QueryDomainVendorHawkerItemByDomainId";
                            public static final String value = "from HawkItem where DomainId=:DomainId";
                            
                            public static org.hibernate.query.Query Create(Session ssn, long DomainId) {
                                return ssn.getNamedQuery(name)
                                        .setParameter("DomainId", DomainId);
                            }
                        }
                        public static class Field {
                            public static class ById{
                                public static final String name = "QueryDomainVendorHawkerItemFieldById";
                                public static final String value = "from HawkItemField where DomainId=:DomainId and VendorId=:VendorId and OwnerId=:OwnerId and Id=:Id";
                                
                                public static org.hibernate.query.Query Create(Session ssn, long DomainId, long VendorId, long OwnerId, long Id){
                                    return ssn.getNamedQuery(name)
                                            .setParameter("DomainId", DomainId)
                                            .setParameter("VendorId", VendorId)
                                            .setParameter("OwnerId", OwnerId)
                                            .setParameter("Id", Id);
                                }
                            }
                        }
                    }

                }
            }
            public static class UserAccount{
                public static class ByName {
                    public static final String name = "QueryDomainUserAccountByName";
                    public static final String value = "from UserAccount where DomainId=:DomainId and User=:Name";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId, String Name){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId", DomainId)
                                .setParameter("Name", Name);
                    }
                }
                public static class ByAuth {
                    public static final String name = "QueryDomainUserAccountByAuth";
                    public static final String value = "from UserAccount where DomainId=:DomainId and User=:Name and Auth=:Auth";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId,String Username, String Auth){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId",DomainId)
                                .setParameter("Name",Username)
                                .setParameter("Auth", Auth);
                    }
                }
                public static class ById {
                    public static final String name = "QueryDomainUserAccountById";
                    public static final String value = "from UserAccount where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name).setParameter("Id", Id);
                    }
                }
                public static class ByDomainId {
                    public static final String name = "QueryDomainUserAccountByDomainId";
                    public static final String value = "from UserAccount where DomainId=:DomainId";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId",DomainId);
                    }
                }
                public static class ByDomainIdAndId {
                    public static final String name = "QueryDomainUserAccountByDomainIdAndId";
                    public static final String value = "from UserAccount where DomainId=:DomainId and Id=:Id";

                    public static org.hibernate.query.Query Create(Session ssn, long DomainId,long Id){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId",DomainId)
                                .setParameter("Id",Id);
                    }
                }
                public static class ByDomainIdAndName {
                    public static final String name = "QueryDomainUserAccountByDomainIdAndName";
                    public static final String value = "from UserAccount where DomainId=:DomainId and Name=:Name";
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId,String Name){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId",DomainId)
                                .setParameter("Name",Name);
                    }
                }
                public static class Roster {
                    public static class ByDomainId {
                        public static final String name = "QueryDomainUserAccountRosterByDomainId";
                        public static final String value = "from Roster where DomainId=:DomainId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId){
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId",DomainId);
                        }
                    }
                    public static class ByDomainIdAndOwnerId {
                        public static final String name = "QueryDomainUserAccountRosterByDomainIdAndOwnerId";
                        public static final String value = "from Roster where DomainId=:DomainId and OwnerId=:OwnerId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long OwnerId){

                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId",DomainId)
                                    .setParameter("OwnerId",OwnerId);
                        }
                    }
                    public static class RosterField{
                        public static class ByDomainId {
                            public static final String name = "QueryDomainUserAccountRosterFieldByDomainId";
                            public static final String value = "from RosterField where DomainId=:DomainId";
                            
                            public static org.hibernate.query.Query Create(Session ssn, long DomainId){
                                return ssn.getNamedQuery(name)
                                        .setParameter("DomainId",DomainId);
                            }
                        }
                        public static class ByOwnerId {
                            public static final String name = "QueryDomainUserAccountRosterFieldByOwnerId";
                            public static final String value = "from RosterField where OwnerId=:OwnerId";

                            public static org.hibernate.query.Query Create(Session ssn, long OwnerId){
                                return ssn.getNamedQuery(name)
                                        .setParameter("OwnerId",OwnerId);
                            }
                        }
                    }
                }
            }
            public static class Network{
                public static class ByOwner{
                    public static final String name = "QueryDomainNetworkByOwner";
                    public static final String value = "from Network where DomainId=:DomainId and OwnerId=:OwnerId";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId, long OwnerId){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId", DomainId)
                                .setParameter("OwnerId", OwnerId);
                    }
                }
                public static class ByOwnerAndTitle{
                    public static final String name = "QueryDomainNetworkByOwnerAndTitle";
                    public static final String value = "from Network where DomainId=:DomainId and OwnerId=:OwnerId and Title=:Title";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId, long OwnerId, String Title){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId", DomainId)
                                .setParameter("OwnerId", OwnerId)
                                .setParameter("Title",Title);
                    }
                }
                public static class ByDomainId {
                    public static final String name = "QueryDomainNetworkByDomainId";
                    public static final String value = "from Network where DomainId=:DomainId";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId) {
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId", DomainId);
                    }
                }
                public static class Folder {
                    public static class ById {
                        public static final String name = "QueryDomainNetworkFolderById";
                        public static final String value = "from Folder where DomainId=:DomainId and Id=:Id";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long Id) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId)
                                    .setParameter("Id", Id);
                        }
                    }
                    public static class ByDomainId {
                        public static final String name = "QueryDomainNetworkFolderByDomainId";
                        public static final String value = "from Folder where DomainId=:DomainId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId);
                        }
                    }
                    public static class ByDomainIdAndNetworkIdAndParentIdAndName{
                        public static final String name = "QueryDomainNetworkFolderByDomainIdAndNetworkIdAndParentIdAndName";
                        public static final String value = "from Folder where DomainId=:DomainId and NetworkId=:NetworkId and ParentId=:ParentId and Name=:Name";

                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long NetworkId, long ParentId, String Name) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId)
                                    .setParameter("NetworkId", NetworkId)
                                    .setParameter("ParentId", ParentId)
                                    .setParameter("Name", Name);
                        }
                    }
                    public static class ByDomainIdAndName {
                        public static final String name = "QueryDomainNetworkFolderByDomainIdAndName";
                        public static final String value = "from Folder where DomainId=:DomainId and Name=:Name";

                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, String Name) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId)
                                    .setParameter("Name", Name);
                        }
                    }
                    public static class ByNetworkId {
                        public static final String name = "QueryDomainNetworkFolderByNetworkId";
                        public static final String value = "from Folder where NetworkId=:NetworkId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long Id) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("NetworkId", Id);
                        }
                    }

                }
                public static class File {
                    public static class ByName {
                        public static final String name = "QueryDomainNetworkFileByName";
                        public static final String value = "from File where DomainId=:DomainId and NetworkId=:NetworkId and FolderId=:FolderId and Name=:Name";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long NetworkId, long FolderId, String Name) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId)
                                    .setParameter("NetworkId", NetworkId)
                                    .setParameter("FolderId", FolderId)
                                    .setParameter("Name",Name);
                        }
                    }

                    public static class ById {
                        public static final String name = "QueryDomainNetworkFileById";
                        public static final String value = "from File where DomainId=:DomainId and NetworkId=:NetworkId and Id=:Id";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId, long NetworkId, long Id) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId)
                                    .setParameter("NetworkId", NetworkId)
                                    .setParameter("Id", Id);
                        }
                    }
                    public static class ByDomainId {
                        public static final String name = "QueryDomainNetworkFileByDomainId";
                        public static final String value = "from File where DomainId=:DomainId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId);
                        }
                    }
                    public static class ByFolderId {
                        public static final String name = "QueryDomainNetworkFileByFolderId";
                        public static final String value = "from File where FolderId=:FolderId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long Id) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("FolderId", Id);
                        }
                    }
                    public static class ByNetworkId {
                        public static final String name = "QueryDomainNetworkFileByNetworkId";
                        public static final String value = "from File where NetworkId=:NetworkId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long NetworkId) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("NetworkId", NetworkId);
                        }
                    }
                }
                public static class Member{
                    public static class ByDomainId {
                        public static final String name = "QueryDomainNetworkMemberByDomainId";
                        public static final String value = "from Member where DomainId=:DomainId";
                        
                        public static org.hibernate.query.Query Create(Session ssn, long DomainId) {
                            return ssn.getNamedQuery(name)
                                    .setParameter("DomainId", DomainId);
                        }
                    }
                }
            }
            public static class Avatar{
                public static class ByOwnerAndKind{
                    public static final String name = "QueryDomainAvatarByOwnerAndKind";
                    public static final String value = "from Avatar where DomainId=:DomainId and OwnerId=:OwnerId and Kind=:Kind";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long DomainId, long OwnerId, long Kind){
                        return ssn.getNamedQuery(name)
                                .setParameter("DomainId", DomainId)
                                .setParameter("OwnerId", OwnerId)
                                .setParameter("Kind",Kind);
                    }
                }
                public static class ById{
                    public static final String name = "QueryDomainAvatarById";
                    public static final String value = "from Avatar where Id=:Id";
                    
                    public static org.hibernate.query.Query Create(Session ssn, long Id){
                        return ssn.getNamedQuery(name).setParameter("Id", Id);
                    }
                }
            }
        }
    }
    public static class Field extends com.aurawin.core.lang.Database.Field{
        public static class Cloud{
            public static class Location{
                public static final String Id = "itmid";
                public static final String Country = "itmctry";
                public static final String Region = "itmrgn";
                public static final String Locality = "itmlty";
                public static final String Area = "itma";
                public static final String Street = "itmst";
                public static final String Building = "itbg";
                public static final String Floor = "itfl";
                public static final String Room = "itrm";
                public static final String Zip = "itmz";
            }
            public static class Group{
                public static final String Id = "itmid";
                public static final String Name = "itmne";
                public static final String Row = "itmrw";
                public static final String Rack = "itmrk";
                public static final String LocationId ="itmld";
            }
            public static class Resource{
                public static final String Id = "itmid";
                public static final String GroupId = "itgid";
                public static final String Name = "itmne";
            }
            public static class Service{
                public static final String Id = "itmid";
                public static final String NodeId = "itnid";
                public static final String UniqueId = "ituid";
                public static final String Port="itmp";
                public static final String ScaleStart = "itss";
                public static final String ScaleMin = "itsm";
                public static final String ScaleMax = "itsx";
            }
            public static class Disk{
                public static final String Id = "itmid";
                public static final String OwnerId = "ioid";
                public static final String spaceTotal = "itst";
                public static final String spaceAvailable = "itsa";
                public static final String Mount = "imnt";

            }
            public static class Node{
                public static final String Id = "itmid";
                public static final String ResourceId = "ircid";
                public static final String GroupId = "igid";
                public static final String DomainId = "idid";
                public static final String TransactionsId = "itxid";
                public static final String UptimeId = "iutid";
                public static final String Name = "itmne";
                public static final String IP = "itmip";
            }
            public static class Uptime{
                public static final String Id = "itmid";
                public static final String NodeId = "itmnid";
                public static final String Stamp = "itmstp";
            }
            public static class Transactions{
                public static final String Id = "itmid";
                public static final String NodeId = "itmnid";
                public static final String Sent = "itmsnt";
                public static final String Received = "itmrcv";
                public static final String Filtered = "itfld";
                public static final String Streams = "itmst";
            }
        }
        public static class Domain{
            public static final String Id="itmid";
            public static final String RootId = "itmrid";
            public static final String CertId = "itmcid";
            public static final String Name="itnme";
            public static final String Root="itmrt";
            public static final String FriendlyName="itfme";
            public static final String DefaultOptionCatchAll="itmdca";
            public static final String DefaultOptionQuota="itmdqo";
            public static final String DefaultOptionFiltering="itmdfl";
            public static class UserAccount{
                public static final String Id="itmid";
                public static final String DomainId="itmdi";
                public static final String CabinetId="itcbi";
                public static final String AvatarId="itmad";
                public static final String DiskId="itmdd";
                public static final String RosterId="itmrid";
                public static final String Name="itmun";
                public static final String Pass="itmpswd";
                public static final String Auth="itmauth";
                public static final String LastIP="itmlip";
                public static final String FirstIP="itmfip";
                public static final String Created="itmctd";
                public static final String Modified="itmmtd";
                public static final String LastLogin="itmlln";
                public static final String LockCount="itmlct";
                public static final String LastConsumptionCalc="itmlcc";
                public static final String Consumption="itmcspn";
                public static final String Quota="itmquo";
                public static final String AllowLogin="italgn";
            }
            public static class Roster{
                public static final String Id = "itmid";
                public static final String DomainId="itmdi";
                public static final String OwnerId="itmoid";
                public static final String AvatarId  = "iaid";
                public static final String FirstName ="ifnme";
                public static final String MiddleName = "imnme";
                public static final String FamilyName = "ifmle";
                public static final String Alias = "ianme";
                public static final String Emails = "iemls";
                public static final String Phones = "iphns";
                public static final String Addresses = "iadrs";
                public static final String City = "icty";
                public static final String State = "iste";
                public static final String Postal = "izip";
                public static final String Country = "itry";
                public static final String Websites = "iwebs";
                public static final String Custom = "icst";
            }
            public static class RosterField{
                public static final String Id = "itmid";
                public static final String DomainId="itmdi";
                public static final String OwnerId="itmoid";
                public static final String Key = "itmk";
                public static final String Value = "itmv";
            }
            public static class Avatar{
                public static final String Id ="itmid";
                public static final String DomainId="itmdi";
                public static final String OwnerId="itmoid";
                public static final String Kind = "itmkd";
                public static final String Ext = "itmext";
                public static final String Created ="itctd";
                public static final String Modified = "itmtd";
                public static final String Data = "itmdat";
            }
            public static class Network{
                public static final String Id = "itmid";
                public static final String DomainId = "itmdi";
                public static final String OwnerId = "itmoid";
                public static final String DiskId = "dskid";
                public static final String FolderId = "itmfid";
                public static final String AvatarId = "itmaid";
                public static final String Members = "itmbrs";
                public static final String Exposition = "itme";
                public static final String Flags    = "itmf";
                public static final String Created = "itmctd";
                public static final String Modified = "itmmtd";
                public static final String Title = "itmtit";
                public static final String Description = "itmdsc";
                public static final String CustomFolders = "icflds";
                public static class Member{
                    public static final String Id = "itmid";
                    public static final String DomainId = "itmdi";
                    public static final String NetworkId = "itmni";
                    public static final String UserId = "itmoid";
                    public static final String Exposition = "itmexp";
                    public static final String Standing = "itmstd";
                    public static final String ACL = "itmacl";
                }
                public static class Folders{
                    public static final String Id ="itmid";
                    public static final String DomainId="itmdi";
                    public static final String OwnerId="itoid";
                    public static final String ParentId="ipid";
                    public static final String DiskId="idsid";
                    public static final String NetworkId="itmni";
                    public static final String Exposition = "itme";
                    public static final String Path = "itmp";
                    public static final String Name = "inme";
                    public static final String Created ="itctd";
                    public static final String Modified = "itmtd";
                }
                public static class Files{
                    public static final String Id = "itmid";
                    public static final String DiskId = "itdsk";
                    public static final String DomainId = "itmdi";
                    public static final String NetworkId = "itmni";
                    public static final String FolderId = "itmfi";
                    public static final String Name = "itmnm";
                    public static final String Digest = "itmde";
                    public static final String Created = "itctd";
                    public static final String Modified = "itmtd";
                    public static final String Size = "itsze";
                    public static final String Summary = "ismry";

                }
            }
            public static class Entities {
                public static class Vendor {
                    public static final String Id = "itmid";
                    public static final String DomainId = "itmdi";
                    public static final String OwnerId = "ioid";
                    public static final String NetworkId = "inid";
                    public static final String Namespace = "imsn";
                    public static final String Created = "ictd";
                    public static final String Modified = "imtd";

                    public static class Hawker {
                        public static final String Id = "itmid";
                        public static final String DomainId = "itmdi";
                        public static final String VendorId = "itmvi";
                        public static final String Namespace = "itmns";
                        public static class HawkItem {
                            public static final String Id = "itmid";
                            public static final String DomainId = "itmdi";
                            public static final String VendorId = "itmvi";
                            public static final String OwnerId  = "ioid";

                            public static class Fields {
                                public static final String Id = "itmid";
                                public static final String DomainId = "itmdi";
                                public static final String VendorId = "itmvi";
                                public static final String OwnerId = "itoid";
                                public static final String Name = "itnme";
                                public static final String DefaultLength="dlgth";
                            }
                        }
                    }



                }
            }
        }
    }
    public static class Test extends com.aurawin.core.lang.Database.Test {
        public static class Entities extends com.aurawin.core.lang.Database.Test.Entities {
            public static class Domain{
                public static final String UserAccount = "/test/storage.entities.domain.UserAccount.json";
            }
        }
    }
}