package pptik.project.biodiversitywarriors;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.NewsArtikelAdapter;
import data.NewsData;
import data.NewsDataArtikel;
import volley.AppController;
import volley.Config_URL;

public class ArtikelActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipe;
    private static final String TAG = MainActivity.class.getSimpleName();

    ArrayList<NewsDataArtikel> dataNya = new ArrayList<NewsDataArtikel>();

    NewsArtikelAdapter adapter;
    ListView list;

    Handler handler;
    Runnable runnable;

    int no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Artikel");

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.array_list);
        dataNya.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ArtikelActivity.this, ViewDetailArtikelActivity.class);
                intent.putExtra("idartikel", dataNya.get(position).getId());
                //intent.putExtra("publisher", dataNya.get(position).getPublisher());
                startActivity(intent);
            }
        });

        adapter = new NewsArtikelAdapter(ArtikelActivity.this, dataNya);
        list.setAdapter(adapter);
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           dataNya.clear();
                           adapter.notifyDataSetChanged();
                           callNews();
                       }
                   }
        );

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            callNews();
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });
    }

    //News Artikel
    private void callNews(){

        swipe.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(Config_URL.VIEW_ARTIKEL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    no = obj.getInt("no");
                                    String id     = obj.getString("idartikel");
                                    String judul  = obj.getString("judul");
                                    String gambar = obj.getString("thumbnail");
                                    String tgl    = obj.getString("tglpublish");
                                    String like   = obj.getString("likes");
                                    String namaDpn = obj.getString("namadpn");
                                    String namaBlkg = obj.getString("namablkg");

                                    dataNya.add(new NewsDataArtikel(id,judul,gambar,tgl,like,namaDpn,namaBlkg));

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ArtikelActivity.this, "Please Check Your Network Connection Or Refresh This Activity", Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(ArtikelActivity.this, MainActivity.class);
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

    @Override
    public void onRefresh() {
        dataNya.clear();
        adapter.notifyDataSetChanged();
        callNews();
    }
}
