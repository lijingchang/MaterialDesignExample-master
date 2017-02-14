package com.aswifter.material.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aswifter.material.R;

import us.feras.mdv.MarkdownView;

/**
 * Created by Administrator on 2016/12/7.
 */
public class NoteBookItem extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    private EditText markdownEditText;
    private MarkdownView markdownView;

    public static NoteBookItem newInstance(int position) {
        NoteBookItem f = new NoteBookItem();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_notebook, container, false);
        markdownEditText = (EditText) view.findViewById(R.id.markdownText);
        markdownView = (MarkdownView) view.findViewById(R.id.markdownView);
        SharedPreferences userInfo = getContext().getSharedPreferences("user_info", 0);
        String note = userInfo.getString("note"+position, "##MarkDown Note "+position);
        markdownEditText.setText(note);
        updateMarkdownView();
        markdownEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateMarkdownView();
            }
        });
        return view;
    }

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
        SharedPreferences userInfo = getContext().getSharedPreferences("user_info", 0);
        userInfo.edit().putString("note"+position, markdownEditText.getText().toString()).commit();
        userInfo.edit().putInt("notepage", position).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateMarkdownView() {
        markdownView.loadMarkdown(markdownEditText.getText().toString());
    }
}
