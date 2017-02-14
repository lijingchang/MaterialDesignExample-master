package com.aswifter.material.ijk;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
public class VideoFragment extends Fragment {

    private ListView _listviewlar;
    private  Vector<String> _movielist = new Vector<String>();
    private List<Map<String, Object>>  list;
    private Dialog videochoos;
    private int pos;
    private SimpleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void videochoosedialog(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View logindialog = inflater.inflate(R.layout.video_open, null);
        ((TextView)logindialog.findViewById(R.id.video2d)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videochoos!=null)
                    videochoos.dismiss();
                GiraffePlayerActivity.configPlayer(getActivity()).setTitle(_movielist.get(pos)).play(_movielist.get(pos));
            }
        });
        ((TextView)logindialog.findViewById(R.id.videovr2d)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videochoos!=null)
                    videochoos.dismiss();
                Intent i = new Intent(getContext(),VideoPlayerActivity.class);
                i.setData(Uri.parse(_movielist.get(pos)));
                i.putExtra("mode",false);
                startActivity(i);
            }
        });
        ((TextView)logindialog.findViewById(R.id.videovr3d)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videochoos!=null)
                    videochoos.dismiss();
                Intent i = new Intent(getContext(),VideoPlayerActivity.class);
                i.setData(Uri.parse(_movielist.get(pos)));
                i.putExtra("mode",true);
                startActivity(i);
            }
        });
        videochoos=new Dialog(getContext());
        videochoos.setContentView(logindialog);
        videochoos.setTitle("请选择");
        videochoos.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView=inflater.inflate(R.layout.list_video,null);
        _listviewlar=(ListView)rootView.findViewById(R.id.listview);
        _listviewlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long row) {
                pos=(int) row;
                videochoosedialog();
            }
        });



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
                                adapter.notifyDataSetChanged();
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

        if(Build.VERSION.SDK_INT>=23){
            if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
                new LongOperation().execute("");}else
            new LongOperation().execute("");
        return rootView;
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            _movielist.clear();
           list = new ArrayList<Map<String, Object>>();
            Cursor cursor = getContext().getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                    map.put("videotype", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));

                    int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    String daxiao;
                    if (size > 1024 * 1024 * 1024) {
                        daxiao = size / (1024 * 1024 * 1024) + "G";
                    } else {
                        daxiao = size / (1024 * 1024) + "M";
                    }
                    map.put("size", daxiao);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                    String str = formatter.format(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)));
                    map.put("video_fps", str);
                    map.put("video_format", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)));
                    map.put("length", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.LATITUDE)));
                    map.put("start", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)));
                    map.put("hw", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                    list.add(map);
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    Log.i("rrrrrrr", path);
                    _movielist.add(path);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
            return "Executed";
        }
    }

        private Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        adapter = new SimpleAdapter(getContext(), list, R.layout.video_detail,
                                new String[]{"name", "videotype", "size", "length", "video_format", "hw", "video_fps"},
                                new int[]{R.id.top1, R.id.top2, R.id.top3, R.id.down1, R.id.down2, R.id.down3, R.id.down4});
                        _listviewlar.setAdapter(adapter);
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
