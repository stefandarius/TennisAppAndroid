package com.example.stefan.tennis.models;

import com.example.stefan.tennis.database.TenisDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(name = "tipuri_antrenamente", database = TenisDatabase.class)
public class TipAntrenament extends BaseModel {

    List<IstoricAntrenamente> istoricAntrenamente;
    @PrimaryKey(autoincrement = true)
    private int id;
    @Unique(onUniqueConflict = ConflictAction.FAIL)
    @Column
    private String denumirea;
    @Column
    private boolean activ;

    public TipAntrenament() {

    }

    public TipAntrenament(int id, String denumirea, boolean activ) {
        this.id = id;
        this.denumirea = denumirea;
        this.activ = activ;
    }

    public TipAntrenament(String s) {
        this.denumirea = s;
    }

//    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "istoricAntrenamente")
//    public List<IstoricAntrenamente> getInregistrariSedinte() {
//        if (istoricAntrenamente == null || istoricAntrenamente.isEmpty()) {
//            istoricAntrenamente = SQLite.select()
//                    .from(IstoricAntrenamente.class)
//                    .where(IstoricAntrenamente_Table.tipAntrenament_id.eq(id))
//                    .queryList();
//        }
//        return istoricAntrenamente;
//    }

    public static List<TipAntrenament> getAll() {
        return SQLite.select()
                .from(TipAntrenament.class)
                .queryList();
    }

    public static TipAntrenament getTipAntrenamentById(int id) {
        return SQLite.select()
                .from(TipAntrenament.class)
                .where(TipAntrenament_Table.id.eq(id))
                .querySingle();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenumirea() {
        return denumirea;
    }

    public void setDenumirea(String denumirea) {
        this.denumirea = denumirea;
    }

    public boolean isActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TipAntrenament that = (TipAntrenament) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return denumirea;
    }
}
