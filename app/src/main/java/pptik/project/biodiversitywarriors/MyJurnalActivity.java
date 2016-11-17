package pptik.project.biodiversitywarriors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.NewsJurnalAdapter;
import Adapter.NewsMyJurnalAdapter;
import data.NewsDataMyJurnal;
import volley.AppController;
import volley.Config_URL;

public class MyJurnalActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{


    private static final String TAG = ViewPostActivity.class.getSimpleName();
    ArrayList<NewsDataMyJurnal> dataNya = new ArrayList<NewsDataMyJurnal>();

    NewsMyJurnalAdapter adapter;
    String tag_json_obj = "json_obj_req";

    SwipeRefreshLayout swipe;
    ListView list;

    Handler handler;
    Runnable runnable;
    String iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jurnal);

        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Jurnal Saya");

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.array_list);
        dataNya.clear();

        adapter = new NewsMyJurnalAdapter(MyJurnalActivity.this, dataNya);
        list.setAdapter(adapter);
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //if (!iduser.isEmpty()) {
                               swipe.setRefreshing(true);
                               dataNya.clear();
                               adapter.notifyDataSetChanged();
                               callDetailNews(iduser);
                           //}
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
                            callDetailNews(iduser);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });

    }

    //ambil data JSON cui
    private void callDetailNews(final String id){
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, Config_URL.VIEW_MY_JURNAL,
                new Response.Listener<String>() {
                    int position;
                    // NewsData news = getItem(position);
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response " + response.toString());
                        swipe.setRefreshing(false);

                        if (response.length() > 0) {
                         for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = new JSONObject(response);
                                //JSONArray array = c.getJSONArray("result");
                                //JSONObject obj = array.getJSONObject(i);

                                //String id = obj.getString("iduser");
                                SharedPreferences prefs = getSharedPreferences("myjurnal",
                                        Context.MODE_PRIVATE);
                                String id = obj.getString("judul");
                                iduser = prefs.getString("iduser",id);
                                String judul = obj.getString("judul");
                                String gambar = obj.getString("thumbnail");
                                String tgl = obj.getString("tglpublish");
                                String like = obj.getString("likes");
                                String namaDpn = obj.getString("namadpn");
                                String namaBlkg = obj.getString("namablkg");
                                String kategori = obj.getString("kategori");

                                dataNya.add(new NewsDataMyJurnal(iduser,judul,gambar,tgl,like,namaDpn,namaBlkg,kategori));

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                Toast.makeText(MyJurnalActivity.this, "Please Check Your Network Connection Or Refresh This Activity", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                /*Toast.makeText(ViewPostActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
                swipe.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("iduser", id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }



    @Override
    public void onBackPressed() {
        Intent a = new Intent(MyJurnalActivity.this, MainActivity.class);
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
        callDetailNews(iduser);
    }
}
