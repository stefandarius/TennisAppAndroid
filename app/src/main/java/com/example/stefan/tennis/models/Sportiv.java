package com.example.stefan.tennis.models;

import com.example.stefan.tennis.database.TenisDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Table(name = "sportivi", database = TenisDatabase.class)
public class Sportiv extends BaseModel {

    public final static int NIVEL_INCEPATOR = 1;
    public final static int NIVEL_INTERMEDIAR = 2;
    public final static int NIVEL_AVANSAT = 3;
    public final static int NIVEL_PROFESIONIST = 4;

    public final static int STARE_SANATOS = 1;
    public final static int STARE_BOLNAV = 2;
    public final static int STARE_ACCIDENTAT = 3;
    List<AbonamenteSportivi> inregistrariSedinte;
    List<InregistrareMedicala> inregistrariMedicale;
    @PrimaryKey(autoincrement = true)
    private int id;
    @Column
    private String nume;
    @Column
    private String prenume;
    @Column
    private Date dataNastere;
    @Column
    private boolean sex;
    @Column
    private int nivel;
    @Column
    private String email;
    @Column
    private int greutate;
    @Column
    private int inaltime;
    @Column(name = "stare_sanatate")
    private int stareSanatate;
    @Column(name = "numar_telefon")
    private String numarTelefon;

//    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "inregistrariSedinte")
//    public List<AbonamenteSportivi> getInregistrariSedinte() {
//        if (inregistrariSedinte == null || inregistrariSedinte.isEmpty()) {
//            inregistrariSedinte = SQLite.select()
//                    .from(AbonamenteSportivi.class)
//                    .where(AbonamenteSportivi_Table.sportiv_id.eq(id))
//                    .queryList();
//        }
//        return inregistrariSedinte;
//    }

//    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "inregistrariMedicale")
//    public List<InregistrareMedicala> getInregistrariMedicale() {
//        if (inregistrariMedicale == null || inregistrariMedicale.isEmpty()) {
//            inregistrariMedicale = SQLite.select()
//                    .from(InregistrareMedicala.class)
//                    .where(InregistrareMedicala_Table.sportiv_id.eq(id))
//                    .queryList();
//        }
//        return inregistrariMedicale;
//    }

    public Sportiv() {

    }

    public Sportiv(int id, String nume, String prenume, Date dataNastere, boolean sex, int nivel, String email, int greutate, int inaltime, int stareSanatate, String telefon) {

        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.dataNastere = dataNastere;
        this.sex = sex;
        this.nivel = nivel;
        this.email = email;
        this.greutate = greutate;
        this.inaltime = inaltime;
        this.stareSanatate = stareSanatate;
        this.numarTelefon = telefon;
    }

    public static Sportiv getSportivById(int id) {
        return SQLite.select().from(Sportiv.class).where(Sportiv_Table.id.eq(id)).querySingle();
    }

    public static List<Sportiv> getAll() {
        return SQLite.select()
                .from(Sportiv.class)
                .queryList();
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

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public Date getDataNastere() {
        return dataNastere;
    }

    public void setDataNastere(Date dataNastere) {
        this.dataNastere = dataNastere;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGreutate() {
        return greutate;
    }

    public void setGreutate(int greutate) {
        this.greutate = greutate;
    }

    public int getInaltime() {
        return inaltime;
    }

    public void setInaltime(int inaltime) {
        this.inaltime = inaltime;
    }

    public int getStareSanatate() {
        return stareSanatate;
    }

    public void setStareSanatate(int stareSanatate) {
        this.stareSanatate = stareSanatate;
    }

    public String getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(String numarTelefon) {
        this.numarTelefon = numarTelefon;
    }

    public int getVarsta() {
        Calendar c = Calendar.getInstance();
        int anCurent = c.get(Calendar.YEAR);
        int lunaCurenta = c.get(Calendar.MONTH);
        int ziCurenta = c.get(Calendar.DAY_OF_MONTH);
        c.setTime(dataNastere);
        int anNastere = c.get(Calendar.YEAR);
        int lunaNastere = c.get(Calendar.MONTH);
        int ziNastere = c.get(Calendar.DAY_OF_MONTH);
        if (lunaCurenta < lunaNastere || (lunaCurenta == lunaNastere && ziCurenta < ziNastere)) {
            anCurent--;
        }
        return anCurent - anNastere;
    }

    public String getVarstaAsString() {
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        c.setTime(dataNastere);
        long different = currentTime - c.getTimeInMillis();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long mothsInMilli = daysInMilli * 30;
        long yearInMilli = mothsInMilli * 12;
        long elapsedYear = different / yearInMilli;
        different = different % yearInMilli;
        long elapsedMonths = different / mothsInMilli;
        different = different % mothsInMilli;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        /*long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        System.out.println("--------- elapsedHours : " + elapsedHours);
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        System.out.println("--------- elapsedMinutes : " + elapsedMinutes);
        long elapsedSeconds = different / secondsInMilli;
        System.out.println("--------- elapsedSeconds : " + elapsedSeconds);
*/
        return String.format("%s ani, %s luni si %s zile", elapsedYear, elapsedMonths, elapsedDays);
    }

    public String getNumeComplet() {
        return nume + " " + prenume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sportiv)) return false;
        Sportiv sportiv = (Sportiv) o;
        return id == sportiv.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
