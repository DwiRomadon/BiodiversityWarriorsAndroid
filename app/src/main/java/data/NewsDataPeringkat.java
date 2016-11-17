package data;

/**
 * Created by Terminator on 09/11/2016.
 */

public class NewsDataPeringkat {

    private String no, namaDpn, namaBlkg, poin, gambar;


    public NewsDataPeringkat(String no, String namaDpn, String namaBlkg, String poin, String gambar){
        this.no       = no;
        this.namaDpn  = namaDpn;
        this.namaBlkg = namaBlkg;
        this.poin     = poin;
        this.gambar   = gambar;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNamaDpn() {
        return namaDpn;
    }

    public void setNamaDpn(String namaDpn) {
        this.namaDpn = namaDpn;
    }

    public String getNamaBlkg() {
        return namaBlkg;
    }

    public void setNamaBlkg(String namaBlkg) {
        this.namaBlkg = namaBlkg;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    public String getGambar(){
        return gambar;
    }

    public void setGambar(String gambar){
        this.gambar = gambar;
    }

}
