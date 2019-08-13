package com.example.stefan.tennis.models;

import com.example.stefan.tennis.database.TenisDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(name = "antrenori", database = TenisDatabase.class)
public class Antrenor extends BaseModel {

    List<IstoricAntrenamente> istoricAntrenamenteList;
    @PrimaryKey(autoincrement = true)
    private int id;
    @Column
    private String nume;
    @Column
    private String prenume;
    //    @Column
//    private String parola;
    @Column
    private String email;

    public Antrenor() {

    }

    public Antrenor(String s) {
        nume = s;
    }

    public Antrenor(String nume, String prenume, String email) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        //this.parola = parola;
    }

//    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "istoricAntrenamenteList")
//    public List<IstoricAntrenamente> getIstoricAntrenamente() {
//        if (istoricAntrenamenteList == null || istoricAntrenamenteList.isEmpty()) {
//            istoricAntrenamenteList = SQLite.select()
//                    .from(IstoricAntrenamente.class)
//                    .where(IstoricAntrenamente_Table.antrenor_id.eq(id))
//                    .queryList();
//        }
//        return istoricAntrenamenteList;
//    }

    public static List<Antrenor> getAll() {
        return SQLite.select()
                .from(Antrenor.class)
                .queryList();
    }

    public static Antrenor getAntrenorById(int id) {
        return SQLite.select().from(Antrenor.class).where(Antrenor_Table.id.eq(id)).querySingle();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

//    public String getParola() {
//        return parola;
//    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getNumeComplet() {
        if (prenume == null)
            return nume;
        return nume + " " + prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Antrenor antrenor = (Antrenor) o;

        return id == antrenor.id;
    }

    @Override
    public int hashCode() {
        return id;
    }


    @Override
    public String toString() {
        return getNumeComplet();
    }
}
