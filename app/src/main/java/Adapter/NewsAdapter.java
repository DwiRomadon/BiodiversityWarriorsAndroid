package Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import data.NewsData;
import pptik.project.biodiversitywarriors.MainActivity;
import pptik.project.biodiversitywarriors.R;
import volley.AppController;

/**
 * Created by Terminator on 06/10/2016.
 */

public class NewsAdapter extends ArrayAdapter<NewsData> {

    private Context context;

    public NewsAdapter(Context context, ArrayList<NewsData> newsItems) {
        super(context, 0, newsItems);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(R.layout.view_list, parent, false);

        NewsData news = getItem(position);

        ImageView photonya = (ImageView) convertView.findViewById(R.id.news_gambar);
        TextView judul = (TextView) convertView.findViewById(R.id.judul);
        TextView timestamp = (TextView) convertView.findViewById(R.id.tgl);
        TextView like = (TextView) convertView.findViewById(R.id.like);
        TextView idnya = (TextView) convertView.findViewById(R.id.idnya);
        TextView nama  = (TextView) convertView.findViewById(R.id.nmDpn);
        TextView publisher = (TextView) convertView.findViewById(R.id.iduser);
        TextView kategori = (TextView) convertView.findViewById(R.id.kategori);

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
        kategori.setText(news.getKategori());
        return convertView;
    }
}
