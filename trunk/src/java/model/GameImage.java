/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

/**
 *
 * @author Gussoh
 */
public class GameImage {

    private ByteBuffer imageData;
    private int width = UNKNOWN,  height = UNKNOWN;
    private String contentType;
    public static final int UNKNOWN = Integer.MIN_VALUE;

    /**
     * 
     * @param imageData raw image data encoded according to the contentType
     * @param contentType the mime type of the image. image/jpeg or image/gif for example.
     * @param width width of image in pixels. set to GameImage.UNKNOWN for unknown
     * @param height height of image in pixels. set to GameImage.UNKNOWN for unknown
     */
    protected GameImage(ByteBuffer imageData, String contentType, int width, int height) {
        this.imageData = imageData;
        this.width = width;
        this.height = height;
        this.contentType = contentType;
    }

    /**
     * Files can have a maximum size of Integer.MAX_VALUE bytes
     * @param location location of the file.
     * @param width width of image in pixels. set to GameImage.UNKNOWN for unknown
     * @param height height of image in pixels. set to GameImage.UNKNOWN for unknown
     * @throws IOException when error fetching the image
     */
    protected GameImage(URL location, int width, int height) throws IOException {

        URLConnection connection = location.openConnection();
        InputStream in = new BufferedInputStream(connection.getInputStream());
        
        int data;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        while ((data = in.read()) != -1) {
            arrayOutputStream.write(data);
        }

        ByteBuffer.wrap(arrayOutputStream.toByteArray());

        contentType = connection.getContentType();
        in.close();
    }

    public ByteBuffer getImageData() {
        return imageData;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getContentType() {
        return contentType;
    }
}
