package pptik.project.biodiversitywarriors;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import volley.AppController;
import volley.Config_URL;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class DetailProfile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    Spinner jenisKelamin;
    AutoCompleteTextView iduser,namadpn,namabelakang,emailtext,usernametext,biotext,alamattext,nohptext,notelptext,passwordText;
    EditText inputBirthday;
    CircularImageView gambarnya;
    private int year;
    private int month;
    private int day;
    private static final String TAG = ViewPostActivity.class.getSimpleName();
    static final int DATE_PICKER_ID = 1111;

    String tag_json_obj = "json_obj_req";

    String id_user;
    SwipeRefreshLayout swipe;


    private Uri UrlGambar;
    int foto = 0;

    String image_Currs = "";
    int success;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Profil");

        iduser          = (AutoCompleteTextView) findViewById(R.id.iduser);
        namadpn         = (AutoCompleteTextView) findViewById(R.id.namadpn);
        namabelakang    = (AutoCompleteTextView) findViewById(R.id.namaBelakang);
        emailtext       = (AutoCompleteTextView) findViewById(R.id.email);
        usernametext    = (AutoCompleteTextView) findViewById(R.id.username);
        inputBirthday   = (AutoCompleteTextView) findViewById(R.id.birthday);
        jenisKelamin    = (Spinner) findViewById(R.id.jenisKelamin);
        biotext         = (AutoCompleteTextView) findViewById(R.id.bio);
        alamattext      = (AutoCompleteTextView) findViewById(R.id.alamat);
        nohptext        = (AutoCompleteTextView) findViewById(R.id.noHp);
        notelptext      = (AutoCompleteTextView) findViewById(R.id.noTlp);
        passwordText     = (AutoCompleteTextView) findViewById(R.id.password);
        gambarnya       = (CircularImageView)findViewById(R.id.imageProfile);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.pilihanJenisKelamin, R.layout.spinner_item);
        jenisKelamin.setAdapter(adapter);

        id_user = getIntent().getStringExtra("iduser");
        //callDetailNews(id_user);

        //Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        inputBirthday.setFocusable(false);
        inputBirthday.setFocusableInTouchMode(false);
        inputBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id_user.isEmpty()) {
                               callDetailNews(id_user);
                           }
                       }
                   }
        );

        gambarnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //ambil data JSON cui
    private void callDetailNews(final String id){

        swipe.setRefreshing(true);
        StringRequest strReq = new StringRequest(Request.Method.POST, Config_URL.VIEW_DETAIL_PROFIL_USER,
                new Response.Listener<String>() {
                    int position;
                    // NewsData news = getItem(position);
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response " + response.toString());

                        swipe.setRefreshing(false);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String namaDpn   = obj.getString("namadpn");
                            String Gambar    = obj.getString("gambar");
                            String namaBlkg  = obj.getString("namablkg");
                            String email     = obj.getString("email");
                            String username  = obj.getString("username");
                            String tgl       = obj.getString("tgl");
                            String gender    = obj.getString("gender");
                            String bio       = obj.getString("bio");
                            String alamat    = obj.getString("alamat");
                            String noHp      = obj.getString("nohp");
                            String pass      = obj.getString("password");
                            String noTelp    = obj.getString("notlp");

                            String urlfoto = "http://biodiversitywarriors.lskk.ee.itb.ac.id/user/gambar/userProfil/";
                            String fotoNya = urlfoto + Gambar;
                            String urlnya  = Gambar;

                            if(urlnya.length()>10){
                                Picasso.
                                        with(DetailProfile.this).
                                        load(fotoNya).
                                        into(gambarnya);
                            }else {
                                Picasso.
                                        with(DetailProfile.this).
                                        load(R.drawable.profi).
                                        into(gambarnya);
                            }
                            namadpn.setText(namaDpn);
                            namabelakang.setText(namaBlkg);
                            emailtext.setText(email);
                            usernametext.setText(username);
                            inputBirthday.setText(tgl);
                            jenisKelamin.setPrompt(gender);
                            biotext.setText(bio);
                            alamattext.setText(alamat);
                            passwordText.setText(pass);
                            nohptext.setText(noHp);
                            notelptext.setText(noTelp);
                            iduser.setText(id_user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailProfile.this, "Please Check Your Network Connection Or Refresh This Activity", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Detail News Error: " + error.getMessage());
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


    private void showFileChooser() {
        final String [] pilih = new String[]{"Camera", "SD Card"};
        ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,pilih);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Pilih Gambar");
        builder.setAdapter(arr_adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pilihan) {
                if(pilihan == 0){
                    String random = String.valueOf(System.currentTimeMillis());

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),
                            "DCIM/Camera/img_" + random + ".jpg");

                    image_Currs = "img_" + random.toString() + ".jpg";
                    foto++;

                    UrlGambar = Uri.fromFile(file);

                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, UrlGambar);
                        intent.putExtra("return-data",true);

                        startActivityForResult(intent, CAMERA);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dialog.cancel();
                }else if(pilihan == 1){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });

        final AlertDialog dialog = builder.create();

        gambarnya       = (CircularImageView)findViewById(R.id.imageProfile);

        CircularImageView gambar  = (CircularImageView) findViewById(R.id.imageProfile);
        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //fungsion image
        String path = "";

        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    UrlGambar = data.getData();
                    path = getRealPath(UrlGambar);
                    if(path == null){
                        path = UrlGambar.getPath();
                    }else{
                        bitmap = BitmapFactory.decodeFile(path);
                    }

                }else {
                    path = UrlGambar.getPath();
                    bitmap = BitmapFactory.decodeFile(path);
                }

                try {
                    //mengambil fambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), UrlGambar);
                    //menampilkan gambar yang dipilih dari gallery ke ImageView
                    gambarnya.setImageBitmap(bitmap);
                    Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    public String getRealPath(Uri contentUri) {
        String path = null;
        String[] images_data = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, images_data, null, null, null);
        if(cursor.moveToFirst())
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(DetailProfile.this, MainActivity.class);
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth+1;
            day   = selectedDay;
            inputBirthday.setText(year+"-"+month+"-"+day);
        }
    };

    @Override
    public void onRefresh() {
        callDetailNews(id_user);
    }
}
