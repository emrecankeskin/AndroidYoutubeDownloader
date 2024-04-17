package com.emre.youtubedownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import com.emre.youtubedownloader.Fragment.BrowserFragment;
import com.emre.youtubedownloader.Fragment.ConnectionFragment;
import com.emre.youtubedownloader.Fragment.DownloadFragment;
import com.emre.youtubedownloader.Fragment.FolderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity  {

    public File tempFile;
    public String[] permissions = {
            "android.permission.INTERNET",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.MANAGE_EXTERNAL_STORAGE",
            "android.permission.MANAGE_MEDIA"
    };

    public static String youtubeUrl;
    public String FILE_PATH;
    public String TEMP_PATH;



    public final Pattern patYouTubePageLink = Pattern.compile("(http|https)://(www\\.|m.|)youtube\\.com/watch\\?v=(.+?)( |\\z|&)");
    public Matcher matcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FILE_PATH = getFilesDir().getAbsolutePath();
        TEMP_PATH = getFilesDir().getAbsolutePath()+"/temp";
        System.out.println("Main Activity"+TEMP_PATH);
        tempFile = new File(TEMP_PATH);

        if(!tempFile.exists()){
            tempFile.mkdirs();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,80);
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new BrowserFragment()).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if(item.getItemId() == R.id.youtubeBrowser){
                    fragment = new BrowserFragment();
                    System.out.println("EXTERNAL STORAGE -> "+FILE_PATH);
                }else if(item.getItemId() == R.id.filesItem){
                    fragment = new FolderFragment();
                }else if(item.getItemId() == R.id.downloadButton){
                    /*
                     * Check if there is an internet connection and
                     * the youtube url is valid
                     * */
                    matcher = patYouTubePageLink.matcher(youtubeUrl);
                    if(matcher.find()){
                        fragment = new DownloadFragment();
                    }else{
                        fragment = new ConnectionFragment();
                    }

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
                return true;
            }
        });
    }




}