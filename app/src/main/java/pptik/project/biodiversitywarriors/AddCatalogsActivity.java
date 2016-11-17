package pptik.project.biodiversitywarriors;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class AddCatalogsActivity extends AppCompatActivity {

    String pilihanKatKatalog[] = {"-Kategori-","Tumbuhan","Hewan"};

    private Uri UrlGambar;
    private ImageView SetImageView;

    private static final int CAMERA = 1;
    Uri imageUri = null;
    //is FileInputStream
    private static final int FILE = 2;
    String image_Currs = "";
    int foto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catalogs);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Katalog");

        Spinner spinner= (Spinner) findViewById(R.id.pilKatPost);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,pilihanKatKatalog);
        spinner.setAdapter(adapter);

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
                }else if(pilihan ==1){

                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent,"Pilih Aplikasi"),FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();

        SetImageView = (ImageView)findViewById(R.id.img_set);

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
        if (resultCode != RESULT_OK) return;

        Bitmap bitmap = null;
        String path = "";

        if (requestCode == FILE)
        {
            UrlGambar = data.getData();
            path = getRealPath(UrlGambar);

            if (path == null)
            {
                path = UrlGambar.getPath();
            }
            else
            {
                bitmap = BitmapFactory.decodeFile(path);
            }
        }
        else
        {
            path = UrlGambar.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }

        Toast.makeText(this, path,Toast.LENGTH_LONG).show();
        SetImageView.setImageBitmap(bitmap);
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
        Intent a = new Intent(AddCatalogsActivity.this, MainActivity.class);
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
