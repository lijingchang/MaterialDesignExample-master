package com.aswifter.material.ijk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aswifter.material.R;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/1/6.
 */

public class QRFragment extends Fragment {

    private EditText contain;
    private Button make;
    private Button save;
    private ImageView preview;
    private Bitmap myBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr, null);
        contain=(EditText)view.findViewById(R.id.edit_QR);
        make=(Button)view.findViewById(R.id.makeQR);
        save=(Button)view.findViewById(R.id.saveQR);
        preview=(ImageView)view.findViewById(R.id.QR_preview);
        save.setClickable(false);

        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contain.getText().toString().equals(""))
                    Toast.makeText(getContext(),"null",Toast.LENGTH_SHORT).show();
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


        return view;

    }


    private void save(){

        if(myBitmap!=null){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/QRpic");
        if(!dir.exists())
            dir.mkdir();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());

        File next = new File(dir, fileName);
        try {
        FileOutputStream outStream = new FileOutputStream(next);
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(next));
        getContext().sendBroadcast(mediaScanIntent);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        Toast.makeText(getContext(),"save successful"+next.toString(),Toast.LENGTH_SHORT).show();}else
            Toast.makeText(getContext(),"save faliure ,make it first",Toast.LENGTH_SHORT).show();
    }
    private void makepre(){
        save.setClickable(true);
        myBitmap = QRCode.from(contain.getText().toString()).withSize(400,400).withCharset("UTF-8").bitmap();
        preview.setImageBitmap(myBitmap);
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    makepre();
                    break;
                case 1:
                    save();
                    break;
                default:break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
