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

    private URL profile;
    
    public Monkey(URL location, URL profile) throws IOException {
        super(location);
        this.profile = profile;
    }

    public Monkey(ByteBuffer imageData, String contentType, URL profile) {
        super(imageData, contentType);
        this.profile = profile;
    }

    public URL getProfile() {
        return profile;
    }
    
    
}
