package com.aswifter.material;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.book.BooksFragment;
import com.aswifter.material.duagram.PicFlowActivity;
import com.aswifter.material.example.EditTextFLActivity;
import com.aswifter.material.ijk.AudioFragment;
import com.aswifter.material.ijk.FindMathFragment;
import com.aswifter.material.ijk.PicFliterActivity;
import com.aswifter.material.ijk.QRFragment;
import com.aswifter.material.ijk.VideoFragment;
import com.aswifter.material.news.NewsListActivity;
import com.aswifter.material.utils.DisplayUtil;
import com.aswifter.material.widget.BackHandledFragment;
import com.aswifter.material.widget.BookReader;
import com.aswifter.material.widget.ColorSelector;
import com.aswifter.material.widget.FlipHorizontalLayoutActivity;

public class MainActivity extends AppCompatActivity implements BackHandledFragment.BackHandlerInterface ,ColorSelector.OnColorSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private BackHandledFragment selectedFragment;
    private NavigationView mNavigationView;
    private  TextView user;
    private String bookurl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);
        //profile Image
        setUpProfileImage();

        final Intent intent = getIntent();
        final String action = intent.getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            bookurl=intent.getDataString();
            switchToBookReader();
        }else
        switchToBook();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }



    private void switchToBook() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BooksFragment()).commit();
        mToolbar.setTitle(R.string.navigation_book);
    }

    private void switchToExample() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NewsListActivity()).commit();
        mToolbar.setTitle(R.string.navigation_example);
    }

    private void switchToBlog() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BlogFragment()).commit();
        mToolbar.setTitle(R.string.navigation_my_blog);
    }
    private void switchToNoteBook() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NoteBook()).commit();
        mToolbar.setTitle(R.string.navigation_my_blog);
    }

    private void switchToBookReader() {
        BookReader reader=new BookReader();
        if(!bookurl.equals(""))
            reader.setbookurl(bookurl);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, reader).commit();
        mToolbar.setTitle(R.string.navigation_bookreader);

    }

    private void switchToVideo() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new VideoFragment()).commit();
        mToolbar.setTitle(R.string.navigation_video);
    }

    private void switchToAudio() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AudioFragment()).commit();
        mToolbar.setTitle(R.string.navigation_audio);
    }

    private void switchToCamera() {
        startActivity(new Intent(this,PicFliterActivity.class));
        mToolbar.setTitle(R.string.navigation_camera);
    }

    private void switchTofillpic() {
        startActivity(new Intent(this,PicFlowActivity.class));
        mToolbar.setTitle(R.string.navigation_picfill);
    }

    private void switchToQR() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new QRFragment()).commit();
        mToolbar.setTitle(R.string.navigation_QR);
    }

    private void switchToMath() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FindMathFragment()).commit();
        mToolbar.setTitle(R.string.navigation_function);
    }

    private void switchToAbout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutFragment()).commit();
        mToolbar.setTitle(R.string.navigation_about);
    }


    private void setUpProfileImage() {
        View headerView=  mNavigationView.inflateHeaderView(R.layout.navigation_header);
        View profileView = headerView.findViewById(R.id.profile_image);
        user=(TextView) headerView.findViewById(R.id.user_name);
        SharedPreferences userInfo = getSharedPreferences("user_info", 0);
        String name = userInfo.getString("user", "李xx");
        user.setText(name);
        if (profileView != null) {
            profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), EditTextFLActivity.class);
                    startActivityForResult(intent,99);
                    mDrawerLayout.closeDrawers();
                    mNavigationView.getMenu().getItem(1).setChecked(true);

                }
            });
        }
    }


    @Override
    public void onColorSelected(int color){
        DisplayUtil.colorselect=color;
        mToolbar.setBackgroundColor(color);
        Log.i("rrrrrrr","color:"+color);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.navigation_item_book:
                                switchToBook();
                                break;
                            case R.id.navigation_item_example:
                              //  switchToExample();
                                switchToExample();
                                break;
                            case R.id.navigation_item_notebook:
                                switchToNoteBook();
                                break;
                            case R.id.navigation_item_bookreader:
                                switchToBookReader();
                                break;
                            case R.id.navigation_item_video:
                                switchToVideo();
                                break;
                            case R.id.navigation_item_audio:
                                switchToAudio();
                                break;
                            case R.id.navigation_item_camera:
                                switchToCamera();
                                break;
                            case R.id.navigation_item_picfill:
                                switchTofillpic();
                                break;
                            case R.id.navigation_item_QR:
                                switchToQR();
                                break;
                            case R.id.navigation_item_findmath:
                                switchToMath();
                                break;
                            case R.id.navigation_item_about:
                                switchToAbout();
                                break;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new ColorSelector(MainActivity.this, Color.RED,  MainActivity.this).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        this.selectedFragment = backHandledFragment;
    }


    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 99) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                if((user!=null)&&(!result.isEmpty()))
                user.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                doExitApp();
            }
        }
    }

}
