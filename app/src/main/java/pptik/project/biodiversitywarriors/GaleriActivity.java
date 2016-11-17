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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.NewsGaleriAdapter;
import Adapter.NewsKatalogAdapter;
import data.NewsDataGaleri;
import data.NewsDataKatalog;
import volley.AppController;
import volley.Config_URL;

public class GaleriActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    //private static final String TAG = GaleriActivity.class.getSimpleName();
    SwipeRefreshLayout swipe;
    private static final String TAG = MainActivity.class.getSimpleName();

    ArrayList<NewsDataGaleri> dataNya = new ArrayList<NewsDataGaleri>();

    NewsGaleriAdapter adapter;
    //ListView list;

    Handler handler;
    Runnable runnable;

    int no;


    private ProgressBar mProgressBar;
    private GridView mGridView;

    //private GridViewAdapters mGridAdapter;
    //private ArrayList<GridItems> mGridData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Galeri");

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //list = (ListView) findViewById(R.id.array_list);
        //dataNya.clear();

        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(GaleriActivity.this, ViewDetailGaleri.class);
                intent.putExtra("idartikel", dataNya.get(position).getId());
                intent.putExtra("publisher", dataNya.get(position).getPublisher());
                startActivity(intent);
            }
        });*/


        mGridView = (GridView) findViewById(R.id.gridView);

        //Initialize with empty data
        dataNya = new ArrayList<>();
        //mGridAdapter = new GridViewAdapters(this, R.layout.grid_item_layout, mGridData);
        adapter = new NewsGaleriAdapter(this, R.layout.grid_item_layout, dataNya);
        mGridView.setAdapter(adapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                NewsDataGaleri item = (NewsDataGaleri) parent.getItemAtPosition(position);

                //Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                /*intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight()).
                        putExtra("title", item.getTitle()).
                        putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);*/
                Intent intent = new Intent(GaleriActivity.this, ViewDetailGaleri.class);
                intent.putExtra("idartikel", dataNya.get(position).getId());
                intent.putExtra("publisher", dataNya.get(position).getPublisher());
                startActivity(intent);
            }
        });



        //list.setAdapter(adapter);
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
    }

    //News Artikel
    private void callNews(){

        swipe.setRefreshing(true);
        //mProgressBar.setVisibility(View.VISIBLE);

        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(Config_URL.VIEW_GALERI,
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
                                    String tgl    = obj.getString("tgl");
                                    String like   = obj.getString("likes");
                                    String publisher = obj.getString("publisher");

                                    dataNya.add(new NewsDataGaleri(id,judul,gambar,tgl,like,publisher));

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        swipe.setRefreshing(false);
                        //mProgressBar.setVisibility(View.GONE);

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GaleriActivity.this, "Please Check Your Network Connection Or Refresh This Activity", Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                //mProgressBar.setVisibility(View.GONE);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(GaleriActivity.this, MainActivity.class);
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
