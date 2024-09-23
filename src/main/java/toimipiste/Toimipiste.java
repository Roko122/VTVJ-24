package toimipiste;

public class Toimipiste {
    private String nimi;
    private String lahiosoite;
    private int toimipisteID;
    private int postinumero;
    private String postitoimipaikka;
    private int kapasiteetti;
    private double vrkhinta;

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getLahiosoite() {
        return lahiosoite;
    }

    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public int getToimipisteID() {
        return toimipisteID;
    }

    public void setToimipisteID(int toimipisteID) {
        this.toimipisteID = toimipisteID;
    }

    public int getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(int postinumero) {
        this.postinumero = postinumero;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }

    public int getKapasiteetti() {
        return kapasiteetti;
    }

    public void setKapasiteetti(int kapasiteetti) {
        this.kapasiteetti = kapasiteetti;
    }

    public double getVrkhinta() {
        return vrkhinta;
    }

    public void setVrkhinta(double vrkhinta) {
        this.vrkhinta = vrkhinta;
    }

    public Toimipiste(String nimi, String lahiosoite, int toimipisteID, int postinumero, String postitoimipaikka, int kapasiteetti, double vrkhinta) {
        this.nimi = nimi;
        this.lahiosoite = lahiosoite;
        this.toimipisteID = toimipisteID;
        this.postinumero = postinumero;
        this.postitoimipaikka = postitoimipaikka;
        this.kapasiteetti = kapasiteetti;
        this.vrkhinta = vrkhinta;
    }
}
