package com.aurawin.scs.lang;


public class Database extends com.aurawin.core.lang.Database {
    public static class Test extends com.aurawin.core.lang.Database.Test{
        public static class Entities{
            public static class Domain {
                public static final String UserAccount = "storage.entities.domain.UserAccount.json";
            }
        }
    }
    public static class Table extends com.aurawin.core.lang.Database.Table{
        public static final String ContentType = "tbl_cnt";
        public static final String DNS = "tbl_dns";
        public static class Security{
            public static final String ACL = "tbl_sec_a";
            public static final String Intrusion = "tbl_sec_n";
            public static final String IpLog = "tbl_sec_i";
            public static final String Filter = "tbl_sec_f";
        }
        public static class Cloud{
            public static final String Cluster="tbl_c_c";
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
            public static final String KeyValue = "tbl_d_kv";
            public static class Network{
                public static final String List = "tbl_d_ntk";
                public static final String Member = "tbl_d_ntk_m";
                public static final String Folder= "tbl_d_frs";
                public static final String File= "tbl_d_fls";
            }
            public static class User {
                public static final String Avatar = "tbl_d_avr";
                public static final String Role = "tbl_d_rle";
                public static final String RoleMap = "tbl_d_rmp";
                public static class Account {
                    public static final String Items = "tbl_d_uas";
                    public static class Roster {
                        public static final String Items = "tbl_d_uas_rtr";
                        public static final String Field = "tbl_d_uas_rtf";
                    }
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
        public static class ContentType {
            public static class All {
                public static final String name = "QueryContentTypeAll";
                public static final String value = "from ContentType";
            }

            public static class ById {
                public static final String name = "QueryContentTypeById";
                public static final String value = "from ContentType where Id=:Id";
            }
        }
        public static class DNS{
            public static class All {
                public static final String name = "QueryDNSAll";
                public static final String value = "from DNS";
            }

            public static class ById {
                public static final String name = "DNSById";
                public static final String value = "from DNS where Id=:Id";
            }
            public static class ByIp {
                public static final String name = "DNSByIp";
                public static final String value = "from DNS where Host=:Ip";
            }

        }
        public static class Security{
            public static class ACL{
                public static class ById{
                    public static final String name ="QuerySecurityACLById";
                    public static final String value = "from ACL where Id=:Id";
                }
                public static class ByNamespaceIdAndOwnerId{
                    public static final String name ="QuerySecurityACLByNamespaceIdAndOwnerId";
                    public static final String value = "from ACL where NamespaceId=:NamespaceId and OwnerId=:OwnerId";
                }
                public static class ListAllByOwnerId{
                    public static final String name ="QuerySecurityACLListAllByOwnerId";
                    public static final String value = "from ACL where OwnerId=:OwnerId";
                }
            }
            public static class Filter{
                public static class ListAll{
                    public static final String name ="QuerySecurityFilterListAll";
                    public static final String value = "from Filter where Namespace.Id=:NamespaceId";
                }
                public static class ById{
                    public static final String name ="QuerySecurityFilterById";
                    public static final String value = "from Filter where Id=:Id";
                }
                public static class Increment{
                    public static final String name ="QuerySecurityFilterIncrement";
                    public static final String value = "update Filter set Counter = Counter +1 where Id=:Id";
                }
            }
            public static class IpLog{
                public static class ById{
                    public static final String name ="QuerySecurityIpLogCountById";
                    public static final String value = "select count(*) from IpLog where Id=:Id";
                }
                public static class CountLastEntriesByIpAndTime{
                    public static final String name ="QuerySecurityIpLogCountLastEntriesByIpAndTime";
                    public static final String value = "from IpLog where Ip=:Id and Time>=:Time";
                }
            }
            public static class Intrusion{
                public static class ById{
                    public static final String name ="QuerySecurityIntrusionById";
                    public static final String value = "from Intrusion where Id=:Id";
                }
            }
        }
        public static class Cloud{
            public static class Group{
                public static class All{
                    public static final String name ="QueryCloudGroupAll";
                    public static final String value = "from Group";
                }
                public static class ById{
                    public static final String name ="QueryCloudGroupById";
                    public static final String value = "from Group where Id=:Id";
                }
                public static class ByName{
                    public static final String name ="QueryCloudGroupByName";
                    public static final String value = "from Group where Name=:Name";
                }
            }
            public static class Service{
                public static class ById{
                    public static final String name ="QueryCloudServiceById";
                    public static final String value = "from Service where Id=:Id";
                }
                public static class ByOwnerId{
                    public static final String name ="QueryCloudServiceByOwnerId";
                    public static final String value = "from Service where Owner.Id=:OwnerId order by Id";
                }
                public static class ByOwnerIdAndKind{
                    public static final String name ="QueryCloudServiceByOwnerIdAndKind";
                    public static final String value = "from Service where Owner.Id=:OwnerId and Kind=:Kind";
                }
                public static class ByName{
                    public static final String name ="QueryCloudServiceByName";
                    public static final String value = "from Group where Namespace=:Namespace";
                }
            }
            public static class Location{
                public static class ById{
                    public static final String name ="QueryCloudLocationById";
                    public static final String value = "from Location where Id=:Id";
                }
                public static class ByName{
                    public static final String name ="QueryCloudLocationByName";
                    public static final String value = "from Location where Area like :Area or Locality like :Locality or Region like :Region";
                }
            }
            public static class Disk{
                public static class All{
                    public static final String name ="QueryCloudDiskAll";
                    public static final String value = "from Disk";
                }
                public static class ById{
                    public static final String name ="QueryCloudDiskById";
                    public static final String value = "from Disk where Id=:Id";
                }
                public static class ByOwnerId{
                    public static final String name ="QueryCloudDiskByOwnerId";
                    public static final String value = "from Disk where OwnerId=:OwnerId";
                }
                public static class ByServiceId{
                    public static final String name ="QueryCloudDiskByServiceId";
                    public static final String value = "from Disk where ServiceId=:ServiceId";
                }
            }
            public static class Node{
                public static class ById{
                    public static final String name ="QueryCloudNodeById";
                    public static final String value = "from Node where Id=:Id";
                }
                public static class ByName{
                    public static final String name ="QueryCloudNodeByName";
                    public static final String value = "from Node where Name=:Name";
                }
                public static class ByOwnerId{
                    public static final String name ="QueryCloudNodeByOwnerId";
                    public static final String value = "from Node where Resource.Id=:OwnerId";
                }
                public static class ByIP{
                    public static final String name ="QueryCloudNodeByIP";
                    public static final String value = "from Node where IP=:Ip";
                }
            }
            public static class Resource{
                public static class ById{
                    public static final String name ="QueryCloudResourceById";
                    public static final String value = "from Resource where Id=:Id";
                }
                public static class ByOwnerId{
                    public static final String name ="QueryCloudResourceByOwnerId";
                    public static final String value = "from Resource where Group.Id=:OwnerId";
                }
                public static class ByName{
                    public static final String name ="QueryCloudResourceByName";
                    public static final String value = "from Resource where Name=:Name";
                }
            }
            public static class Transactions{
                public static class ById{
                    public static final String name ="QueryCloudTransactionsById";
                    public static final String value = "from Transactions where Id=:Id";
                }
                public static class DeleteByNodeId{
                    public static final String name ="QueryCloudTransactionsDeleteByNodeId";
                    public static final String value = "from Transactions where NodeId=:Id";
                }
            }
            public static class Uptime{
                public static class ById{
                    public static final String name ="QueryCloudUptimeById";
                    public static final String value = "from Uptime where Id=:Id";
                }
            }
        }
        public static class Domain{
            public static class KeyValue {
                public static class ByDomainIdAndNamespaceId {
                    public static final String name = "QueryDomainKeyValueByDomainIdAndNamespaceId";
                    public static final String value = "from KeyValue where DomainId=:DomainId and NamespaceId=:NamespaceId";
                }
                public static class DeleteDomainKeyValueByDomainIdAndNamespaceId {
                    public static final String name = "QueryDeleteDomainKeyValueByDomainId";
                    public static final String value = "delete from KeyValue where DomainId=:DomainId";
                }
            }

            public static class All{
                public static final String name = "QueryDomainAll";
                public static final String value = "from Domain";
            }
            public static class ById {
                public static final String name = "QueryDomainById";
                public static final String value = "from Domain where Id=:Id";
            }
            public static class ByName {
                public static final String name = "QueryDomainByName";
                public static final String value = "from Domain where Name=:Name";
            }
            public static class Vendor{
                public static class ByNamespace{
                    public static final String name = "QueryDomainVendorByNamespace";
                    public static final String value = "from Vendor where DomainId=:DomainId and Namespace=:Namespace";
                }
                public static class ById {
                    public static final String name = "QueryDomainVendorById";
                    public static final String value = "from Vendor where DomainId=:DomainId and Id=:Id";
                }
                public static class ByDomainId {
                    public static final String name = "QueryDomainVendorByDomainId";
                    public static final String value = "from Vendor where DomainId=:DomainId";
                }
                public static class Hawker {
                    public static class ByNamespace{
                        public static final String name = "QueryDomainVendorHawkerByNamespace";
                        public static final String value = "from Hawker where DomainId=:DomainId and VendorId=:VendorId and Namespace=:Namespace";
                    }
                    public static class ById{
                        public static final String name = "QueryDomainVendorHawkerById";
                        public static final String value = "from Hawker where DomainId=:DomainId and VendorId=:VendorId and Id=:Id";
                    }
                    public static class ByDomainId{
                        public static final String name = "QueryDomainVendorHawkerByDomainId";
                        public static final String value = "from Hawker where DomainId=:DomainId";
                    }
                    public static class Item {
                        public static class ById {
                            public static final String name = "QueryDomainVendorHawkerItemById";
                            public static final String value = "from HawkItem where DomainId=:DomainId and Id=:Id";
                        }
                        public static class ByDomainId {
                            public static final String name = "QueryDomainVendorHawkerItemByDomainId";
                            public static final String value = "from HawkItem where DomainId=:DomainId";
                        }
                        public static class Field {
                            public static class ById{
                                public static final String name = "QueryDomainVendorHawkerItemFieldById";
                                public static final String value = "from HawkItemField where DomainId=:DomainId and VendorId=:VendorId and OwnerId=:OwnerId and Id=:Id";
                            }
                        }
                    }

                }
            }
            public static class User{
                public static class Account {
                    public static class Delete{
                        public static class ById {
                            public static final String name = "QueryDomainUserAccountDeleteByName";
                            public static final String value = "delete from Account where DomainId=:DomainId and Id=:Id";
                        }
                    }
                    public static class ByName {
                        public static final String name = "QueryDomainUserAccountByName";
                        public static final String value = "from Account where DomainId=:DomainId and Name=:Name";
                    }

                    public static class ByAuth {
                        public static final String name = "QueryDomainUserAccountByAuth";
                        public static final String value = "from Account where DomainId=:DomainId and Name=:Name and Auth=:Auth";
                    }

                    public static class ById {
                        public static final String name = "QueryDomainUserAccountById";
                        public static final String value = "from Account where Id=:Id";
                    }
                    public static class ByDomainId {
                        public static final String name = "QueryDomainUserAccountByDomainId";
                        public static final String value = "from Account where DomainId=:DomainId";
                    }

                    public static class All {
                        public static final String name = "QueryDomainUserAccountAll";
                        public static final String value = "from Account where DomainId=:DomainId";
                    }
                    public static class ByDomainIdAndId {
                        public static final String name = "QueryDomainUserAccountByDomainIdAndId";
                        public static final String value = "from Account where DomainId=:DomainId and Id=:Id";
                    }

                    public static class ByDomainIdAndName {
                        public static final String name = "QueryDomainUserAccountByDomainIdAndName";
                        public static final String value = "from Account where DomainId=:DomainId and Name=:Name";
                    }
                    public static class ByDomainIdAndNameAndPass {
                        public static final String name = "QueryDomainUserAccountByDomainIdAndNameAndPass";
                        public static final String value = "from Account where DomainId=:DomainId and Name=:Name and Password=:Password";
                    }
                    public static class ByDomainIdAndNameAndAuth {
                        public static final String name = "QueryDomainUserAccountByDomainIdAndNameAndDigest";
                        public static final String value = "from Account where DomainId=:DomainId and Name=:Name and Auth=:Auth";
                    }
                }
                public static class Avatar{
                    public static class ByOwnerAndKind{
                        public static final String name = "QueryDomainUserAvatarByOwnerAndKind";
                        public static final String value = "from Avatar where DomainId=:DomainId and OwnerId=:OwnerId and Kind=:Kind";
                    }
                    public static class ById{
                        public static final String name = "QueryDomainUserAvatarById";
                        public static final String value = "from Avatar where Id=:Id";
                    }
                }
                public static class Role{
                    public static class ByOwnerId{
                        public static final String name = "QueryDomainUserRoleByOwnerId";
                        public static final String value = "from Role where OwnerId=:OwnerId";
                    }
                    public static class ById{
                        public static final String name = "QueryDomainUserRoleById";
                        public static final String value = "from Role where Id=:Id";
                    }
                    public static class ByName{
                        public static final String name = "QueryDomainUserRoleByName";
                        public static final String value = "from Role where Name=:Name";
                    }
                }
                public static class RoleMap{
                    public static class ByOwnerId{
                        public static final String name = "QueryDomainUserRoleMapByOwnerId";
                        public static final String value = "from RoleMap where OwnerId=:OwnerId";
                    }
                    public static class ById{
                        public static final String name = "QueryDomainUserRoleMapById";
                        public static final String value = "from RoleMap where Id=:Id";
                    }
                }
                public static class Roster {
                    public static class ByDomainId {
                        public static final String name = "QueryDomainUserRosterByDomainId";
                        public static final String value = "from Roster where DomainId=:DomainId";
                    }

                    public static class ByDomainIdAndOwnerId {
                        public static final String name = "QueryDomainUserRosterByDomainIdAndOwnerId";
                        public static final String value = "from Roster where DomainId=:DomainId and OwnerId=:OwnerId";
                    }

                    public static class RosterField {
                        public static class ByDomainId {
                            public static final String name = "QueryDomainUserRosterFieldByDomainId";
                            public static final String value = "from RosterField where DomainId=:DomainId";
                        }

                        public static class ByOwnerId {
                            public static final String name = "QueryDomainUserRosterFieldByOwnerId";
                            public static final String value = "from RosterField where OwnerId=:OwnerId";
                        }
                    }
                }
            }
            public static class Network{
                public static class ByOwner{
                    public static final String name = "QueryDomainNetworkByOwner";
                    public static final String value = "from Network where DomainId=:DomainId and OwnerId=:OwnerId";
                }
                public static class ByOwnerAndTitle{
                    public static final String name = "QueryDomainNetworkByOwnerAndTitle";
                    public static final String value = "from Network where DomainId=:DomainId and OwnerId=:OwnerId and Title=:Title";
                }
                public static class ByDomainId {
                    public static final String name = "QueryDomainNetworkByDomainId";
                    public static final String value = "from Network where DomainId=:DomainId";
                }
                public static class Folder {
                    public static class ById {
                        public static final String name = "QueryDomainNetworkFolderById";
                        public static final String value = "from Folder where DomainId=:DomainId and Id=:Id";
                    }
                    public static class ByParentId {
                        public static final String name = "QueryDomainNetworkFolderByParentId";
                        public static final String value = "from Folder where ParentId=:Id";
                    }
                    public static class ByDomainId {
                        public static final String name = "QueryDomainNetworkFolderByDomainId";
                        public static final String value = "from Folder where DomainId=:DomainId";
                    }
                    public static class ByDomainIdAndNetworkIdAndParentIdAndName{
                        public static final String name = "QueryDomainNetworkFolderByDomainIdAndNetworkIdAndParentIdAndName";
                        public static final String value = "from Folder where DomainId=:DomainId and NetworkId=:NetworkId and ParentId=:ParentId and Name=:Name";
                    }
                    public static class ByDomainIdAndName {
                        public static final String name = "QueryDomainNetworkFolderByDomainIdAndName";
                        public static final String value = "from Folder where DomainId=:DomainId and Name=:Name";
                    }
                    public static class ByNetworkId {
                        public static final String name = "QueryDomainNetworkFolderByNetworkId";
                        public static final String value = "from Folder where NetworkId=:NetworkId";
                    }

                }
                public static class File {
                    public static class ByName {
                        public static final String name = "QueryDomainNetworkFileByName";
                        public static final String value = "from File where DomainId=:DomainId and NetworkId=:NetworkId and FolderId=:FolderId and Name=:Name";
                    }

                    public static class ById {
                        public static final String name = "QueryDomainNetworkFileById";
                        public static final String value = "from File where DomainId=:DomainId and NetworkId=:NetworkId and Id=:Id";
                    }
                    public static class ByParentId {
                        public static final String name = "QueryDomainNetworkFileByParentId";
                        public static final String value = "from File where FolderId=:Id";
                    }
                    public static class ByDomainId {
                        public static final String name = "QueryDomainNetworkFileByDomainId";
                        public static final String value = "from File where DomainId=:DomainId";
                    }
                    public static class ByFolderId {
                        public static final String name = "QueryDomainNetworkFileByFolderId";
                        public static final String value = "from File where FolderId=:FolderId";
                    }
                    public static class ByNetworkId {
                        public static final String name = "QueryDomainNetworkFileByNetworkId";
                        public static final String value = "from File where NetworkId=:NetworkId";
                    }
                    public static class ConsumptionByOwnerId{
                        public static final String name = "QueryDomainNetworkFileConsumptionByOwnerId";
                        public static final String value = "select count(*) from File where OwnerId=:OwnerId";
                    }

                }
                public static class Member{
                    public static class ByDomainId {
                        public static final String name = "QueryDomainNetworkMemberByDomainId";
                        public static final String value = "from Member where DomainId=:DomainId";
                    }
                }
            }

        }
    }
    public static class Field extends com.aurawin.core.lang.Database.Field{
        public static class ContentType{
            public static final String Id = "itmid";
            public static final String Major = "itmm";
            public static final String Minor = "itmn";
            public static final String Ext = "itmet";
        }
        public static class DNS{
            public static final String Id = "itmid";
            public static final String Host = "ithst";
        }
        public static class Security{
            public static class ACL{
                public static final String Id = "itmid";
                public static final String OwnerId = "ioid";
                public static final String NamespaceId = "nsid";
            }
            public static class Filter {
                public static final String Id = "itmid";
                public static final String Counter = "ictr";
                public static final String Expires = "iexp";
                public static final String Enabled = "ield";
                public static final String NamespaceId = "nsid";
                public static final String Value = "itvl";
                public static final String Data = "idat";
            }

            public static class IpLog{
                public static final String Id = "itmid";
                public static final String Ip = "itmip";
                public static final String Time = "itme";
            }
            public static class Intrusion{
                public static final String Id = "itmid";
                public static final String DomainId = "itdid";
                public static final String User = "itun";
                public static final String Password = "ipwd";
                public static final String Ip = "itmip";
                public static final String Time = "itme";
            }
        }
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
                public static final String Description = "itmdn";
                public static final String Row = "itmrw";
                public static final String Rack = "itmrk";
                public static final String LocationId ="itmld";
            }
            public static class Resource{
                public static final String Id = "itmid";
                public static final String OwnerId = "itoid";
                public static final String Name = "itmne";
            }
            public static class Service{
                public static final String Id = "itmid";
                public static final String OwnerId = "itnid";
                public static final String UniqueId = "ituid";
                public static final String Enabled = "itme";
                public static final String Port="itmp";
                public static final String Service="itmsv";
                public static final String MountPoint="itmmp";
                public static final String ScaleStart = "itss";
                public static final String ScaleMin = "itsm";
                public static final String ScaleMax = "itsx";
            }
            public static class Disk{
                public static final String Id = "itmid";
                public static final String OwnerId = "ioid";
                public static final String ServiceId = "isid";
                public static final String NamespaceId = "insd";
                public static final String spaceTotal = "itst";
                public static final String spaceAvailable = "itsa";
                public static final String Mount = "imnt";

            }
            public static class Node{
                public static final String Id = "itmid";
                public static final String OwnerId = "ioid";
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
            public static final String RootName="itmrn";

            public static final String Organization="iogn";
            public static final String FriendlyName="itfme";
            public static final String DefaultOptionCatchAll="itmdca";
            public static final String DefaultOptionQuota="itmdqo";
            public static final String DefaultOptionFiltering="itmdfl";

            public static class Collection{
                public static final String Id = "itmid";
                public static final String DomainId = "itdid";
                public static final String NamespaceId = "itmnsid";
                public static final String Name = "itmnme";
                public static final String Value = "itmvl";
            }
            public static class User {
                public static class Role{
                    public static final String Id = "itmid";
                    public static final String Name = "inme";
                    public static final String Description = "itle";
                }
                public static class RoleMap{
                    public static final String Id = "itmid";
                    public static final String OwnerId = "ioid";
                    public static final String NamespaceId = "nsid";
                }
                public static class Account {
                    public static final String Id = "itmid";
                    public static final String DomainId = "itmdi";
                    public static final String CabinetId = "itcbi";
                    public static final String AvatarId = "itmad";
                    public static final String DiskId = "itmdd";
                    public static final String RosterId = "itmrid";
                    public static final String Name = "itmun";
                    public static final String Pass = "itmpswd";
                    public static final String Auth = "itmauth";
                    public static final String LastIP = "itmlip";
                    public static final String FirstIP = "itmfip";
                    public static final String Created = "itmctd";
                    public static final String Modified = "itmmtd";
                    public static final String LastLogin = "itmlln";
                    public static final String LockCount = "itmlc";
                    public static final String LastConsumptionCalc = "itmlcc";
                    public static final String Consumption = "itmcspn";
                    public static final String Quota = "itmquo";
                    public static final String Roles = "itmrls";
                    public static final String AllowLogin = "italgn";
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
                    public static final String OwnerId = "itoid";
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

}
