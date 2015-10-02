package com.aurawin.core.stored.entities.vendor.hawker.manifest;

import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.entities.Entities;
import com.google.gson.Gson;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;


import javax.persistence.*;
import java.util.ArrayList;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name= Database.Table.Domain.Vendor.Hawker.Manifest.Items)
@EntityDispatch(
        onCreated = false,
        onDeleted = true,
        onUpdated = false
)
public class Manifest extends Stored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.Manifest.Id)
    protected long Id;
    @Override
    public long getId(){
        return Id;
    }

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.Manifest.DomainId)
    protected long DomainId;
    public long getDomainId(){return DomainId;}

    @Column(name = Database.Field.Domain.Entities.Vendor.Hawker.Manifest.VendorId)
    protected long VendorId;
    public long getVendorId(){return VendorId;}

    @OneToMany(
            targetEntity = ManifestField.class,
            cascade =CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy="Owner"
    )
    protected ArrayList<ManifestField> fields = new ArrayList<ManifestField>();

    public ManifestField addField(String name, ManifestFieldValueKind kind){
        ManifestField f = new ManifestField(name,kind);
        fields.add(f);
        return f;
    }
    public void fromJSON(String src) throws Exception{
        Assign(new Gson().fromJson(src,Manifest.class));
    }
    public void Empty(){
        Id=0;
        DomainId=0;
        VendorId=0;
        fields.clear();
    }
    public void Assign(Manifest m){
        Empty();
        Id=m.Id;
        DomainId=m.DomainId;
        VendorId=m.VendorId;
        for (ManifestField f : m.fields){
            fields.add(new ManifestField(f));
        }
    }
    public static void entityCreated(Entities List, Stored Entity){}
    public static void entityDeleted(Entities List, Stored Entity, boolean Cascade){}
    public static void entityUpdated(Entities List, Stored Entity, boolean Cascade){}
}
