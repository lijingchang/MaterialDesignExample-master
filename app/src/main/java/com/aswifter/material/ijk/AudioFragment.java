package com.aswifter.material.ijk;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.MainActivity;
import com.aswifter.material.R;
import com.aswifter.material.widget.GiraffePlayerActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Administrator on 2016/12/12.
 */
public class AudioFragment extends Fragment  {

//    private ArrayAdapter  _adapterlar;
    private ListView _listviewlar;
    private Vector<String> _movielist = new Vector<String>();
    private List<Map<String, Object>>  list;

    private ImageButton pre;
    private ImageButton play;
    private ImageButton next;

//    private int nowpos=-1;
    private boolean isplaying=false;

    private Music player;
    private boolean mBound=false;

    private Intent playerServiceIntent;
    private MusicAdapter musicadapter;
    private int pos;

    public ServiceConnection playerServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            Music.PlayerBinder playerBinder = (Music.PlayerBinder)service;
            Log.i("rrrrrrr","getbinder!!");
            player = playerBinder.getService();
            mBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound=false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("rrrrrr","start binder");
        playerServiceIntent = new Intent(getActivity(), Music.class);
        getActivity().bindService(playerServiceIntent, playerServiceConnection,  Context.BIND_AUTO_CREATE);
        getActivity().startService(playerServiceIntent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView=inflater.inflate(R.layout.list_video,null);
        ((LinearLayout)rootView.findViewById(R.id.controller)).setVisibility(View.VISIBLE);
        pre=(ImageButton)rootView.findViewById(R.id.preplay) ;
        play=(ImageButton)rootView.findViewById(R.id.play) ;
        next=(ImageButton)rootView.findViewById(R.id.playnext) ;

        play.setClickable(false);
        pre.setClickable(false);
        next.setClickable(false);

        ((TextView)rootView.findViewById(R.id.info)).setText("本地音频列表");
        _listviewlar=(ListView)rootView.findViewById(R.id.listview);
        _listviewlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long row) {
                    player.play((int)row);
                    play.setImageResource(R.drawable.pause);
                    isplaying=true;
            }
        });


        if(Build.VERSION.SDK_INT>=23){
            if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
                new LongOperation().execute("");}else
            new LongOperation().execute("");

        _listviewlar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long row) {
                pos=(int)row;
                new AlertDialog.Builder(getContext())
                        .setTitle("删除本地文件")
                        .setMessage("确定要删除该文件吗?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                File newFile=new File(_movielist.get(pos));
                                if(newFile.exists())
                                    newFile.delete();
                                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                mediaScanIntent.setData(Uri.fromFile(newFile));
                                getContext().sendBroadcast(mediaScanIntent);
                                list.remove(pos);
                                _movielist.remove(pos);
                                musicadapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Log.i("rrrrrrr","no");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isplaying=true;
                play.setImageResource(R.drawable.pause);
                player.previous();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isplaying){
                    player.pause();
                    isplaying=false;
                    play.setImageResource(R.drawable.play);
                }else{
                    play.setImageResource(R.drawable.pause);
                    player.goon();
                    isplaying=true;
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setImageResource(R.drawable.pause);
                player.next();
                isplaying=true;
            }
        });
        return rootView;
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            _movielist.clear();
            list = new ArrayList<Map<String, Object>>();
            Cursor cursor = getContext().getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                    map.put("videotype", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)));

                    int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    String daxiao;
                    if (size > 1024 * 1024 * 1024) {
                        daxiao = size / (1024 * 1024 * 1024) + "G";
                    } else {
                        daxiao = size / (1024 * 1024) + "M";
                    }
                    map.put("size", daxiao);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                    String str = formatter.format(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)));
                    map.put("video_fps", str);
                    map.put("video_format", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)));
                    map.put("length", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    map.put("start", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    map.put("hw", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    map.put("ID", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    list.add(map);
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // ·??
                    _movielist.add(path);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessageDelayed(msg,200);
            }
            return "Executed";
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if((_movielist.size()>0)&&(player!=null)){
                    musicadapter=new MusicAdapter(getContext(),list);
                    _listviewlar.setAdapter(musicadapter);
                        player.setolaylist(_movielist);
                    player.setAdapter(musicadapter);
                    player.showNotification();
                        play.setClickable(true);
                        pre.setClickable(true);
                        next.setClickable(true);}
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(mBound){
            getActivity().unbindService(playerServiceConnection);
            mBound=false;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        getActivity().stopService(playerServiceIntent);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
