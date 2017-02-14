package com.aswifter.material;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aswifter.material.widget.Slider;
import com.zys.brokenview.BrokenView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Chenyc on 15/6/27.
 */
public class AboutFragment extends Fragment {

    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window

    private Camera camera;
    private boolean flashison=false;
    private Button flash;
    private boolean hasflash=true;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);

        cResolver = getActivity().getContentResolver();
        flash=(Button)view.findViewById(R.id.flash);

        if( !getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            hasflash=false;

        try{
            camera = Camera.open();
        } catch (RuntimeException ex){
        }
  //      window = getActivity().getWindow();



        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasflash) {
                    if (flashison) {
                        flash.setText(R.string.default_flash);
                        shutdown(camera);
                    } else {
                        flash.setText(R.string.default_flashon);
                        startfl(camera);
                    }
                    flashison = !flashison;
                } else
                Toast.makeText(getContext(),"连闪光灯都没有还想扮演上帝!!!",Toast.LENGTH_SHORT).show();
            }});
        return view;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //   Toast.makeText(getApplicationContext(),"nowdate:"+date+";starttime:"+starttime,Toast.LENGTH_SHORT).show();

            switch (msg.what) {
                case 0:
                    break;
                default:break;
            }
        }
    };

   private void startfl(Camera a){
       Camera.Parameters p = a.getParameters();
       p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
       a.setParameters(p);
       a.startPreview();
   }

    private void shutdown(Camera a){
        a.stopPreview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        camera.release();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
