package com.example.stefan.tennis.models;

import com.example.stefan.tennis.database.TenisDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

@Table(name = "inregistrari_medicale", database = TenisDatabase.class)
public class InregistrareMedicala extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private int id;
    @Column(name = "data_inregistrare")
    private Date dataInregistrare;
    @ForeignKey(saveForeignKeyModel = false)
    private Sportiv sportiv;
    @Column
    private String descriere;

    public InregistrareMedicala() {

    }

    public InregistrareMedicala(int id, Date dataInregistrare, Sportiv sportiv, String descriere) {
        this.dataInregistrare = dataInregistrare;
        this.sportiv = sportiv;
        this.descriere = descriere;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataInregistrare() {
        return dataInregistrare;
    }

    public void setDataInregistrare(Date dataInregistrare) {
        this.dataInregistrare = dataInregistrare;
    }

    public Sportiv getSportiv() {
        return sportiv;
    }

    public void setSportiv(Sportiv sportiv) {
        this.sportiv = sportiv;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
