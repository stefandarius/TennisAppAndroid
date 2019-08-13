package com.example.stefan.tennis.models;

import com.example.stefan.tennis.database.TenisDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.List;

@Table(name = "notificari", database = TenisDatabase.class)
public class Notificare extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private int id;
    @Column
    private Type tip;
    @Column
    private int actiune;
    @Column
    private String continut;
    @Column
    private Date dataPrimire;
    @Column
    private Date dataCitire;
    @Column
    private Date dataStergere;

    public Notificare(int id, Type tip, int actiune, String continut, Date dataPrimire, Date dataCitire, Date dataStergere) {
        this.id = id;
        this.tip = tip;
        this.actiune = actiune;
        this.continut = continut;
        this.dataPrimire = dataPrimire;
        this.dataCitire = dataCitire;
        this.dataStergere = dataStergere;
    }

    public Notificare() {
    }

    public static List<Notificare> getNotificariNesterse() {
        Where where = SQLite.select().from(Notificare.class)
                .where(Notificare_Table.dataStergere.isNull());
        return where.queryList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getTip() {
        return tip;
    }

    public void setTip(Type tip) {
        this.tip = tip;
    }

    public int getActiune() {
        return actiune;
    }

    public void setActiune(int actiune) {
        this.actiune = actiune;
    }

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public Date getDataPrimire() {
        return dataPrimire;
    }

    public void setDataPrimire(Date dataPrimire) {
        this.dataPrimire = dataPrimire;
    }

    public Date getDataCitire() {
        return dataCitire;
    }

    public void setDataCitire(Date dataCitire) {
        this.dataCitire = dataCitire;
    }

    public Date getDataStergere() {
        return dataStergere;
    }

    public void setDataStergere(Date dataStergere) {
        this.dataStergere = dataStergere;
    }

    public enum Type {
        MESAJ,
        EVALUARE_ANTRENAMENT
    }
}
