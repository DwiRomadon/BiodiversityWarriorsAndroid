package pptik.project.biodiversitywarriors;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.AppController;
import volley.Config_URL;

public class ViewDetailArtikelActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    TextView judul, tgl, isi, idpus,penulis;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SwipeRefreshLayout swipe;
    String id_news;
    ImageView img;

    private static final String TAG = ViewPostActivity.class.getSimpleName();
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Artikel");

        img         = (ImageView) findViewById(R.id.gambar_news);
        judul       = (TextView) findViewById(R.id.judul_news);
        tgl         = (TextView) findViewById(R.id.tgl_news);
        isi         = (TextView) findViewById(R.id.isi_news);
        id_news = getIntent().getStringExtra("idartikel");

        penulis     =  (TextView) findViewById(R.id.nmDpn);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id_news.isEmpty()) {
                               callDetailNews(id_news);
                           }
                       }
                   }
        );
    }

    //ambil data JSON cui
    private void callDetailNews(final String id){
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, Config_URL.DETAIL_POSTING, new Response.Listener<String>() {
            int position;
            // NewsData news = getItem(position);
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe.setRefreshing(false);

                try {
                    JSONObject obj = new JSONObject(response);

                    String Judul    = obj.getString("judul");
                    String Gambar   = obj.getString("thumbnail");
                    String Tgl      = obj.getString("tglpublish");
                    String Isi      = obj.getString("isi");

                    String publish  = obj.getString("namadpn");
                    String publish2  = obj.getString("namablkg");
                    //String publish  = obj.getString("namadpn");
                    String urlfoto = "http://biodiversitywarriors.lskk.ee.itb.ac.id/gambar/artikel/thumb/";
                    String fotoNya = urlfoto + Gambar;
                    String urlnya  = Gambar;

                    if(urlnya.length()>10){
                        Picasso.
                                with(ViewDetailArtikelActivity.this).
                                load(fotoNya).
                                into(img);
                    }else {
                        Picasso.
                                with(ViewDetailArtikelActivity.this).
                                load(R.drawable.logo).
                                into(img);
                    }
                    judul.setText(Judul);
                    tgl.setText(Tgl);
                    isi.setText(Html.fromHtml(Isi));
                    penulis.setText(publish + " " + publish2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewDetailArtikelActivity.this, "Please Check Your Network Connection Or Refresh This Activity", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                /*Toast.makeText(ViewDetailArtikelActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
                swipe.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idartikel", id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    @Override
    public void onRefresh() {
        callDetailNews(id_news);
    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(ViewDetailArtikelActivity.this, ArtikelActivity.class);
        startActivity(a);
        finish();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
