package com.aswifter.material.ijk;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aswifter.material.R;
import com.aswifter.material.mathviews.ClockView;
import com.aswifter.material.mathviews.MathAdapter;
import com.aswifter.material.mathviews.SinView;
import com.aswifter.material.widget.ProgressBarCircularIndeterminate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FindMathFragment extends Fragment {


    private ViewPager pager;
    private ProgressBarCircularIndeterminate waitebar;
    private List<View> MathViews;
    private MathAdapter adapter;



    // TODO: Rename and change types of parameters


    public FindMathFragment() {
        // Required empty public constructor
    }

    public static FindMathFragment newInstance() {
        FindMathFragment fragment = new FindMathFragment();
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
        MathViews=new ArrayList<View>();
        MathViews.add(inflater.inflate(R.layout.math_sinview, container, false));
        MathViews.add(inflater.inflate(R.layout.math_clockview, container, false));
        pager=(ViewPager)view.findViewById(R.id.pager);
        waitebar=(ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar);
        waitebar.setVisibility(View.GONE);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        adapter = new MathAdapter(MathViews);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0,true);
        return view;
    }




    @Override
    public void onStart() {
        super.onStart();
    }

    private Handler mHandler = new Handler() {
        //重写handleMessage()方法，此方法在UI线程运行
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                default:break;
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
