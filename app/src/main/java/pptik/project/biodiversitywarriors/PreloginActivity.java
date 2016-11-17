package pptik.project.biodiversitywarriors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import Adapter.PreloginAdapter;
import helper.SessionManager;
import net.qiujuer.genius.blur.StackBlur;
public class PreloginActivity extends FragmentActivity {

    private Button toLogin;
    private Button skip;
    private ViewPager preloginPager;
    private PreloginAdapter preAdapter;
    private SessionManager session;

    ImageView image1;
    ImageView image2;
    ImageView image3;
    Integer currentPage;

    View view1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelogin);

        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        preloginPager = (ViewPager) findViewById(R.id.pagerprelogin);
        image1 = (ImageView)findViewById(R.id.imgprelogin1);
        image2 = (ImageView)findViewById(R.id.imgprelogin2);
        image3 = (ImageView)findViewById(R.id.imgprelogin3);

        toLogin = (Button)findViewById(R.id.btnLinkToLogin);
        skip = (Button)findViewById(R.id.btn_skip);

        bindingXML();

        //session manager
        session = new SessionManager(getApplicationContext());

        toLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent ilogin = new Intent(PreloginActivity.this, LoginActivity.class);
                startActivity(ilogin);
                finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                session.setSkip(true);
                Intent ifront = new Intent(PreloginActivity.this, MainActivity.class);
                startActivity(ifront);
                finish();
            }
        });

        preAdapter = new PreloginAdapter(getSupportFragmentManager());

        preloginPager.setAdapter(preAdapter);
        currentPage = preloginPager.getCurrentItem();
        preloginPager.setCurrentItem(currentPage, true);

        final Handler handler = new Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == preAdapter.getCount()) {
                    currentPage = 0;
                }
                preloginPager.setCurrentItem(currentPage++, true);


            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 7000);
        preloginPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    image2.setVisibility(View.GONE);
                    image3.setVisibility(View.GONE);
                    image1.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    image1.setVisibility(View.GONE);
                    image3.setVisibility(View.GONE);
                    image2.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    image2.setVisibility(View.GONE);
                    image1.setVisibility(View.GONE);
                    image3.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*@Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }*/
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    private void bindingXML(){
        Bitmap bg= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.bg_1);
        bg= StackBlur.blurNativelyPixels(bg,8,false);
        view1=(View)findViewById(R.id.opacityFilter);
        view1.setBackground(new BitmapDrawable(bg));

    }

}
