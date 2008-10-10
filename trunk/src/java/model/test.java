/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;



/**
 *
 * @author TAND0015
 */
public class test {
    private static RequestContext requestContext;
    
    // testing 
    public static void main(String args[]) throws IOException, SAXException, FlickrException
    {
        
        requestContext = RequestContext.getRequestContext();
        String apiKey = "159ced7ac09b5794485ac8dee1e4be20";
        System.out.println(apiKey);
        Flickr f = new Flickr(apiKey);
        
        // Set the shared secret which is used for any calls which require signing.
        f.setSharedSecret("9c8dc7a2d4967255");
                
        PhotosInterface photosInterface = f.getPhotosInterface();
        PhotoList photoList = photosInterface.getRecent(1, 1);
        Photo photo;
                
        if(!photoList.isEmpty())
        {
            photo = (Photo)photoList.get(0); 
            String photoUrl = photo.getUrl();
            System.out.println(photoUrl);
            
            BufferedImage photoImage = photosInterface.getImage(photoUrl);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);

            ImageIO.write(photoImage, "jpeg" ,baos);

            baos.flush();
            byte[] resultImageAsRawBytes = baos.toByteArray();

            baos.close();         
        }       
    }
    
    
}
