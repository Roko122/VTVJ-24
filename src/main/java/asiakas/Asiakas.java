package asiakas;

public class Asiakas {
    private final int asiakasID;
    private String etunimi, sukunimi, sahkopostiosoite, puhelinnumero, lahiosoite, postinumero,
            postitoimipaikka;

    public Asiakas(int asiakasID, String etunimi, String sukunimi, String sahkopostiosoite, String puhelinnumero,
                   String lahiosoite, String postinumero, String postitoimipaikka) {
        this.asiakasID = asiakasID;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.sahkopostiosoite = sahkopostiosoite;
        this.puhelinnumero = puhelinnumero;
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
        this.postitoimipaikka = postitoimipaikka;
    }

    public int getAsiakasID() {
        return asiakasID;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getSahkopostiosoite() {
        return sahkopostiosoite;
    }

    public void setSahkopostiosoite(String sahkopostiosoite) {
        this.sahkopostiosoite = sahkopostiosoite;
    }

    public String getLahiosoite() {
        return lahiosoite;
    }

    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    @Override
    public String toString() {
        return "Asiakas{" +
                "asiakasID=" + asiakasID +
                ", etunimi='" + etunimi + '\'' +
                ", sukunimi='" + sukunimi + '\'' +
                ", sahkopostiosoite='" + sahkopostiosoite + '\'' +
                ", puhelinnumero='" + puhelinnumero + '\'' +
                ", lahiosoite='" + lahiosoite + '\'' +
                ", postinumero='" + postinumero + '\'' +
                ", postitoimipaikka='" + postitoimipaikka + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object asiakas) {
        if (asiakas == null || getClass() != asiakas.getClass()) {
            return false;
        }
        Asiakas toinenAsiakas = (Asiakas) asiakas;
        return this.asiakasID == toinenAsiakas.getAsiakasID();
    }
}
