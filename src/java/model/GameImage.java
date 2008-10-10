/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.nio.ByteBuffer;

/**
 *
 * @author Gussoh
 */
public class GameImage {
    private ByteBuffer imageData;
    private int width, height;

    protected GameImage(ByteBuffer imageData, int width, int height) {
        this.imageData = imageData;
        this.width = width;
        this.height = height;
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
    
}
