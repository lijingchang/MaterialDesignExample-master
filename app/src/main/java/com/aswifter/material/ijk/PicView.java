package com.aswifter.material.ijk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/29.
 */

public class PicView extends View {

    private String Path;
    private ColorMatrix colorMatrix ;
    private float[] colorfilter=new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0};
    private Bitmap bitmap;
    private Paint picPaint;
    private boolean saveit=false;
    private Matrix matrix;


    public PicView(Context C){
        super(C);
        // Other setup code you want here
    }

    public PicView(Context C, AttributeSet attribs){
        super(C, attribs);
        // Other setup code you want here
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(colorMatrix!=null){
            colorMatrix.reset();
            colorMatrix.set(colorfilter);}
        else
            colorMatrix=new ColorMatrix(colorfilter);

        if(picPaint==null) {
            picPaint = new Paint();
            picPaint.setColor(Color.WHITE);
            picPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            picPaint.setAntiAlias(true);
            picPaint.setFilterBitmap(true);
            picPaint.setDither(true);
        }else
            picPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));


        if(Path!=null) {
            if(bitmap==null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(Path,options);}
            if(bitmap!=null){
                if(matrix==null) {
                    matrix = new Matrix();
                    float vw = this.getWidth();
                    float vh = this.getHeight();
                    float bw = (float) bitmap.getWidth();
                    float bh = (float) bitmap.getHeight();
                    float s1x = vw / bw;
                    float s1y = vh / bh;
                    float s1 = (s1x < s1y) ? s1x : s1y;
                    matrix.postScale(s1, s1);
                }
                canvas.drawBitmap(bitmap,matrix,picPaint);
                if(saveit){
                    saveit=false;
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdCard.getAbsolutePath() + "/picfliter");
                    if(!dir.exists())
                        dir.mkdir();

                    try {
                        Bitmap bmOverlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                        Canvas savecanvas = new Canvas(bmOverlay);
                        savecanvas.drawBitmap(bitmap,0,0,picPaint);
                        String fileName = String.format("%d.jpg", System.currentTimeMillis());
                        File next = new File(dir, fileName);
                        FileOutputStream outStream = new FileOutputStream(next);
                        bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        outStream.flush();
                        outStream.close();
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        mediaScanIntent.setData(Uri.fromFile(next));
                        getContext().sendBroadcast(mediaScanIntent);
                        Toast.makeText(getContext(),"save successful"+next.toString(),Toast.LENGTH_SHORT).show();

                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            }
        }
    }

    public void savepic(){
        saveit=true;
    }

    public void changeMatrix(int pos,float data){
        colorfilter[pos]=data;
    }

    public void setPicSource(String path){
        Path=path;
    }

}
