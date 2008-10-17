/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.people.PeopleInterface;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author TAND0015
 */
public class FlickrQuestionProvider extends QuestionProvider {

    private static RequestContext requestContext;
    private int numberOfMonkeys = 4;
    List<Monkey> errorMonkeys = new ArrayList<Monkey>();
    HashSet<String> errorMonkeyUrls = new HashSet<String>();

    @Override
    @SuppressWarnings("empty-statement")
    protected Question createQuestion() throws QuestionProviderException {



        requestContext = RequestContext.getRequestContext();
        String apiKey = "159ced7ac09b5794485ac8dee1e4be20";
        Flickr f = new Flickr(apiKey);

        // Set the shared secret which is used for any calls which require signing.
        f.setSharedSecret("9c8dc7a2d4967255");

        PhotosInterface photosInterface = f.getPhotosInterface();
        PhotoList photoList = null;
        Photo photo = null;
        String photoUrl = null;
        String photoPageUrl = null;
        PeopleInterface peopleInterface = f.getPeopleInterface();
        User photoOwner = null;
        User ownerInfo = null;
        String ownerIconUrl = null;
        String ownerUsername = null;
        int correctOwnerIconFarm = 0;
        Random r = new Random();
        int randint = 0;
        String profilePagePrefix = "http://flickr.com/people";

 
        // get the correct monkey
        while (correctOwnerIconFarm == 0 && !errorMonkeyUrls.contains(ownerIconUrl)) {
            try {
                photoList = photosInterface.getRecent(5, 1);
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
            randint = Math.abs(r.nextInt()) % 5;
            photo = (Photo) photoList.get(randint);
            photoOwner = photo.getOwner();

            try {
                ownerInfo = peopleInterface.getInfo(photoOwner.getId());
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

            correctOwnerIconFarm = ownerInfo.getIconFarm();
            photoUrl = photo.getMediumUrl();
            photoPageUrl = photo.getUrl();
            ownerIconUrl = ownerInfo.getBuddyIconUrl();
            ownerUsername = ownerInfo.getUsername();
        }        
        
        
        // getting error monkey icons
        boolean first = true;
        while (errorMonkeys.size() < 10 || first) {
            first = false;
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

            for (int i = 0; i < photoList.size(); i++) {
                Photo currentPhoto = (Photo) photoList.get(i);
                User currentOwner = currentPhoto.getOwner();
                User currentInfo = null;
                try {
                    currentInfo = peopleInterface.getInfo(currentOwner.getId());
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
                String errorMonkeyIconUrl = currentInfo.getBuddyIconUrl();
                String errorMonkeyUsername = currentInfo.getUsername();
                if (currentInfo.getIconServer() != 0 && !errorMonkeyIconUrl.equals(ownerIconUrl)) {
                    
                    try {
                        errorMonkeys.add(new Monkey(new URL(errorMonkeyIconUrl), new URL(profilePagePrefix + errorMonkeyUsername) ));
                        errorMonkeyUrls.add(errorMonkeyIconUrl);
                    } catch (IOException ex) {
                        Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
                        throw new QuestionProviderException(ex.toString());
                    }
                }
            }
        }

        Collections.shuffle(errorMonkeys);
        int correct = (int) (Math.random() * numberOfMonkeys);
        Monkey[] monkeys = new Monkey[numberOfMonkeys];
        try {
            monkeys[correct] = new Monkey(new URL(ownerIconUrl), new URL(profilePagePrefix + ownerUsername));
        } catch (IOException ex) {
            Logger.getLogger(FlickrQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new QuestionProviderException(ex.toString());
        }
        for (int i = 0; i < monkeys.length; i++) {

            if (i != correct) {
                monkeys[i] = errorMonkeys.get(i);
            }
        }

        /*for (int i = 0; i < 4; i++) {
        try {
        monkeys.add(new Monkey(new URL(ownerIconUrl)));
        } catch (IOException ex) {
        throw new QuestionProviderException(ex.toString());
        }
        }*/
        QuestionImage qi = null;
        try {
            qi = new QuestionImage(new URL(photoUrl), new URL(photoPageUrl));
        } catch (IOException ex) {
            throw new QuestionProviderException(ex.toString());
        }

        return new Question(Arrays.asList(monkeys), correct, qi);
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
