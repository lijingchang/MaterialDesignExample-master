package com.aswifter.material.ijk;

/**
 * Created by Administrator on 2016/12/29.
 */
/*
Copyright 2012 Aphid Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.R;
import com.aswifter.material.widget.ButtonRectangle;
import com.aswifter.material.widget.Slider;

import java.io.InputStream;
import java.math.BigDecimal;

import static java.security.AccessController.getContext;


public class PicFliterActivity extends Activity  {

    private ButtonRectangle picchos;
    private static int PICK_IMAGE=99;
    private FrameLayout contain;
    private Button save;
    private PicView picmanger;
    private Button add;
 //   private Button savefliter;
    private LinearLayout pickmatrix;
    private Slider R1;
    private Slider R2;
    private Slider R3;
    private Slider G1;
    private Slider G2;
    private Slider G3;
    private Slider B1;
    private Slider B2;
    private Slider B3;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picfliter);
        picchos=(ButtonRectangle)findViewById(R.id.choosepic);
        contain=(FrameLayout)findViewById(R.id.container);
        save=(Button)findViewById(R.id.save);
   //     savefliter=(Button)findViewById(R.id.savefliter);
        picmanger=(PicView)findViewById(R.id.picmanger);
        add=(Button)findViewById(R.id.addfliter);
        pickmatrix=(LinearLayout)findViewById(R.id.matrixselect);
        R1=(Slider)findViewById(R.id.red1);
        R2=(Slider)findViewById(R.id.red2);
        R3=(Slider)findViewById(R.id.red3);
        G1=(Slider)findViewById(R.id.green1);
        G2=(Slider)findViewById(R.id.green2);
        G3=(Slider)findViewById(R.id.green3);
        B1=(Slider)findViewById(R.id.blue1);
        B2=(Slider)findViewById(R.id.blue2);
        B3=(Slider)findViewById(R.id.blue3);


        R1.setValue(60);
        G1.setValue(50);
        B1.setValue(50);
        R2.setValue(50);
        G2.setValue(60);
        B2.setValue(50);
        R3.setValue(50);
        G3.setValue(50);
        B3.setValue(60);

        R1.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(0,r1);
                        picmanger.invalidate();
                    }
                }
        );
        R2.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(1,r1);
                        picmanger.invalidate();
                    }
                }
        );
        R3.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(2,r1);
                        picmanger.invalidate();
                    }
                }
        );
        G1.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(5,r1);
                        picmanger.invalidate();
                    }
                }
        );
        G2.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(6,r1);
                        picmanger.invalidate();
                    }
                }
        );
        G3.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(7,r1);
                        picmanger.invalidate();
                    }
                }
        );
        B1.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(10,r1);
                        picmanger.invalidate();
                    }
                }
        );
        B2.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(11,r1);
                        picmanger.invalidate();
                    }
                }
        );
        B3.setOnValueChangedListener(
                new Slider.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(int value) {
                        float r1=(float)div((double)(value-50),(double)10,4);
                        picmanger.changeMatrix(12,r1);
                        picmanger.invalidate();
                    }
                }
        );


        picchos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }});

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picmanger.savepic();
                picmanger.invalidate();
            }
        });

     //   savefliter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                savefliter();
//            }
//        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pickmatrix.getVisibility()==View.INVISIBLE)
                {add.setText(R.string.button_removefliter);
                pickmatrix.setVisibility(View.VISIBLE);
                }
                else{add.setText(R.string.button_addfliter);
                    pickmatrix.setVisibility(View.INVISIBLE);
                }
            }});
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void savefliter(){
        SharedPreferences userInfo = getSharedPreferences("user_info", 0);
        int note = userInfo.getInt("flitersum", 0);
        String data=R1.getValue()+";"+R2.getValue()+";"+R3.getValue()+";"+G1.getValue()+";"+G2.getValue()+";"+G3.getValue()+";"+B1.getValue()+";"+B2.getValue()+";"+B3.getValue();
        userInfo.edit().putString("fliter"+(note+1),data).commit();
        userInfo.edit().putInt("flitersum",note+1).commit();
        Toast.makeText(this,"save fliter"+(note+1)+" successful",Toast.LENGTH_SHORT);
    }

    private void usein(int pos){
        SharedPreferences userInfo = getSharedPreferences("user_info", 0);
        int note = userInfo.getInt("flitersum", 0);
        if(pos<note){
            String data=userInfo.getString("fliter"+pos,"");
            if(data.contains(";")){
            String[] color=data.split(";");
                int len=color.length;
            for(int i=0;i<len;++i){
                switch (i){
                    case 0:
                        R1.setValue(Integer.parseInt(color[i]));
                        break;
                    case 1:
                        R2.setValue(Integer.parseInt(color[i]));
                        break;
                    case 2:
                        R3.setValue(Integer.parseInt(color[i]));
                        break;
                    case 3:
                        G1.setValue(Integer.parseInt(color[i]));
                        break;
                    case 4:
                        G2.setValue(Integer.parseInt(color[i]));
                        break;
                    case 5:
                        G3.setValue(Integer.parseInt(color[i]));
                        break;
                    case 6:
                        B1.setValue(Integer.parseInt(color[i]));
                        break;
                    case 7:
                        B2.setValue(Integer.parseInt(color[i]));
                        break;
                    case 8:
                        B3.setValue(Integer.parseInt(color[i]));
                        break;
                }
                float r1=(float)div((double)(Integer.parseInt(color[i])-50),(double)10,4);
                picmanger.changeMatrix(i+i/3*2,r1);
            }
            picmanger.invalidate();
        }}
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                    cursor.close();
                    picchos.setVisibility(View.GONE);
                    contain.setVisibility(View.VISIBLE);
                    picmanger.setPicSource(picturePath);
                    picmanger.invalidate();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}

