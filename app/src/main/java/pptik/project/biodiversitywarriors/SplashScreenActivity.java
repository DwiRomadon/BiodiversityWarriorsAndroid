package pptik.project.biodiversitywarriors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import helper.SessionManager;

/**
 * Created by Terminator on 11/10/2016.
 */

public class SplashScreenActivity extends AppCompatActivity {

    SessionManager session;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //session manager
        session = new SessionManager(getApplicationContext());

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
