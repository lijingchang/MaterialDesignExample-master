package com.aswifter.material.ijk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aswifter.material.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/28.
 */

public class MusicAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Map<String, Object>> list;
    private int nowplay=0;
    public MusicAdapter(Context context,List<Map<String, Object>> data){
        this.list = data;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        final ViewHolder holder;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = inflater.inflate(R.layout.audio_detail, null);
            holder.top1 = (TextView) arg1.findViewById(R.id.top1);
            holder.top2 = (TextView) arg1.findViewById(R.id.top2);
            holder.top3 = (TextView) arg1.findViewById(R.id.top3);
            holder.down1 = (TextView) arg1.findViewById(R.id.down1);
            holder.down2 = (TextView) arg1.findViewById(R.id.down2);
            holder.down3 = (TextView) arg1.findViewById(R.id.down3);
            holder.down4 = (TextView) arg1.findViewById(R.id.down4);
            holder.isplay=(ImageView)arg1.findViewById(R.id.isplay);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }

        holder.top1.setText(list.get(arg0).get("name") + "");
        holder.top2.setText(list.get(arg0).get("videotype") + "");
        holder.top3.setText(list.get(arg0).get("size") + "");
        holder.down1.setText(list.get(arg0).get("length") + "");
        holder.down2.setText(list.get(arg0).get("video_format") + "");
        holder.down3.setText(list.get(arg0).get("hw") + "");
        holder.down4.setText(list.get(arg0).get("video_fps") + "");


        if(arg0==nowplay)
            holder.isplay.setVisibility(View.VISIBLE);
        else
            holder.isplay.setVisibility(View.GONE);

        return arg1;
    }

    public void setpos(int pos){
        nowplay=pos;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public Map<String, Object> getItem(int arg0) {
        if (arg0 >= getCount())
        {
            return null;
        }
        return list.get(arg0);
    }


    class ViewHolder {
        private TextView top1;
        private TextView top2;
        private TextView top3;
        private TextView down1;
        private TextView down2;
        private TextView down3;
        private TextView down4;
        private ImageView isplay;
    }
}
