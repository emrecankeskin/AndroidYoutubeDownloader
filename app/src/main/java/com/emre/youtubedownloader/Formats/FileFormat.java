package com.emre.youtubedownloader.Formats;

public class FileFormat {

    String fileName;
    String fileDuration;
    String fileSize;

    public FileFormat(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
