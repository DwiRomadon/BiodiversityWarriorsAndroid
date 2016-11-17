package pptik.project.biodiversitywarriors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForceCloseActivity extends AppCompatActivity {

    Button send;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_close);

        send = (Button) findViewById(R.id.btnLogin);

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "dwi.13421054@student.ubl.ac.id");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Report Semut App");
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("error"));

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }
}
