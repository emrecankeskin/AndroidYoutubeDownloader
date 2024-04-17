package com.emre.youtubedownloader.Parsers;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.io.File;

public class Merger {

    public Merger(String path,String fileName, String videoFile, String audioFile) {
        Movie video;
        Movie audio;
        File vidFile;
        File audFile;

        try {
            System.out.println("");
            video = MovieCreator.build(path+"/"+videoFile);
            audio = MovieCreator.build(path+"/"+audioFile);
            System.out.println(path+audioFile+"[MERGER]");
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            return;
        }

        Movie movie = new Movie();
        try {

            movie.addTrack(new AppendTrack(video.getTracks().get(0)));
            movie.addTrack(new AppendTrack(audio.getTracks().get(0)));
            Container out = new DefaultMp4Builder().build(movie);
            FileChannel fc = new FileOutputStream(path+"/"+fileName).getChannel();
            out.writeContainer(fc);
            fc.close();
            vidFile = new File(path+"/"+videoFile);
            audFile = new File(path+"/"+audioFile);
            vidFile.delete();
            audFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
