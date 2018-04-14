package com.aurawin.scs.media;

import com.aurawin.core.file.Util;
import com.aurawin.core.file.Writer;
import com.aurawin.core.rsr.transport.methods.http.dav.Write;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ImageTest {
    String Output1 = "/home/atbrunner/au1.png";
    String Output2 = "/home/atbrunner/au2.png";
    String Output3 = "/home/atbrunner/au3.png";
    String Output4 = "/home/atbrunner/au4.png";
    @Test
    public void TestResource() throws IOException {
        InputStream is1  = ImageTest.class.getResourceAsStream("/src/test/resources/au.png");
        try{
            byte[] img1 = Image.Rotate(is1.readAllBytes(),"png",90);
            Writer.toFile(img1,new File(Output1));

        } finally {
            is1.close();
        }
        InputStream is2  = ImageTest.class.getResourceAsStream("/src/test/resources/au.png");
        try {
            byte[] img2 = Image.Scale(is2.readAllBytes(), "png", 0.5);
            Writer.toFile(img2, new File(Output2));
        } finally {
            is2.close();
        }
        InputStream is3  = ImageTest.class.getResourceAsStream("/src/test/resources/au.png");
        try {
            byte[] img3 = Image.scaleTo(is3.readAllBytes(), "png", Scale.sDisplay);
            Writer.toFile(img3, new File(Output3));
        } finally {
            is3.close();
        }
        InputStream is4  = ImageTest.class.getResourceAsStream("/src/test/resources/family.png");
        try {
            byte[] img4 = Image.scaleTo(is4.readAllBytes(), "png", Scale.sThumb);
            Writer.toFile(img4, new File(Output4));
        } finally {
            is4.close();
        }

    }

}