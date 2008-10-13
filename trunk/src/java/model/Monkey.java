/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 *
 * @author Gussoh
 */
public class Monkey extends GameImage {

    public Monkey(URL location, int width, int height) throws IOException {
        super(location, width, height);
    }

    public Monkey(ByteBuffer imageData, String contentType, int width, int height) {
        super(imageData, contentType, width, height);
    }

    
}
