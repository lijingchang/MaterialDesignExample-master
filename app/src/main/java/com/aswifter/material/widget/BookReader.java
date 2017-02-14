package com.aswifter.material.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aswifter.material.R;
import com.aswifter.material.flip.FlipViewController;
import com.aswifter.material.utils.Utils;



/**
 * Created by Administrator on 2016/12/7.
 */
public class BookReader extends Fragment {

    private FlipAdapter mAdapter;
    private  FlipViewController flipView;
    private String tag="bookreader";
    private int pagenb;
    private ProgressBarCircularIndeterminate waitebar;
    private String booklocation="";
    private String MD5;

    public BookReader() {
        // Required empty public constructor
    }

    public static BookReader newInstance(String param1, String param2) {
        BookReader fragment = new BookReader();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences userInfo = getContext().getSharedPreferences("user_info", 0);
        if(booklocation.equals(""))
            pagenb = userInfo.getInt("bookpage", 0);
        else
            pagenb = userInfo.getInt("bookpage"+MD5, 0);


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        }catch (InterruptedException e){
                        }

                        if(!booklocation.equals(""))
                            mAdapter = new FlipAdapter(getActivity(),booklocation);
                        else
                            mAdapter = new FlipAdapter(getActivity());
                        mHandler.obtainMessage(1).sendToTarget();
                    }}
        ).start();
    }

    public void setbookurl(String  url){
        booklocation=url;
        MD5= Utils.md5(booklocation);
    }

    private Handler mHandler = new Handler() {
        // 重写handleMessage()方法，此方法在UI线程运行
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    flipView.setAdapter(mAdapter);
                    flipView.setSelection(pagenb);
                    waitebar.setVisibility(View.GONE);
                    break;
                default:break;
            }
        }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookreader, null);
        flipView =  (FlipViewController)view.findViewById(R.id.txtpage);
        waitebar=(ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar);
        return view;
    }
}
