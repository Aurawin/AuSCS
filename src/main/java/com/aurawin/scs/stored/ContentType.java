package com.aurawin.scs.stored;

import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.EntityDispatch;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryById;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.scs.lang.Database;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = Database.Table.ContentType)
@EntityDispatch(
        onCreated = false,
        onDeleted = false,
        onUpdated = false
)
@QueryById(
        Name = Database.Query.ContentType.ById.name,
        Fields = ("Id")
)

@QueryAll(
        Name = Database.Query.ContentType.All.name
)
public class ContentType extends Stored {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Database.Field.ContentType.Id)
    @Expose(serialize = true, deserialize = true)
    protected long Id;
    @Override
    public long getId() {
        return Id;
    }

    @Column(name = Database.Field.ContentType.Major)
    @Expose(serialize = true, deserialize = true)
    protected String Major;
    public String getMajor(){return Major;}
    public void setMajor(String major){ Major = major;}

    @Column(name = Database.Field.ContentType.Minor)
    @Expose(serialize = true, deserialize = true)
    protected String Minor;
    public String getMinor(){return Minor;}
    public void setMinor(String minor){ Minor = minor;}


    @Column(name = Database.Field.ContentType.Ext)
    @Expose(serialize = true, deserialize = true)
    protected String Ext;
    public String getExt(){return Ext;}
    public void setExt(String ext){ Ext = ext;}

    public volatile boolean Verified;

    public String getStamp(){
        return Major+"/"+Minor;
    }

    public void Assign(ContentType ct){
        Id = ct.Id;
        Major = ct.Major;
        Minor = ct.Minor;
        Ext = ct.Ext;
    }

    @Override
    public void Identify(Session ssn){
        if (Id == 0) {
            Transaction tx = (ssn.isJoinedToTransaction())? ssn.getTransaction() : ssn.beginTransaction();
            try {
                ssn.save(this);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                throw e;
            }
        }
    }
    public static void entityCreated(Stored Entity, boolean Cascade){ }
    public static void entityDeleted(Stored Entity, boolean Cascade) {}
    public static void entityUpdated(Stored Entity, boolean Cascade) {}
}
