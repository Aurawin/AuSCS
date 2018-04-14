package com.aurawin.scs.media;

import com.aurawin.core.file.Writer;
import com.aurawin.core.stream.MemoryStream;
import com.mpatric.mp3agic.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void FileTest() throws IOException, UnsupportedTagException,InvalidDataException {
        MP3Details Deets = new MP3Details();
        Mp3File mp3 = new Mp3File("/Developer/Internal/Java/AuSCS/src/test/resources/1.mp3");

        // todo load and set values
        Deets.Duration = mp3.getLengthInSeconds();
        Deets.Bitrate = mp3.getBitrate();
        Deets.Samplerate = mp3.getSampleRate();

        if (mp3.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3.getId3v1Tag();
            Deets.Track=id3v1Tag.getTrack();
            Deets.Artist= id3v1Tag.getArtist();
            Deets.Title = id3v1Tag.getTitle();
            Deets.Album= id3v1Tag.getAlbum();
            Deets.Year = id3v1Tag.getYear();
            Deets.Genre = id3v1Tag.getGenre();
            Deets.GenreDescription = id3v1Tag.getGenreDescription();
            Deets.Comment= id3v1Tag.getComment();
        }

        if (mp3.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3.getId3v2Tag();
            Deets.Track = id3v2Tag.getTrack();
            Deets.Artist= id3v2Tag.getArtist();
            Deets.Title = id3v2Tag.getTitle();
            Deets.Album= id3v2Tag.getAlbum();
            Deets.Year = id3v2Tag.getYear();
            Deets.Genre = id3v2Tag.getGenre();
            Deets.GenreDescription = id3v2Tag.getGenreDescription();
            Deets.Comment= id3v2Tag.getComment();
            Deets.Composer= id3v2Tag.getComposer();
            Deets.Publisher= id3v2Tag.getPublisher();
            Deets.OriginalArtist= id3v2Tag.getOriginalArtist();
            Deets.AlbumArtist= id3v2Tag.getAlbumArtist();
            Deets.Copyright= id3v2Tag.getCopyright();
            Deets.Encoder= id3v2Tag.getEncoder();
        }

    }
    @Test
    public void MP3Test() throws IOException {
        InputStream is1  = ImageTest.class.getResourceAsStream("/src/test/resources/1.mp3");
        try{
            MemoryStream ms = new MemoryStream();
            ms.Write(is1.readAllBytes());
            ms.Position=0;
            MP3Details deets = MP3.Load(ms);
            deets.OriginalArtist="";

        } finally {
            is1.close();
        }
    }
}