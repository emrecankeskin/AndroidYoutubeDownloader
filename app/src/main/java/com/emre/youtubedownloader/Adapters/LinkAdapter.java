package com.emre.youtubedownloader.Adapters;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.emre.youtubedownloader.Formats.MediaFormat;
import com.emre.youtubedownloader.MainActivity;
import com.emre.youtubedownloader.Parsers.Downloader;
import com.emre.youtubedownloader.Parsers.Merger;
import com.emre.youtubedownloader.R;

import java.util.ArrayList;

public class LinkAdapter extends ArrayAdapter<MediaFormat> {

    public String APP_FILE_PATH = getContext().getFilesDir().getAbsolutePath();
    public String url;
    public String quality;
    public String CHANNEL_ID ="finish_channel_01";
    public CharSequence name = "finish_channel";
    public String description = "Channel for finished downloads";


    NotificationManager notificationManager;
    NotificationChannel mChannel;
    NotificationCompat.Builder builder;
    Intent intent;
    PendingIntent pendingIntent;
    TaskStackBuilder stackBuilder;

    Downloader downloader;
    MediaFormat audio;
    Merger merger;
    ArrayList<MediaFormat> mediaFormats;

    Context context;
    TextView qualityView;
    TextView typeView;
    Button downloadButton;

    Thread thread;



    public LinkAdapter(@NonNull Context context,  @NonNull ArrayList<MediaFormat> mediaFormats) {
        super(context, 0, mediaFormats);
        this.context = context;
        this.mediaFormats = mediaFormats;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        builder = new NotificationCompat.Builder(context, "my_channel_01")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Downloading...")
                .setContentText("Downloaded : "+getItem(position).getName())
                .setSmallIcon(R.drawable.download_image);

        intent = new Intent(context, MainActivity.class);
        stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);



        url = getItem(position).getUrl();
        quality = getItem(position).getQuality();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        qualityView = convertView.findViewById(R.id.mediaQuality);
        qualityView.setSelected(true);
        qualityView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        qualityView.setSingleLine(true);

        typeView = convertView.findViewById(R.id.mediaType);
        downloadButton = convertView.findViewById(R.id.downloadButton);

        qualityView.setText(quality);
        typeView.setText(getItem(position).getMimeType().substring(0,5));
        
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"STARTED TO DOWNLOAD",Toast.LENGTH_LONG).show();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        downloader = new Downloader(APP_FILE_PATH);
                        System.out.println("CREATED DOWNLOADER");
                        //getItem(position).videoName+".mp4"
                        if(getItem(position).getMimeType().contains("video")){

                            System.out.println("MERGE STARTED");

                            downloader.downloadFile(getItem(position).getUrl(),"video");
                            //just select before the media quality
                            for(int i = 0; i<mediaFormats.size(); i++){

                                if(mediaFormats.get(i).getMimeType().contains("audio")){
                                    System.out.println("ENTERED IF [LINK ADAPTER]");
                                    downloader.downloadFile(mediaFormats.get(i).getUrl()
                                            ,"audio");
                                    merger = new Merger(APP_FILE_PATH,
                                            getItem(position).getName(),
                                            "video",
                                            "audio");
                                    break;
                                }
                            }

                        }else{
                            downloader.downloadFile(getItem(position).getUrl(),getItem(position).getName());
                        }

                        notificationManager.notify(234,builder.build());
                        System.out.println("FINISHED DOWNLOAD");

                    }
                });
                thread.start();
            }
        });

        return convertView;

    }


    @Override
    public void clear() {
        super.clear();
    }

}
