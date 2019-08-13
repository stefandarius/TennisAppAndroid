package com.example.stefan.tennis.models;

import com.example.stefan.tennis.database.TenisDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(name = "abonamente", database = TenisDatabase.class)
public class Abonament extends BaseModel {
    List<AbonamenteSportivi> inregistrariSedinte;
    @PrimaryKey(autoincrement = true)
    private int id;
    @Column
    private String denumire;
    @Column(name = "numar_sedinte")
    private int nrSedinte;
    @Column
    private int pret;
    @Column
    private int durata;

    public Abonament() {

    }

    public Abonament(int id, String denumire, int nrSedinte, int pret) {
        this.denumire = denumire;
        this.nrSedinte = nrSedinte;
        this.pret = pret;
        this.id = id;
    }

    public static List<Abonament> getAll() {
        return SQLite.select()
                .from(Abonament.class)
                .queryList();
    }

    public static Abonament getAbonamentById(int id) {
        return SQLite.select().from(Abonament.class).where(Abonament_Table.id.eq(id)).querySingle();
    }

    //    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "inregistrariSedinte")
//    public List<AbonamenteSportivi> getInregistrariSedinte(){
//        if(inregistrariSedinte == null || inregistrariSedinte.isEmpty()){
//            inregistrariSedinte = SQLite.select()
//                    .from(AbonamenteSportivi.class)
//                    .where(AbonamenteSportivi_Table.abonament_id.eq(id))
//                    .queryList();
//        }
//        return inregistrariSedinte;
//    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNrSedinte() {
        return nrSedinte;
    }

    public void setNrSedinte(int nrSedinte) {
        this.nrSedinte = nrSedinte;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Abonament abonament = (Abonament) o;

        return id == abonament.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

}
