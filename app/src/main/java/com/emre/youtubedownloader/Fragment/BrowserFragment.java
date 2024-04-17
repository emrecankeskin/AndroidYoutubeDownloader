package com.emre.youtubedownloader.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.emre.youtubedownloader.MainActivity;
import com.emre.youtubedownloader.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String url = null;

    public WebView webView;
    public View view;
    public ImageView backButton;
    public ImageView forwardButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrowserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowserFragment newInstance(String param1, String param2) {
        BrowserFragment fragment = new BrowserFragment();
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

        //Button Initialization




    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.browser_fragment, container, false);
        url = "https://www.youtube.com";
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onLoadResource(WebView  view, String  url1){
                MainActivity.youtubeUrl=view.getUrl();
            }
            @Override
            public void onPageFinished( WebView view, String url ) {

                super.onPageFinished(webView, url );
                if(webView != null){
                    webView.canGoBack();
                    webView.canGoForward();
                    //Make Enable or Disable buttons
                }
            }

        });
        backButton = view.findViewById(R.id.backButton);
        forwardButton = view.findViewById(R.id.forwardButton);

        //Back Button Action
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Going back if canGoBack true
                if(webView.canGoBack()){
                    webView.goBack();
                }
            }
        });
        //Forward Button Action
        forwardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(webView.canGoForward()){
                    webView.goForward();
                }
            }
        });
        webView.loadUrl(url);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
        webView = null;
        backButton = null;
        forwardButton = null;
        url = null;
    }

    public String getUrl(){
        return this.url;
    }
}