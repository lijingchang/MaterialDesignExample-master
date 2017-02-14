package com.aswifter.material.widget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aswifter.material.R;
import com.aswifter.material.utils.Utils;

public class FlipAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private TextView contain;
	private TextView Page;
	private SharedPreferences userInfo;
	private String booklocat;
	private String MD5="";

	public FlipAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		userInfo = context.getSharedPreferences("user_info", 0);
	}

	public FlipAdapter(Context context,String booklocation) {
		inflater = LayoutInflater.from(context);
		userInfo = context.getSharedPreferences("user_info", 0);
		booklocat=booklocation;
		MD5= Utils.md5(booklocat);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = inflater.inflate(R.layout.fragment_bookcontain, parent, false);
		contain=(TextView)convertView.findViewById(R.id.bookpage);
		Page=(TextView)convertView.findViewById(R.id.page);
		Page.setText("Page:"+position+"/100");

		StringBuilder text = new StringBuilder();
		try {
			InputStreamReader inputStreamReader;
			BufferedReader br;
			String line;

			if(booklocat!=null){
				FileInputStream fin = new FileInputStream(booklocat.substring(7));
				inputStreamReader = new InputStreamReader(fin, "GBK");
				br = new BufferedReader(inputStreamReader);
				br.skip(450 * position);
				while ((line = br.readLine()) != null) {
					if(text.length()<450){
						text.append(line);
						text.append('\n');}
				}
				br.close();
				inputStreamReader.close();
				fin.close();
			}
			else {
				InputStream fin2 = parent.getContext().getResources().openRawResource(R.raw.test1);
				inputStreamReader = new InputStreamReader(fin2, "GBK");
				br = new BufferedReader(inputStreamReader);
				br.skip(450 * position);
				while ((line = br.readLine()) != null) {
					if(text.length()<450){
						text.append(line);
						text.append('\n');}
				}
				br.close();
				inputStreamReader.close();
				fin2.close();
			}
		}
		catch (IOException e) {
			//You'll need to add proper error handling here
		}
		if(booklocat!=null) {
			userInfo.edit().putInt("bookpage" +MD5, position).commit(); ;
		}else
			userInfo.edit().putInt("bookpage", position).commit();

		contain.setText(text);
		return convertView;
	}

}
