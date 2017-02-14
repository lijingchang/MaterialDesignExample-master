package com.aswifter.material.ijk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.aswifter.material.R;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

/**
 * Created by hzqiujiadi on 16/4/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class VideoPlayerActivity extends Activity {

  //  private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    private VrVideoView vrVideoView;
    private boolean modeis3D=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.videovr);
        vrVideoView=(VrVideoView) findViewById(R.id.vr_video_view);

        vrVideoView.setEventListener(new VrVideoEventListener() {
            /**
             * 视频播放完成回调
             */
            @Override
            public void onCompletion() {
                super.onCompletion();
                /**播放完成后跳转到开始重新播放**/
                end();
            }

            /**
             * 加载每一帧视频的回调
             */
            @Override
            public void onNewFrame() {
                super.onNewFrame();
            }

            /**
             * 点击VR视频回调
             */
            @Override
            public void onClick() {
                super.onClick();
            }

            /**
             * 加载VR视频失败回调
             * @param errorMessage
             */
            @Override
            public void onLoadError(String errorMessage) {
                super.onLoadError(errorMessage);
            }

            /**
             * 加载VR视频成功回调
             */
            @Override
            public void onLoadSuccess() {
                super.onLoadSuccess();
            }

            /**
             * 显示模式改变回调
             * 1.默认
             * 2.全屏模式
             * 3.VR观看模式，即横屏分屏模式
             * @param newDisplayMode 模式
             */
            @Override
            public void onDisplayModeChanged(int newDisplayMode) {
                super.onDisplayModeChanged(newDisplayMode);
            }
        });

        Intent i = getIntent();
        modeis3D=i.getBooleanExtra("mode",false);
        Uri uri = i.getData();
        if (uri != null){
            VrVideoView.Options options = new VrVideoView.Options();
            if(modeis3D)
            options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;
            else
                options.inputType = VrVideoView.Options.TYPE_MONO;
            try {
                /**加载VR视频**/
                vrVideoView.loadVideo(uri,options);
                vrVideoView.playVideo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        vrVideoView.pauseVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vrVideoView.pauseVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vrVideoView.playVideo();
    }

    private void end(){
        Log.i("rrrrrrr","shutdown!!");
        Intent intent=getIntent();
        setResult(12, intent);
        this.finish();
    }

}
