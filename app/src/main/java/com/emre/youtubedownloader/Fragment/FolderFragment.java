package com.emre.youtubedownloader.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.emre.youtubedownloader.Adapters.FileAdapter;
import com.emre.youtubedownloader.Formats.FileFormat;
import com.emre.youtubedownloader.R;

import java.io.File;
import java.util.ArrayList;


public class FolderFragment extends Fragment {

    public String FOLDER_PATH;

    public View view;
    public FileAdapter fileAdapter;
    public ListView listView;
    public Thread thread;

    ArrayList<FileFormat> fileList = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FolderFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        FOLDER_PATH = getActivity().getFilesDir().getAbsolutePath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.folder_fragment, container, false);
        listView = view.findViewById(R.id.fileView);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(FOLDER_PATH);
                if(!file.exists()){

                    file.mkdirs();
                }
                File[] files = file.listFiles();
                for(int i=0; i<files.length; i++){
                    if(!(files[i].isDirectory()) && !(files[i].getName().equals("video") || files[i].getName().equals("audio"))){
                        fileList.add(new FileFormat(files[i].getName()));
                    }
                }

                getActivity().runOnUiThread(() -> {
                    fileAdapter = new FileAdapter(getActivity(),fileList);
                    listView.setAdapter(fileAdapter);

                });

            }
        });
        thread.start();
        return view;
    }
}