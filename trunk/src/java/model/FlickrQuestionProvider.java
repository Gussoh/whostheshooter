/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author TAND0015
 */
public class FlickrQuestionProvider extends QuestionProvider {
    private static RequestContext requestContext;
    @Override
    protected Question createQuestion() throws QuestionProviderException {
        
        requestContext = RequestContext.getRequestContext();
        String apiKey = "159ced7ac09b5794485ac8dee1e4be20";
        Flickr f = new Flickr(apiKey);
        
        // Set the shared secret which is used for any calls which require signing.
        f.setSharedSecret("9c8dc7a2d4967255");
                
        PhotosInterface photosInterface = f.getPhotosInterface();
        PhotoList photoList = null;
        Photo photo;
        byte[] resultImageAsRawBytes = null;        
        try {
            photoList = photosInterface.getRecent(10, 1);
            
        } catch (IOException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new QuestionProviderException(ex.toString());
        } catch (SAXException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new QuestionProviderException(ex.toString());
        } catch (FlickrException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new QuestionProviderException(ex.toString());
        }
        
        Random r = new Random();
        int randint = Math.abs(r.nextInt()) % 10;       
        photo = (Photo)photoList.get(randint); 
        String photoUrl = photo.getMediumUrl();
        User photoOwner = photo.getOwner();
        String ownerIconUrl = photoOwner.getBuddyIconUrl();
        
         List<Monkey> monkeys = new LinkedList<Monkey>();
        for (int i = 0; i < 4; i++) {
            try {
                monkeys.add(new Monkey(new URL(ownerIconUrl)));
            } catch (IOException ex) {
                throw new QuestionProviderException(ex.toString());
            }
        }       
               
        QuestionImage qi = null;
        try {
            qi = new QuestionImage(new URL(photoUrl));
        } catch (IOException ex) {
            throw new QuestionProviderException(ex.toString());
        }
        return new Question(monkeys, 3, qi);       
        //return null;
        /*
         List<Monkey> monkeys = new LinkedList<Monkey>();
        for (int i = 0; i < 4; i++) 
            monkeys.add(new Monkey(ByteBuffer.allocate(0), 200, 200));
        requestContext = RequestContext.getRequestContext();
        String apiKey = "159ced7ac09b5794485ac8dee1e4be20";
        Flickr f = new Flickr(apiKey);
        
        // Set the shared secret which is used for any calls which require signing.
        f.setSharedSecret("9c8dc7a2d4967255");
                
        PhotosInterface photosInterface = f.getPhotosInterface();
        PhotoList photoList = null;
        Photo photo;
        byte[] resultImageAsRawBytes = null;        
        try {
            photoList = photosInterface.getRecent(1, 1);
        } catch (IOException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlickrException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        photo = (Photo)photoList.get(0); 
        String photoUrl = photo.getUrl();
            
        BufferedImage photoImage = null;
            
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        try {
            photoImage = photosInterface.getImage(photoUrl);
            ImageIO.write(photoImage, "jpeg", baos);
            baos.flush();

            resultImageAsRawBytes = baos.toByteArray();

            baos.close();
        } catch (IOException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        ByteBuffer buf = ByteBuffer.wrap(resultImageAsRawBytes);
        QuestionImage qi = new QuestionImage(buf, 400, 300);
        return new Question(monkeys, 3, qi);*/
    }

}
