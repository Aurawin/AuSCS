package com.aurawin.scs.stored;

import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.*;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.Package;
import com.aurawin.scs.lang.Database;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;

@Entity
@Namespaced
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = Database.Table.ContentType)
@NamedQueries(
        {
            @NamedQuery(
                    name  = Database.Query.ContentType.ById.name,
                    query = Database.Query.ContentType.ById.value
            ),
            @NamedQuery(
                    name  = Database.Query.ContentType.All.name,
                    query = Database.Query.ContentType.All.value
)
        }
                )
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

    public String getStamp(){
        return Major+"/"+Minor;
    }

    public void Assign(ContentType ct){
        Id = ct.Id;
        Major = ct.Major;
        Minor = ct.Minor;
        Ext = ct.Ext;
    }

    public static void installDefaults(){
        Gson gson = new Gson();
        MemoryStream ms = new MemoryStream();
        try {
            ms.Write(ContentType.class.getClassLoader().getResourceAsStream("default.content.types"));
            JSONObject jo = new JSONObject(ms.toString());
            Iterator it =jo.keys();
            String key="";
            String sKey;
            String sVal;
            String[] ct;
            JSONArray ja;
            JSONObject jk;
            ContentType ctDB;
            while (it.hasNext()){
                key = (String) it.next();
                if (key.equalsIgnoreCase("ContentTypes")) {
                    Object a = jo.get(key);
                    if (a instanceof JSONArray) {
                        ja = (JSONArray) a;
                        Iterator jt = ja.iterator();
                        while (jt.hasNext()) {
                            jk = (JSONObject) jt.next();
                            sKey = jk.keys().next();
                            sVal = jk.getString(sKey);
                            ct = sKey.split("/");
                            if (ct.length == 2) {
                                ctDB = Entities.Settings.ContentType.Lookup(ct[0], ct[1], sVal);
                                if (ctDB == null) {
                                    ctDB = new ContentType();
                                    ctDB.Major = ct[0];
                                    ctDB.Minor = ct[1];
                                    ctDB.Ext = sVal;
                                    assert ((sVal != null) && (ct[1] != null) && (ct[0] != null));
                                    Entities.Save(ctDB, CascadeOn);
                                }

                            }
                        }
                    }
                }

            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally{
            ms.close();
        }


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
