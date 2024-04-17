package com.emre.youtubedownloader.Formats;

public class MediaFormat {

    public String url;
    public String name;
    public String quality;
    public String mimeType;

    public MediaFormat(String quality,String url, String name, String mimeType){
        this.quality = quality;
        this.url = url;
        this.name = name;
        this.mimeType = mimeType;
    }


    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getQuality() {
        return quality;
    }
    public String getMimeType(){return mimeType;}

}
