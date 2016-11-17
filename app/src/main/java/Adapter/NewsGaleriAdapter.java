package Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import data.NewsDataGaleri;
import data.NewsDataJurnal;
import pptik.project.biodiversitywarriors.R;

/**
 * Created by Terminator on 06/11/2016.
 */

public class NewsGaleriAdapter extends ArrayAdapter<NewsDataGaleri> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<NewsDataGaleri> mGridData = new ArrayList<NewsDataGaleri>();

    public NewsGaleriAdapter(Context mContext, int layoutResourceId, ArrayList<NewsDataGaleri> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<NewsDataGaleri> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewsGaleriAdapter.ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new NewsGaleriAdapter.ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.idTextview = (TextView) row.findViewById(R.id.grid_item_id);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (NewsGaleriAdapter.ViewHolder) row.getTag();
        }

        NewsDataGaleri item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getJudul()));
        holder.idTextview.setText(item.getId());
        String urlfoto = "http://biodiversitywarriors.lskk.ee.itb.ac.id/gambar/artikel/thumb/";
        String urlnya =item.getGambar();
        String fotoNya = urlfoto+item.getGambar();
        String a = fotoNya;


        if(urlnya.length()>20){
            Picasso.with(mContext).load(a).into(holder.imageView);
        }else{
            Picasso.with(mContext).load(R.drawable.logo).into(holder.imageView);
        }

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView idTextview;
        ImageView imageView;
    }
}
