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
public class QuestionImage extends GameImage {

    public QuestionImage(URL location) throws IOException {
        super(location);
    }

    public QuestionImage(ByteBuffer imageData, String contentType) {
        super(imageData, contentType);
    }


    
}
