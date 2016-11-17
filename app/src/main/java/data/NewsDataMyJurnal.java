package data;

/**
 * Created by Terminator on 14/11/2016.
 */

public class NewsDataMyJurnal {

    private String id, judul, tgl, gambar, like, namaDpn, namaBlkg, kategori;

    public NewsDataMyJurnal(String id,String judul, String gambar, String tgl, String like, String namaDpn,String namaBlkg, String kategori) {
        this.id = id;
        this.judul = judul;
        this.gambar = gambar;
        this.tgl = tgl;
        this.like = like;
        this.namaDpn = namaDpn;
        this.namaBlkg = namaBlkg;
        this.kategori = kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
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

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
