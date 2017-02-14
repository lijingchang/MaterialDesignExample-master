package com.aswifter.material.example;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aswifter.material.R;
import com.aswifter.material.widget.ButtonRectangle;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

/**
 * Created by chenyc on 2015/6/25.
 */
public class EditTextFLActivity extends AppCompatActivity {
    private EditText user;
    private ButtonRectangle login;

    private BrokenView test;
    private BrokenTouchListener listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittext_fl);

        test = BrokenView.add2Window(this);
        listen = new BrokenTouchListener.Builder(test).build();

        user=(EditText) this.findViewById(R.id.edit_user);
        login=(ButtonRectangle) this.findViewById(R.id.btnLogin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((LinearLayout)findViewById(R.id.testforbroken)).setOnTouchListener(listen);

        login.setTextsize((float) 18);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user.getText().toString().isEmpty()) {
                    SharedPreferences userInfo = getSharedPreferences("user_info", 0);
                    userInfo.edit().putString("user", user.getText().toString()).commit();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", user.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                }else{
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
    }
}
