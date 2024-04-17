package com.emre.youtubedownloader.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.emre.youtubedownloader.Formats.FileFormat;
import com.emre.youtubedownloader.Fragment.MusicPlayerFragment;
import com.emre.youtubedownloader.R;

import java.io.File;
import java.util.ArrayList;

public class FileAdapter extends ArrayAdapter<FileFormat> {

    String FILE_PATH = getContext().getFilesDir().getAbsolutePath();

    ArrayList<FileFormat> fileList;
    MusicPlayerFragment musicPlayerFragment;
    Context context;

    TextView fileView;
    ImageView optionsButton;
    ImageView playButton;


    String fileName;

    public FileAdapter(@NonNull Context context,@NonNull ArrayList<FileFormat> fileList) {
        super(context, 0,fileList);
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        fileName = fileList.get(position).getFileName();

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.file_item,parent,false);
        optionsButton = convertView.findViewById(R.id.optionsButton);
        playButton = convertView.findViewById(R.id.playButton);

        fileView = convertView.findViewById(R.id.fileName);
        fileView.setSelected(true);
        fileView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        fileView.setSingleLine(true);

        fileView.setText(fileName);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.inflate(R.menu.file_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.delete_item:
                                //pop up thing for asking really want to delete
                                Toast.makeText(context,getItem(position).getFileName(),Toast.LENGTH_LONG).show();
                                //Removing file from adapter and directory
                                File file = new File(getContext().getFilesDir().getAbsolutePath()+"/"+getItem(position).getFileName());

                                remove(getItem(position));
                                if(file.delete()){

                                    Toast.makeText(context,"DELETED",Toast.LENGTH_LONG).show();

                                }
                                break;
                            case R.id.rename_item:
                                Toast.makeText(context,"CLICKED TO RENAME",Toast.LENGTH_LONG).show();
                                //do something
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayerFragment = new MusicPlayerFragment(FILE_PATH+"/"+getItem(position).getFileName());

                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, musicPlayerFragment);
                fragmentTransaction.commit();

            }
        });
        return convertView;

    }

}
