package palvelu;

public class Palvelu {
    private final int palveluID;
    private String nimi, tyyppi;
    private int hinta;


    public Palvelu(int palveluID, String nimi, int hinta, String tyyppi) {
        this.palveluID = palveluID;
        this.nimi = nimi;
        this.hinta = hinta;
        this.tyyppi = tyyppi;
    }
    public int getPalveluID(){return palveluID;}
    public String getNimi(){return nimi;}
    public void setNimi(String nimi){this.nimi = nimi;}
    public int getHinta(){return hinta;}
    public void setHinta(int hinta){this.hinta = hinta;}
    public String getTyyppi(){return tyyppi;}
    public void setTyyppi(String tyyppi){this.tyyppi = tyyppi;}

    public String toString() {
        return "Palvelu{"+"palveluID="+ palveluID +
                ", nimi='" + nimi + '\'' +
                ", hinta='" + hinta + '\'' +
                ", tyyppi='" + tyyppi + '\''+
                '}';
    }
}
