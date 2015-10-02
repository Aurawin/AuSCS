package com.aurawin.core.stored.entities.vendor.hawker.manifest;

import com.aurawin.core.Memo;
import com.aurawin.core.array.KeyItem;
import com.aurawin.core.array.KeyPair;
import com.aurawin.core.array.VarString;
import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.entities.Entities;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;

@Entity
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@NamedQueries(
        {
                @NamedQuery(
                        name  = Database.Query.Domain.Vendor.Hawker.Manifest.Field.ById.name,
                        query = Database.Query.Domain.Vendor.Hawker.Manifest.Field.ById.value
                )
        }
)
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name=Database.Table.Domain.Vendor.Hawker.Manifest.Field.Items)
@QueryById(
        Name = Database.Query.Domain.Vendor.Hawker.Manifest.Field.ById.name,
        Fields = {"DomainId","VendorId","OwnerId","Id"}
)
public class ManifestField extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.Manifest.Fields.Id)
    protected long Id;
    @Override
    public long getId(){return Id;}

    @Column (name=Database.Field.Domain.Entities.Vendor.Hawker.Manifest.Fields.DomainId)
    protected long DomainId;

    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.Manifest.Fields.VendorId)
    protected long VendorId;

    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.Manifest.Fields.Name)
    public String Name;

    @Column(name=Database.Field.Domain.Entities.Vendor.Hawker.Manifest.Fields.DefaultLength)
    protected long defaultLength;

    @ManyToOne(
            targetEntity=Manifest.class,
            cascade= CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name=Database.Field.Domain.Entities.Vendor.Hawker.Manifest.Fields.OwnerId)
    protected Manifest Owner;


    @Transient
    public ManifestFieldValue Value;

    public ManifestField(ManifestField src){
        Name = src.Name;
        Value = new ManifestFieldValue(src.Value.getKind());
        Value.setValue(src.Value.getData());
        defaultLength =src.Value.getKind().getLength();
    }
    public ManifestField(String name, ManifestFieldValueKind kind){
        Name = name;
        Value = new ManifestFieldValue(kind);
        defaultLength=kind.getLength();
    }
    public ManifestField(String name, String value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.String);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, Memo value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.Memo);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, VarString value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.StringList);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, int value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.Integer);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, long value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.Int64);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, KeyItem value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.KeyPair);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, KeyPair value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.KeyPairList);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, double value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.Double);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }
    public ManifestField(String name, boolean value) throws Exception{
        Name = name;
        Value = new ManifestFieldValue(ManifestFieldValueKind.Boolean);
        Value.setValue(value);
        defaultLength=Value.getKind().getLength();
    }

    public static void entityCreated(Entities List, Stored Entity){}
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade){}
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade){}
}
