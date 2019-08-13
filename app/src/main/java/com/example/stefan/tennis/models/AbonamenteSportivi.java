package com.example.stefan.tennis.models;

import android.util.Log;

import com.example.stefan.tennis.database.TenisDatabase;
import com.example.stefan.tennis.utils.ProjectUtils;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.sql.language.property.PropertyFactory;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Table(name = "abonamente_sportivi", database = TenisDatabase.class)
public class AbonamenteSportivi extends BaseModel {
    List<IstoricAntrenamente> istoricAntrenamenteList;
    @PrimaryKey(autoincrement = true)
    private int id;
    @ForeignKey
    private Sportiv sportiv;
    @ForeignKey
    private Abonament abonament;
    @Column(name = "data_inceput")
    private Date dataInceput;

    public AbonamenteSportivi() {

    }

    public AbonamenteSportivi(int id, Sportiv sportiv, Abonament abonament, Date dataInceput) {
        this.id = id;
        this.sportiv = sportiv;
        this.abonament = abonament;
        this.dataInceput = dataInceput;
    }

    public static List<Abonament> getAbonamenteBySportiv(int id) {
        Where where = SQLite.select(Abonament_Table.id.withTable(),
                Abonament_Table.denumire.withTable(), Abonament_Table.numar_sedinte.withTable(),
                Abonament_Table.pret.withTable())
                .from(Abonament.class)
                .innerJoin(AbonamenteSportivi.class)
                .on(AbonamenteSportivi_Table.abonament_id.withTable().eq(Abonament_Table.id.withTable()))
                .where(AbonamenteSportivi_Table.sportiv_id.withTable().eq(id));
        Log.v("SQLLearning", where.getQuery());
        return where.queryList();
    }

    public static List<AbonamenteSportivi> getAbonamenteSportivBySportivId(int id) {
        return SQLite.select().from(AbonamenteSportivi.class).where(AbonamenteSportivi_Table.sportiv_id.eq(id))
                .queryList();
    }

    public static AbonamenteSportivi getValabilNrSedinteBySportiv(int id) {
        List<AbonamenteSportivi> abonamenteSportivi = SQLite.select(Property.allProperty(AbonamenteSportivi.class)).from(AbonamenteSportivi.class)
                .innerJoin(Abonament.class)
                .on(AbonamenteSportivi_Table.abonament_id.withTable().eq(Abonament_Table.id.withTable()))
                .where(AbonamenteSportivi_Table.sportiv_id.eq(id)).queryList();
        for (AbonamenteSportivi ab : abonamenteSportivi)
            if (ab.getSedinteRamase() > 0)
                return ab;
        return null;
    }

    public static AbonamenteSportivi getAbonamentValabilBySportivId(int id, Date dataAntrenament) {
        List<AbonamenteSportivi> abonementeSportivi = SQLite.select(Property.allProperty(AbonamenteSportivi.class)).from(AbonamenteSportivi.class)
                .innerJoin(Abonament.class)
                .on(AbonamenteSportivi_Table.abonament_id.withTable().eq(Abonament_Table.id.withTable()))
                .where(AbonamenteSportivi_Table.sportiv_id.eq(id))
                .and(AbonamenteSportivi_Table.data_inceput
                        .plus(Abonament_Table.durata
                                .times(PropertyFactory.from(24 * 3600 * 1000)))
                        .greaterThanOrEq(dataAntrenament))
                .orderBy(AbonamenteSportivi_Table.data_inceput.withTable(), true).queryList();
        long efectuate;
        long nrSedinteRamase;
        for (AbonamenteSportivi ab : abonementeSportivi) {
            Log.v("valide", ProjectUtils.getFormattedDate(ab.getDataInceput()) + " " + ab.getAbonament().getDenumire() + " " + ab.getId());
            efectuate = SQLite.selectCountOf().from(IstoricAntrenamente.class)
                    .where(IstoricAntrenamente_Table.abonamanteSportiv_id.eq(ab.getId())).count();
            nrSedinteRamase = ab.getAbonament().getNrSedinte() - efectuate;
            Log.v("valide", "Detalii sedinte " + ab.getAbonament().getDenumire() + " " + efectuate + " - " + ab.getAbonament().getNrSedinte());
            if (nrSedinteRamase > 0)
                return ab;
        }
        return null;
    }

    public static AbonamenteSportivi getAbonamentValabiByDate(int id, Date dataAntrenament) {
        Where where = SQLite.select(Property.allProperty(AbonamenteSportivi.class)).from(AbonamenteSportivi.class)
                .innerJoin(Abonament.class)
                .on(AbonamenteSportivi_Table.abonament_id.withTable().eq(Abonament_Table.id.withTable()))
                .where(AbonamenteSportivi_Table.sportiv_id.eq(id))
                .and(AbonamenteSportivi_Table.data_inceput
                        .plus(Abonament_Table.durata
                                .times(PropertyFactory.from(24 * 3600 * 1000)))
                        .greaterThanOrEq(dataAntrenament))
                .orderBy(AbonamenteSportivi_Table.data_inceput.withTable(), true);
        Log.v("ABS", where.getQuery());
        List<AbonamenteSportivi> abonementeSportivi = where.queryList();
        if (abonementeSportivi.size() > 0)
            return abonementeSportivi.get(0);
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sportiv getSportiv() {
        return sportiv;
    }

    public void setSportiv(Sportiv sportiv) {
        this.sportiv = sportiv;
    }

    public Abonament getAbonament() {
        return abonament;
    }

    public void setAbonament(Abonament abonament) {
        this.abonament = abonament;
    }

    public Date getDataInceput() {
        return dataInceput;
    }

    public void setDataInceput(Date dataInceput) {
        this.dataInceput = dataInceput;
    }

    @Override
    public String toString() {
        return "AbonamenteSportivi{" +
                "id=" + id +
                ", sportiv=" + sportiv +
                ", abonament=" + abonament +
                ", dataInceput=" + dataInceput +
                ", istoricAntrenamenteList=" + istoricAntrenamenteList +
                '}';
    }

    public int getSedinteRamase() {
        long efectuate = SQLite.selectCountOf().from(IstoricAntrenamente.class)
                .where(IstoricAntrenamente_Table.abonamanteSportiv_id.eq(getId())).count();
        int ramase = abonament.getNrSedinte() - (int) efectuate;
        return ramase;
    }

    public Date getDataSfarsit() {
        Calendar c = Calendar.getInstance();
        c.setTime(dataInceput);
        c.add(Calendar.DAY_OF_MONTH, abonament.getDurata());
        return c.getTime();
    }

    public boolean isValabil() {
        return getDataSfarsit().after(new Date()) && getSedinteRamase() > 0;
    }
}
