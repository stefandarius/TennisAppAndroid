package com.example.stefan.tennis.models;

import com.example.stefan.tennis.database.TenisDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.sql.language.property.PropertyFactory;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.List;

@Table(name = "istoric_antrenamente", database = TenisDatabase.class)
public class IstoricAntrenamente extends BaseModel {

    public final static int USOR = 1;
    public final static int MEDIU = 2;
    public final static int GREU = 3;


    @PrimaryKey(autoincrement = true)
    private int id;
    @ForeignKey
    private Antrenor antrenor;
    @ForeignKey
    private AbonamenteSportivi abonamanteSportiv;
    @ForeignKey
    private TipAntrenament tipAntrenament;
    @Column(name = "grad_dificultate")
    private int gradDificultate;
    @Column
    private int rating;
    @Column(name = "data_antrenament")
    private Date dataAntrenament;

    public IstoricAntrenamente() {

    }

    public IstoricAntrenamente(int id, Antrenor antrenor, AbonamenteSportivi sportiv, TipAntrenament tipAntrenament, int gradDificultate, int rating, Date dataAntrenament) {
        this.id = id;
        this.antrenor = antrenor;
        this.abonamanteSportiv = sportiv;
        this.tipAntrenament = tipAntrenament;
        this.gradDificultate = gradDificultate;
        this.rating = rating;
        this.dataAntrenament = dataAntrenament;
    }

    public static List<IstoricAntrenamente> getAll() {
        return SQLite.select().from(IstoricAntrenamente.class).queryList();
    }

    public static IstoricAntrenamente getIstoricAntrenamentById(int id) {
        return SQLite.select().from(IstoricAntrenamente.class).where(IstoricAntrenamente_Table.id.eq(id)).querySingle();
    }

    public static List<IstoricAntrenamente> getAntrenamenteBySportiv(int idSportiv) {
        Where where = SQLite.select(Property.allProperty(IstoricAntrenamente.class)).from(IstoricAntrenamente.class)
                .innerJoin(AbonamenteSportivi.class)
                .on(IstoricAntrenamente_Table.abonamanteSportiv_id.withTable().eq(AbonamenteSportivi_Table.id.withTable()))
                .innerJoin(Sportiv.class).on(Sportiv_Table.id.withTable().eq(AbonamenteSportivi_Table.sportiv_id.withTable()))
                .where(Sportiv_Table.id.withTable().eq(idSportiv));
        return where.queryList();
    }

    public static List<IstoricAntrenamente> getAntrenamenteByDate(Date d) {
        Property<Long> pfDataAntrenament = PropertyFactory.from(d.getTime());
        Method date = new Method("DATE", IstoricAntrenamente_Table.data_antrenament.div(PropertyFactory.from(1000)), PropertyFactory.from("unixepoch"), PropertyFactory.from("localtime"));
        Method now = new Method("DATE", pfDataAntrenament.div(PropertyFactory.from(1000)), PropertyFactory.from("unixepoch"), PropertyFactory.from("localtime"));
        Where where = SQLite.select().from(IstoricAntrenamente.class).where(date.eq(now)).orderBy(IstoricAntrenamente_Table.data_antrenament, false);
        return where.queryList();
    }

    public static List<IstoricAntrenamente> getAntrenamentWithoutRating() {
        Where where = SQLite.select().from(IstoricAntrenamente.class)
                .where(IstoricAntrenamente_Table.rating.eq(0))
                .and(IstoricAntrenamente_Table.data_antrenament.lessThan(new Date()));
        return where.queryList();
    }

    public static long getNumarAntrenamenteWithoutRating() {
        return SQLite.selectCountOf().from(IstoricAntrenamente.class)
                .where(IstoricAntrenamente_Table.rating.eq(0))
                .and(IstoricAntrenamente_Table.data_antrenament.lessThan(new Date())).count();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Antrenor getAntrenor() {
        if (antrenor.getNumeComplet() == null)
            antrenor.load();
        return antrenor;
    }

    public void setAntrenor(Antrenor antrenor) {
        this.antrenor = antrenor;
    }

    public Abonament getAbonament() {
        return abonamanteSportiv.getAbonament();
    }

    public TipAntrenament getTipAntrenament() {
        if (tipAntrenament == null || tipAntrenament.getDenumirea() == null) {
            tipAntrenament.load();
        }
        return tipAntrenament;
    }

    public void setTipAntrenament(TipAntrenament tipAntrenament) {
        this.tipAntrenament = tipAntrenament;
    }

    public int getGradDificultate() {
        return gradDificultate;
    }

    public void setGradDificultate(int gradDificultate) {
        this.gradDificultate = gradDificultate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDataAntrenament() {
        return dataAntrenament;
    }

    public void setDataAntrenament(Date dataAntrenament) {
        this.dataAntrenament = dataAntrenament;
    }

    public AbonamenteSportivi getAbonamanteSportiv() {
        return abonamanteSportiv;
    }

    public void setAbonamanteSportiv(AbonamenteSportivi abonamenteSportiv) {
        this.abonamanteSportiv = abonamenteSportiv;
    }

    @Override
    public String toString() {
        return "IstoricAntrenamente{" +
                "id=" + id +
                ", antrenor=" + antrenor +
                ", abonamanteSportiv=" + abonamanteSportiv +
                ", tipAntrenament=" + tipAntrenament +
                ", gradDificultate=" + gradDificultate +
                ", rating=" + rating +
                ", dataAntrenament=" + dataAntrenament +
                '}';
    }
}
