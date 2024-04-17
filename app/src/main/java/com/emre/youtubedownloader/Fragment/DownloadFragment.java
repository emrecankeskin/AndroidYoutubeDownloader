package com.emre.youtubedownloader.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.emre.youtubedownloader.Adapters.LinkAdapter;
import com.emre.youtubedownloader.Formats.MediaFormat;
import com.emre.youtubedownloader.MainActivity;
import com.emre.youtubedownloader.Parsers.Decrypter;
import com.emre.youtubedownloader.Parsers.Streams;
import com.emre.youtubedownloader.Parsers.URLDataReceiver;
import com.emre.youtubedownloader.R;

import org.json.JSONException;

import java.util.ArrayList;



/**
 * Bunu video player yap browserın da altına koy indirme linkini
 *
 *
 *
 */

public class DownloadFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String youtubeLink;
    String youtubeUrlSource;
    String baseJsUrl;

    View view;
    ListView listView;
    LinkAdapter linkAdapter;
    Thread thread;

    URLDataReceiver urlDataReceiver;
    Decrypter decrypter;
    Streams streams;


    public ArrayList<MediaFormat> mediaFormatList = new ArrayList<>();


    private String mParam1;
    private String mParam2;

    public DownloadFragment() {
        // Required empty public constructor
    }


    public static DownloadFragment newInstance(String param1, String param2) {
        DownloadFragment fragment = new DownloadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        urlDataReceiver = new URLDataReceiver();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.download_fragment, container, false);
        listView = view.findViewById(R.id.listView);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    youtubeLink = MainActivity.youtubeUrl;
                    youtubeUrlSource = urlDataReceiver.getUrlData(youtubeLink);
                    decrypter = new Decrypter(youtubeUrlSource);
                    baseJsUrl = decrypter.getBaseJs();
                    decrypter.findFunctionTypes(urlDataReceiver.getUrlData(baseJsUrl));
                    streams = new Streams(youtubeUrlSource,decrypter);

                    streams.parseJson();
                    streams.parseAdaptiveVideoFormatList();
                    streams.parseAudioFormatList();

                    mediaFormatList = streams.getMediaFormatList();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("PUTTING ADAPTER TO VIEW");
                            linkAdapter = new LinkAdapter(getActivity(),mediaFormatList);
                            //progressBar.setVisibility(View.INVISIBLE);
                            //textView.setVisibility(View.INVISIBLE);
                            listView.setAdapter(linkAdapter);
                        }
                    });
                }


            }
        });
        thread.start();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listView.setAdapter(null);
        view = null;
        listView = null;
        linkAdapter = null;
        streams = null;
        decrypter = null;
        mediaFormatList = null;
        youtubeLink = null;
        youtubeUrlSource = null;
    }

}