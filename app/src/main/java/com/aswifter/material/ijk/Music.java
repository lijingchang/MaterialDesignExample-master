package com.aswifter.material.ijk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.aswifter.material.MainActivity;
import com.aswifter.material.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Administrator on 2016/12/28.
 */

public class Music extends Service {
    private MediaPlayer mp;
    private IBinder playerBinder=new PlayerBinder();
    private Vector<String> _playlist;
    private int playpos=0;

    private static int stopped=0;
    private static int pause=1;
    private static int isplaying=2;

    private int status=0;

    private MusicAdapter uiadapter;

    private static final String ACTION_PLAY_TOGGLE = "com.aswifter.material.ACTION.PLAY_TOGGLE";
    private static final String ACTION_PLAY_LAST = "com.aswifter.material.ACTION.PLAY_LAST";
    private static final String ACTION_PLAY_NEXT = "com.aswifter.material.ACTION.PLAY_NEXT";
    private static final String ACTION_STOP_SERVICE = "com.aswifter.material.ACTION.STOP_SERVICE";
    private RemoteViews  mContentViewSmall;
    private static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    private Notification mNotification;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("rrrrrrr","receicve binder request!!!");
        return playerBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY_TOGGLE.equals(action)) {
                if (status==isplaying) {
                    pause();
                } else {
                    goon();
                }
            } else if (ACTION_PLAY_NEXT.equals(action)) {
                next();
            } else if (ACTION_PLAY_LAST.equals(action)) {
                previous();
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (status==isplaying) {
                    pause();
                }
                stopForeground(true);
            }
        }
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("rrrrrrr","create music service!!!");
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        mp= new MediaPlayer();
        mp.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next();
                    }
                }
        );
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void showNotification() {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        // Set the info for the views that show in the notification panel.
        mNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)
                .setContent(getSmallContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();
        // Send the notification.
        startForeground(NOTIFICATION_ID, mNotification);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    private RemoteViews getSmallContentView() {
        Log.i("rrrrrrr","getView!!");
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        return mContentViewSmall;
    }

    private void setUpRemoteView(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.closewhite);
        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.platlastwhite);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.nextwhite);
        remoteView.setImageViewResource(R.id.image_view_play_toggle, R.drawable.playwhite);

        remoteView.setTextColor(R.id.tips,getResources().getColor(R.color.white));

        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_PLAY_LAST));
        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_PLAY_TOGGLE));
    }



    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        mp.reset();
        mp.release();
    }

    public void setAdapter(MusicAdapter mu){
        uiadapter=mu;
    }

    public void setolaylist(Vector<String> list){
        _playlist=list;
    }

    public void play(int pos){
        uiadapter.setpos(pos);
        uiadapter.notifyDataSetChanged();
        if(status>0)
            mp.stop();
        mp.reset();
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(_playlist.get(pos)));
            mp.setDataSource(file.getFD());
            mp.prepare();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        playpos=pos;
        mNotification.contentView.setImageViewResource(R.id.image_view_play_toggle,  R.drawable.pausewhite);
        mNotificationManager.notify(NOTIFICATION_ID,mNotification);
        status=isplaying;
    }

    public void next(){
        playpos=(playpos+1)%_playlist.size();
        play(playpos);
        status=isplaying;
    }
    public void previous(){
        playpos=(playpos-1+_playlist.size())%_playlist.size();
        play(playpos);
        status=isplaying;
    }

    public void pause(){
        mp.pause();
        mNotification.contentView.setImageViewResource(R.id.image_view_play_toggle,  R.drawable.playwhite);
        mNotificationManager.notify(NOTIFICATION_ID,mNotification);
        status=pause;
    }

    public void goon(){
        mp.start();
        mNotification.contentView.setImageViewResource(R.id.image_view_play_toggle,  R.drawable.pausewhite);
        mNotificationManager.notify(NOTIFICATION_ID,mNotification);
        status=isplaying;
    }

    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        return super.stopService(name);
    }


    public class PlayerBinder extends Binder {

        public Music getService() {
            return Music.this;
        }
    }
}