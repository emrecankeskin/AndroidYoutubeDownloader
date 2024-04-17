package com.emrecan.youtubedownloader.Fragments;

import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.emrecan.youtubedownloader.R;




public class VideoPlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Handler handler;
    Uri uri;

    String fileName;
    String endTime;
    String currentTime;
    int DELAY = 1000;

    Button rewindButton;
    Button forwardButton;
    Button playButton;

    TextView textStart;
    TextView textEnd;
    TextView musicName;

    SeekBar seekBar;

    Thread seekBarThread;


    public VideoPlayerFragment(String fileName) {
        // Required empty public constructor
        this.fileName = fileName;
        this.uri = Uri.parse(this.fileName);
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(getContext(),uri);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.music_player_fragment, container, false);
        handler = new Handler();

        rewindButton = view.findViewById(R.id.rewindButton);
        forwardButton = view.findViewById(R.id.forwardButton);
        playButton = view.findViewById(R.id.playButton);

        textStart = view.findViewById(R.id.textStart);
        textEnd = view.findViewById(R.id.textEnd);
        surfaceView = view.findViewById(R.id.surfaceView);

        musicName = view.findViewById(R.id.musicName);
        musicName.setSelected(true);
        musicName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        musicName.setSingleLine(true);
        musicName.setText(this.fileName.substring(49));

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFixedSize(176, 144);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

        });

        seekBar = view.findViewById(R.id.seekBar);
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.teal_700), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.teal_700),PorterDuff.Mode.SRC_IN);
        seekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playButton.setBackgroundResource(R.drawable.pause_image);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    playButton.setBackgroundResource(R.drawable.play_image);
                    mediaPlayer.pause();
                }else{
                    playButton.setBackgroundResource(R.drawable.pause_image);
                    mediaPlayer.start();
                }

            }
        });

        seekBarThread = new Thread(){
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int totalDuration = mediaPlayer.getDuration();
                    int currentPosition = 0;

                    while(currentPosition < totalDuration){
                        try{
                            sleep(500);
                            if(mediaPlayer != null) {
                                currentPosition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currentPosition);
                            }
                        }catch (NullPointerException | InterruptedException  e){
                            e.printStackTrace();
                        }
                    }
                    if(currentPosition == totalDuration){
                        seekBar.setProgress(0);
                    }

                }
            }
        };
        seekBarThread.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        endTime = createTime(mediaPlayer.getDuration());
        textEnd.setText(endTime);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    currentTime = createTime(mediaPlayer.getCurrentPosition());
                    textStart.setText(currentTime);
                    handler.postDelayed(this,DELAY);
                }
            }
        },DELAY);
        mediaPlayer.start();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        //seekBarThread = null;
        seekBar = null;
        //handler = null;

        rewindButton = null;
        forwardButton = null;
        playButton = null;

        textStart = null;
        textEnd = null;
        musicName = null;
    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time = time + min + ":";

        if(sec < 10){
            time = time + "0";
        }
        time = time+sec;

        return time;
    }
}