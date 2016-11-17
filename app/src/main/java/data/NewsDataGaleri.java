package data;

import java.util.ArrayList;

import pptik.project.biodiversitywarriors.GaleriActivity;

/**
 * Created by Terminator on 06/11/2016.
 */

public class NewsDataGaleri {
    private String id, judul, datetime, gambar, like,publisher;


    public NewsDataGaleri(String id,String judul, String gambar, String tgl, String like, String publisher) {
        this.id = id;
        this.judul = judul;
        this.gambar = gambar;
        this.datetime = tgl;
        this.like = like;
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher(){return publisher;}

    public void setPublisher(String iduser){this.publisher = publisher;}

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getGambar() {
        //gambar = "http://biodiversitywarriors.lskk.ee.itb.ac.id/gambar/artikel/thumb/";
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLike(){
        return like;
    }

    public void setLike(String like){
        this.like = like;
    }
}
