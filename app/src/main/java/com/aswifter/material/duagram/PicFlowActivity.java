package com.aswifter.material.duagram;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aswifter.material.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Administrator on 2017/2/10.
 */

public class PicFlowActivity extends AppCompatActivity {

    private  VoronoiView FillView;
    private Button make;
    private Button save;
    private EditText contain;
    private int count=0;
    private int choosindex=0;
    private LayoutInflater layoutInflater;
    private static int PICK_IMAGE=20;
    private ScrollView piccontain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_regions);
        FillView=(VoronoiView) findViewById(R.id.voronoi);
        make=(Button)findViewById(R.id.makepic);
        save=(Button)findViewById(R.id.savepic);
        contain=(EditText)findViewById(R.id.edit_number);
        piccontain=(ScrollView)findViewById(R.id.piccontain);
        layoutInflater = getLayoutInflater();

        piccontain.setDrawingCacheEnabled(true);
        piccontain.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        for (int i = 0; i < 30; i++) {
            View view = layoutInflater.inflate(R.layout.item_voronoi_2, null, false);
            FillView.addView(view);
            ImageView layout = (ImageView)view.findViewById(R.id.picfill);
            int randomColor = randomColor();
            layout.setBackgroundColor(randomColor);
        }

        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contain.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(),"null",Toast.LENGTH_SHORT).show();
                else{
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        });


        FillView.setOnRegionClickListener(new VoronoiView.OnRegionClickListener() {
            @Override
            public void onClick(View view, int position) {
                choosindex=position;
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    makepre();
                    break;
                case 1:
                    savepic();
                    break;
                default:break;
            }
        }
    };

    private int randomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return Color.rgb(r,g,b);
    }


    private void makepre(){
        FillView.removeAllViews();
        count=Integer.parseInt(contain.getText().toString());
        if(count>=50)
        {
            Toast.makeText(getBaseContext(),"数量限制<50",Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < count; i++) {
            View view = layoutInflater.inflate(R.layout.item_voronoi_2, null, false);
            FillView.addView(view);
            ImageView layout = (ImageView)view.findViewById(R.id.picfill);
            int randomColor = randomColor();
            layout.setBackgroundColor(randomColor);
        }
        FillView.refresh();
    }

    private void savepic(){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/pintu");
        if(!dir.exists())
            dir.mkdir();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File next = new File(dir, fileName);
        try {
            FileOutputStream outStream = new FileOutputStream(next);
            Bitmap bmOverlay = piccontain.getDrawingCache(true);
            bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(next));
            sendBroadcast(mediaScanIntent);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        Toast.makeText(this,"save successful"+next.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE) {
            if(resultCode == Activity.RESULT_OK){
                if (data == null) {
                    //Display an error
                    return;
                }else{
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    FillView.removeViewAt(choosindex);
                    View view = layoutInflater.inflate(R.layout.item_voronoi_2, null, false);
                    FillView.addView(view,choosindex);
                    ImageView layout = (ImageView)view.findViewById(R.id.picfill);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath,options);
                    layout.setImageBitmap(bitmap);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
