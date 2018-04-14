package com.aurawin.scs.media;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Image {
    public static byte[] Rotate(byte[] data, String ext, int Degree){
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try {
            try {
                BufferedImage img = ImageIO.read(is);
                AffineTransform at = new AffineTransform();
                at.rotate(Math.toRadians(Degree), img.getWidth()/2, img.getHeight()/2);
                AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                img = op.filter(img, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
                try {
                    ImageIO.write(img, ext, os);
                    return os.toByteArray();
                } finally {
                    os.close();
                }

            } finally {
                is.close();
            }
        }catch (IOException ioe){
            return null;
        }
    }

    public static byte[] Scale(byte[] data, String ext, double Factor){
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try {
            try {
                BufferedImage img = ImageIO.read(is);
                AffineTransform at = new AffineTransform();

                at.scale(Factor,Factor);
                AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                img = op.filter(img, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
                try {
                    ImageIO.write(img, ext, os);
                    return os.toByteArray();
                } finally {
                    os.close();
                }

            } finally {
                is.close();
            }
        }catch (IOException ioe){
            return null;
        }
    }

    public static byte[] scaleTo(byte[] data, String ext, Scale scale){
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try {
            try {
                BufferedImage img = ImageIO.read(is);
                AffineTransform at = new AffineTransform();
                double y = img.getHeight();
                double x = img.getWidth();
                double slope = y/x;
                double scalor = 1;

                Point pt = scale.getValue();
                double scalorX = pt.x/x;
                double scalorY = pt.y/y;

                if (slope==1.0){
                    scalor = scalorX;
                } else {
                    scalor = (scalorX>scalorY) ? scalorX : scalorY;
                }
                at.scale(scalor,scalor);
                AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                img = op.filter(img, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
                try {
                    ImageIO.write(img, ext, os);
                    return os.toByteArray();
                } finally {
                    os.close();
                }

            } finally {
                is.close();
            }
        }catch (IOException ioe){
            return null;
        }
    }
}
