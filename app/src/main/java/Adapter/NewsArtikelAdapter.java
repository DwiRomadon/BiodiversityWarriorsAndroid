package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import data.NewsData;
import data.NewsDataArtikel;
import pptik.project.biodiversitywarriors.R;

/**
 * Created by Terminator on 06/11/2016.
 */

public class NewsArtikelAdapter extends ArrayAdapter<NewsDataArtikel> {

    private Context context;

    public NewsArtikelAdapter(Context context, ArrayList<NewsDataArtikel> a) {
        super(context,0,a);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(R.layout.view_list_artikel, parent, false);

        NewsDataArtikel news = getItem(position);

        ImageView photonya = (ImageView) convertView.findViewById(R.id.news_gambar);
        TextView judul = (TextView) convertView.findViewById(R.id.judul);
        TextView timestamp = (TextView) convertView.findViewById(R.id.tgl);
        TextView like = (TextView) convertView.findViewById(R.id.like);
        TextView idnya = (TextView) convertView.findViewById(R.id.idnya);
        TextView publisher = (TextView) convertView.findViewById(R.id.iduser);
        TextView nama  = (TextView) convertView.findViewById(R.id.nmDpn);

        //thumbNail.setImageUrl(news.getGambar(), imageLoader);
        String urlfoto = "http://biodiversitywarriors.lskk.ee.itb.ac.id/gambar/artikel/thumb/";
        String urlnya =news.getGambar();
        String fotoNya = urlfoto+news.getGambar();
        String a = fotoNya;

        if(urlnya.length()>10){
            Picasso.
                    with(this.context).
                    load(a).
                    into(photonya);
        }else{
            Picasso.
                    with(this.context).
                    load(R.drawable.logo).
                    into(photonya);
        }

        idnya.setText(news.getId());
        idnya.setVisibility(View.INVISIBLE);
        //publisher.setText(news.getPublisher());
        publisher.setVisibility(View.INVISIBLE);
        judul.setText(news.getJudul());
        like.setText(" " + news.getLike());
        timestamp.setText(news.getDatetime());
        nama.setText(news.getNamaDpn()+ " " + news.getNamaBlkg());
        return convertView;
    }
}
