package pptik.project.biodiversitywarriors;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.NewsAdapter;
import data.NewsData;
import data.NewsDataMyJurnal;
import data.NewsDataPeringkat;
import helper.SessionManager;
import volley.AppController;
import volley.Config_URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{


    //DrawerLayout drawer;
    Toolbar toolbar;

    String email_p = "-";
    String user_name = "Please Login !";

    private ProgressDialog pDialog;
    private SessionManager session;

    ListView list;
    SwipeRefreshLayout swipe;
    ArrayList<NewsData> dataNya = new ArrayList<NewsData>();

    private static final String TAG = MainActivity.class.getSimpleName();

    //private static String url_list 	 = Config_URL.VIEW_ALLJURNAL +"read.php?offset=";

    private int offSet = 0;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    int no;

    FloatingActionButton fab,fab1,fab2;
    private Boolean isFabOpen = false;

    NewsAdapter adapter;
    Handler handler;
    Runnable runnable;

    TextView idText;
    String idnya ="iduser";
   // String idakses = idText.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.array_list);
        dataNya.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, ViewPostActivity.class);
                intent.putExtra("idartikel", dataNya.get(position).getId());
                intent.putExtra("publisher", dataNya.get(position).getPublisher());
                startActivity(intent);
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        //floating button for add post
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        // session manager
        session = new SessionManager(getApplicationContext());

        //session tidak skip
        if (!session.isSkip()){
            Intent ipre = new Intent(getApplicationContext(), PreloginActivity.class);
            startActivity(ipre);
            finish();
        }

        //user isSkip
        userIsSkip();

        //Session Login
        if(session.isLoggedIn()){
            fab.setEnabled(true);
            fab1.setEnabled(true);
            fab2.setEnabled(true);
            fab.setVisibility(View.VISIBLE);
            aksesUserLogin();
            //getJSON();
        }else {
            aksesUserBlmLogin(0,"-");
        }

        //adapter = new NewsAdapter(MainActivity.this, newsList);
        //list.setAdapter(adapter);
        adapter = new NewsAdapter(MainActivity.this, dataNya);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //drawer jika Login
        if(session.isLoggedIn()) {
            if (id == R.id.nav_my_jurnal) {

                SharedPreferences prefs = getSharedPreferences("myjurnal",
                        Context.MODE_PRIVATE);
                final String idusernya   = prefs.getString("iduser", "");
                Intent myjurnal = new Intent(MainActivity.this, MyJurnalActivity.class);
                myjurnal.putExtra("iduser",idusernya);
                startActivity(myjurnal);
                finish();
            } else if (id == R.id.nav_topik) {

            } else if (id == R.id.nav_artikel) {
                Intent artikel = new Intent(MainActivity.this, ArtikelActivity.class);
                startActivity(artikel);
                finish();
            }else if (id == R.id.nav_jurnal) {
                Intent artikel = new Intent(MainActivity.this, JurnalActivity.class);
                startActivity(artikel);
                finish();
            } else if (id == R.id.nav_katalog) {
                Intent katalog = new Intent(MainActivity.this, KatalogActivity.class);
                startActivity(katalog);
                finish();
            } else if (id == R.id.nav_galeri) {
                Intent galeri = new Intent(MainActivity.this, GaleriActivity.class);
                startActivity(galeri);
                finish();
            } else if (id == R.id.nav_peringkat) {
                Intent peringkat = new Intent(MainActivity.this, PeringkatWarriorsActivity.class);
                startActivity(peringkat);
                finish();
            } else if (id == R.id.logoutnyanih) {
                logoutUser();
            }
        }/*Drawer jika blm login*/else {
            if (id == R.id.logoutnyanih) {
                Intent in = new Intent(MainActivity.this, PreloginActivity.class);
                startActivity(in);
                finish();
            }else if(id == R.id.nav_my_jurnal || id == R.id.nav_peringkat || id == R.id.nav_topik ||
                     id == R.id.nav_artikel || id == R.id.nav_jurnal || id == R.id.nav_katalog || id == R.id.nav_galeri){
                showAuthAlert();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Tambah jurnal
    public void addPost(){

        Intent a = new Intent(MainActivity.this, AddArtikelActivity.class);
        startActivity(a);
        finish();
    }

    //Tambah jurnal
    public void addKatalog(){

        Intent a = new Intent(MainActivity.this, AddCatalogsActivity.class);
        startActivity(a);
        finish();
    }

    //LogOut
    private void logoutUser() {
        session.setLogin(false);
        session.setSkip(false);
        session.setSessid(0);

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, PreloginActivity.class);
        startActivity(intent);
        finish();
    }

    //drawer user blm login
    private void aksesUserBlmLogin(int state, String poin){

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ImageView profic              = (ImageView)header.findViewById(R.id.imageProfile);
        TextView nameText             = (TextView)header.findViewById(R.id.nameText);
        TextView emailText            = (TextView)header.findViewById(R.id.emailText);
        TextView pointText            = (TextView)header.findViewById(R.id.pointText);

        nameText.setText(user_name);
        emailText.setText(email_p);
        pointText.setText(poin);

        navigationView.setNavigationItemSelectedListener(this);

        //profic.setImageDrawable(new DrawableWrapper(getResources().getDrawable(R.drawable.btn_login_rounded)));
        MenuItem logItem = navigationView.getMenu().findItem(R.id.logoutnyanih);

        if(state == 0){
            //linearHeader.setVisibility(View.GONE);
            logItem.setTitle("Login");
        }
    }

    //dialog untuk memberi peringatan
    private void showAuthAlert(){
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Peringatan !");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Untuk dapat mengakses layanan ini anda harus Login terlebih dahulu...");
        alertDialog.setPositiveButton("Signin Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(MainActivity.this, PreloginActivity.class);
                MainActivity.this.startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    //drawer user login
    public void aksesUserLogin(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        
        TextView nameText             = (TextView)header.findViewById(R.id.nameText);
        TextView emailText            = (TextView)header.findViewById(R.id.emailText);
        TextView pointText            = (TextView)header.findViewById(R.id.pointText);
        idText            = (TextView)header.findViewById(R.id.iduser);
        //ImageView gambarnya           = (ImageView)header.findViewById(R.id.imageProfile);
        CircularImageView gambarnya = (CircularImageView)header.findViewById(R.id.imageProfile);
// Set Border
        gambarnya.setBorderColor(getResources().getColor(R.color.white));
        gambarnya.setBorderWidth(10);
// Add Shadow with default param
        gambarnya.addShadow();
// or with custom param
        gambarnya.setShadowRadius(20);
        gambarnya.setShadowColor(Color.parseColor("#34495E"));


        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);


        String username = prefs.getString("username","");
        String email    = prefs.getString("email", "");
        String poin     = prefs.getString("poin", "");
        String gambar   = prefs.getString("gambar", "");
        final String idusernya   = prefs.getString("iduser", "");

        String urlfoto = "http://biodiversitywarriors.lskk.ee.itb.ac.id/user/gambar/userProfil/";
        String urlnya = gambar;
        String fotoNya = urlfoto+urlnya;
        //String a = fotoNya;

        if(urlnya.length()>10){
            Picasso.
                    with(this).
                    load(fotoNya).
                    into(gambarnya);
        }else{
            Picasso.
                    with(this).
                    load(R.drawable.profi).
                    into(gambarnya);
        }

        nameText.setText(username);
        emailText.setText(email);
        pointText.setText(poin);
        idText.setText(idusernya);
        idText.setVisibility(View.INVISIBLE);

        gambarnya.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.
                Intent detail = new Intent(MainActivity.this, DetailProfile.class);
                detail.putExtra("iduser",idusernya);
                startActivity(detail);
                finish();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onRefresh() {
        dataNya.clear();
        adapter.notifyDataSetChanged();
        callNews();
    }

    //News Artikel
    private void callNews(){

        swipe.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(Config_URL.VIEW_ALLJURNAL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    no              = obj.getInt("no");
                                    String id       = obj.getString("idartikel");
                                    String judul    = obj.getString("judul");
                                    String gambar   = obj.getString("thumbnail");
                                    String tgl      = obj.getString("tglpublish");
                                    String like     = obj.getString("likes");
                                    String namadpn  = obj.getString("namadpn");
                                    String namablkg = obj.getString("namablkg");
                                    String kategori = obj.getString("kategori");

                                    dataNya.add(new NewsData(id,judul,gambar,tgl,like,namadpn,namablkg, kategori));

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
                Toast.makeText(MainActivity.this, "Please Check Your Network Connection Or Refresh This Activity", Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                addKatalog();
                break;
            case R.id.fab2:
                addPost();
                break;
        }
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    public void userIsSkip(){
        //Session Skip
        if(session.isSkip()){
            fab.setEnabled(false);
            fab.setVisibility(View.INVISIBLE);
        }
    }
}