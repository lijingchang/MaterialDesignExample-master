package com.aswifter.material;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.R;
import com.aswifter.material.widget.FlipAdapter;
import com.aswifter.material.widget.NoteBookItem;
import com.aswifter.material.widget.ProgressBarCircularIndeterminate;
import com.google.android.agera.Observable;

import us.feras.mdv.MarkdownView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NoteBook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteBook extends Fragment {


    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ProgressBarCircularIndeterminate waitebar;
    // TODO: Rename and change types of parameters


    public NoteBook() {
        // Required empty public constructor
    }

    public static NoteBook newInstance() {
        NoteBook fragment = new NoteBook();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contain_notebook, container, false);
        pager=(ViewPager)view.findViewById(R.id.pager);
        waitebar=(ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar);
        return view;
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public Fragment getItem(int position) {
            return NoteBookItem.newInstance(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        adapter = new MyPagerAdapter(getFragmentManager());
                        try {
                            Thread.sleep(300);
                        }catch (InterruptedException e){
                        }
                        mHandler.obtainMessage(1).sendToTarget();
                    }}
        ).start();
    }

    private Handler mHandler = new Handler() {
        //重写handleMessage()方法，此方法在UI线程运行
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    pager.setAdapter(adapter);
                    SharedPreferences userInfo = getContext().getSharedPreferences("user_info", 0);
                    int note = userInfo.getInt("notepage", 0);
                    pager.setCurrentItem(note,true);
                    waitebar.setVisibility(View.GONE);
                    break;
                default:break;//12:34:00
            }
        }};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
