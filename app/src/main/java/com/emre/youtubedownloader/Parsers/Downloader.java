package com.emre.youtubedownloader.Parsers;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;


public class Downloader {

    HttpsURLConnection urlConnection;
    URL url;

    FileOutputStream fileOutputStream;
    FileInputStream fileInputStream;
    BufferedInputStream bis;
    BufferedOutputStream bos;



    public String fileName;
    public String PATH;

    public int fileLength;
    public final int BUFFER_SIZE = 8*8*1024;

    public String TEMP_PATH;
    public File tempFile;


    public Downloader(String path){
        this.PATH = path;
        this.TEMP_PATH = this.PATH +"/temp";
    }

    //need to fix file path
    public void downloadFile(String urlName,String fileName) {
        try{
            url = new URL(urlName);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept-Encoding", "identity");
            urlConnection.connect();

            fileOutputStream = new FileOutputStream(TEMP_PATH+"/"+fileName);
            System.out.println(TEMP_PATH+"/"+fileName+"[DOWNlOADER]");
            bis = new BufferedInputStream(urlConnection.getInputStream());
            bos = new BufferedOutputStream(fileOutputStream);

            byte[] bytes = new byte[BUFFER_SIZE];
            int count;

            while((count = bis.read(bytes,0,16368)) != -1){

                bos.write(bytes,0,count);

            }

            bos.close();
            bis.close();
            System.out.println(TEMP_PATH+"/"+fileName);
            fileInputStream = new FileInputStream(TEMP_PATH+"/"+fileName);
            fileOutputStream = new FileOutputStream(PATH+"/"+fileName);

            while((count = fileInputStream.read(bytes,0,16368)) != -1){
                fileOutputStream.write(bytes,0,count);
            }
            fileInputStream.close();
            fileOutputStream.close();
            tempFile = new File(TEMP_PATH+"/"+fileName);
            tempFile.delete();
        }catch (IOException e){
            tempFile = new File(TEMP_PATH+"/"+fileName);
            tempFile.delete();
        }

    }

    public void deleteTemp(){
        tempFile = new File(TEMP_PATH+"/"+fileName);
        tempFile.delete();
    }



    public String getPATH() {
        return PATH;
    }
    public int getFileLength(){
        return fileLength;
    }

    public String getFileName(){
        return this.fileName;
    }
}
