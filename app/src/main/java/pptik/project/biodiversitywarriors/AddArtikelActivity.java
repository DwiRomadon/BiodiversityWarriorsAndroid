package pptik.project.biodiversitywarriors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import volley.AppController;
import volley.Config_URL;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class AddArtikelActivity extends AppCompatActivity {
    Button btnSubmit;
    EditText edJudul,edIsi;

    Spinner spinKategor,spinSubKatagori;

    private static final String TAG = AddArtikelActivity.class.getSimpleName();

    ImageButton btnPilih;

    String image_Currs = "";
    int success;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    private Uri UrlGambar;
    int foto = 0;

    ImageView setImage;

    private String UPLOAD_JURNAL ="http://biodiversitywarriors.lskk.ee.itb.ac.id/gambar/artikel/thumb/uploadimage.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    private String KEY_SUBKATEGORI = "subkategori";
    private String KEY_KATEGORI = "kategori";
    private String KEY_JUDUL = "judul";
    private String KEY_ISI   = "isi";
    private String KEY_THUMBNAIL = "image";

    //googlemaps
    private static final int PLACE_PICKER_REQUEST = 0;
    private EditText location;
    private Button btnGetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artikel);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Jurnal");

        btnSubmit = (Button) findViewById(R.id.submitPost);
        btnPilih = (ImageButton) findViewById(R.id.btn_pilih);
        btnGetLocation = (Button) findViewById(R.id.getLocation);

        setImage = (ImageView) findViewById(R.id.img_set);

        edJudul = (EditText) findViewById(R.id.judulnya);
        edIsi   = (EditText) findViewById(R.id.isinya);
        location = (EditText) findViewById(R.id.lokasi);

        spinKategor = (Spinner) findViewById(R.id.pilKat);
        spinSubKatagori = (Spinner) findViewById(R.id.pilSubKaT);

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    //intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(AddArtikelActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
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

    private void uploadImage(){
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_JURNAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("v Add", jObj.toString());

                                Toast.makeText(AddArtikelActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                kosong();

                            } else {
                                Toast.makeText(AddArtikelActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(AddArtikelActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage().toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String,String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_THUMBNAIL, getStringImage(bitmap));
                params.put(KEY_JUDUL, edJudul.getText().toString().trim());
                params.put(KEY_KATEGORI, spinKategor.getSelectedItem().toString().trim());
                params.put(KEY_SUBKATEGORI, spinSubKatagori.getSelectedItem().toString().trim());
                params.put(KEY_ISI, edIsi.getText().toString().trim());
                //kembali ke parameters
                Log.d(TAG, ""+params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
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

        setImage = (ImageView) findViewById(R.id.img_set);

        ImageButton tmb_pilih = (ImageButton) findViewById(R.id.btn_pilih);
        tmb_pilih.setOnClickListener(new View.OnClickListener() {
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
        switch (requestCode){
            case PLACE_PICKER_REQUEST:
                    //fungtion maps
                    if (requestCode == PLACE_PICKER_REQUEST) {
                        if(resultCode == RESULT_OK) {

                            Place place = PlacePicker.getPlace(data, this);
                            //final CharSequence name = place.getName();
                            //String name = String.format("Place: %s", place.getName());
                            CharSequence address = place.getAddress();
                            //String attributions = (String) place.getAttributions();
                            /*if (attributions == null) {
                                attributions = "";
                            }*/
                            location.setText(address);
                            Toast.makeText(this, address, Toast.LENGTH_LONG).show();
                        }
                    }
                break;

            case PICK_IMAGE_REQUEST:

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
                        setImage.setImageBitmap(bitmap);
                        Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
            }
    }


    private void kosong(){
        setImage.setImageResource(0);
        edJudul.setText(null);
        edIsi.setText(null);
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
        Intent a = new Intent(AddArtikelActivity.this, MainActivity.class);
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