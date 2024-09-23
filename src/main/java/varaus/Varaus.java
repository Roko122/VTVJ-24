package varaus;

import java.sql.Timestamp;
import java.sql.Date;

public class Varaus {
    private final int varausID;
    private Date alkupvm, loppupvm;
    private Timestamp varausaika;
    private String toimipiste, asiakkaanSukunimi;

    public Varaus(int varausID, String asiakkaanSukunimi, String toimipiste, Date alkupvm, Date loppupvm, Timestamp varausaika) {
        this.varausID = varausID;
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.varausaika = varausaika;
        this.toimipiste = toimipiste;
        this.asiakkaanSukunimi = asiakkaanSukunimi;
    }

    public int getVarausID() {
        return varausID;
    }

    public Date getAlkupvm() {
        return alkupvm;
    }

    public void setAlkupvm(Date alkupvm) {
        this.alkupvm = alkupvm;
    }

    public Date getLoppupvm() {
        return loppupvm;
    }

    public void setLoppupvm(Date loppupvm) {
        this.loppupvm = loppupvm;
    }

    public Timestamp getVarausaika() {
        return varausaika;
    }

    public void setVarausaika(Timestamp varausAika) {
        this.varausaika = varausAika;
    }

    public String getToimipiste() {
        return toimipiste;
    }

    public void setToimipiste(String toimipiste) {
        this.toimipiste = toimipiste;
    }

    public String getAsiakkaanSukunimi() {
        return asiakkaanSukunimi;
    }

    public void setAsiakkaanSukunimi(String asiakkaanSukunimi) {
        this.asiakkaanSukunimi = asiakkaanSukunimi;
    }

    @Override
    public String toString() {
        return "Varaus{" +
                "varausID=" + varausID +
                ", alkupvm=" + alkupvm +
                ", loppupvm=" + loppupvm +
                ", varausaika=" + varausaika +
                ", toimipiste='" + toimipiste + '\'' +
                ", asiakkaanSukunimi='" + asiakkaanSukunimi + '\'' +
                '}';
    }
}

