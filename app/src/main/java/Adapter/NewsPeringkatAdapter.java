package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import data.NewsDataKatalog;
import data.NewsDataPeringkat;
import pptik.project.biodiversitywarriors.R;

/**
 * Created by Terminator on 09/11/2016.
 */

public class NewsPeringkatAdapter extends ArrayAdapter<NewsDataPeringkat> {

    private Context context;

    public NewsPeringkatAdapter(Context context, ArrayList<NewsDataPeringkat> a) {
        super(context,0,a);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(R.layout.view_peringkat, parent, false);

        NewsDataPeringkat news = getItem(position);

        TextView namaDpn = (TextView) convertView.findViewById(R.id.nmDpn);
        //TextView namaBlkg = (TextView) convertView.findViewById(R.id.nmBlkg);
        TextView poin = (TextView) convertView.findViewById(R.id.poin);
        TextView idusernya = (TextView) convertView.findViewById(R.id.idnyauser);
        //ImageView photonya = (ImageView) convertView.findViewById(R.id.img);
        CircularImageView photonya = (CircularImageView)convertView.findViewById(R.id.img);
        //thumbNail.setImageUrl(news.getGambar(), imageLoader);
        String urlfoto = "http://biodiversitywarriors.lskk.ee.itb.ac.id/user/gambar/userProfil/";
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
                    load(R.drawable.profi).
                    into(photonya);
        }

        idusernya.setText(news.getNo());
        idusernya.setVisibility(View.VISIBLE);
        namaDpn.setText(news.getNamaDpn() +" "+ news.getNamaBlkg());
        //namaBlkg.setText(news.getNamaBlkg());
        poin.setText(news.getPoin());
        return convertView;
    }
}