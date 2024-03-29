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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 *
 * @author TAND0015
 */
public class FlickrQuestionProvider extends QuestionProvider {

    private int numberOfMonkeys = 4;
    List<Monkey> errorMonkeys = new ArrayList<Monkey>();
    HashSet<String> errorMonkeyUrls = new HashSet<String>();

    public FlickrQuestionProvider() {
    }

    @Override
    protected Question createQuestion() throws QuestionProviderException {
        try {
            Flickr f = new Flickr("159ced7ac09b5794485ac8dee1e4be20");
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
            String profilePagePrefix = "http://www.flickr.com/photos/";


            // get the correct monkey                  if we have 400 errormonkeys the probability of showing the monkey as error at the same time is small enough
            while (correctOwnerIconFarm == 0 || (errorMonkeyUrls.contains(ownerIconUrl) && errorMonkeys.size() < 400)) {
                System.out.println("Entering search for photo and user");

                int page = 1;//(int) (Math.random() * 10); // we could use a small random page number for a little bit more random set of users.. i dont know if its better..

                photoList = photosInterface.getRecent(5, page);

                randint = Math.abs(r.nextInt()) % 5;
                photo = (Photo) photoList.get(randint);
                photoOwner = photo.getOwner();

                ownerInfo = peopleInterface.getInfo(photoOwner.getId());


                correctOwnerIconFarm = ownerInfo.getIconFarm();
                photoUrl = photo.getMediumUrl();
                photoPageUrl = photo.getUrl();
                ownerIconUrl = ownerInfo.getBuddyIconUrl();
                ownerUsername = ownerInfo.getId();

                System.out.println("\tUSER: " + ownerInfo.getUsername() + "\tPHOTO TITLE: " + photo.getTitle());
            }

            if (errorMonkeys.size() > 1000) {
                System.out.println("Clearing error monkey database...");
                errorMonkeys.clear();
                errorMonkeyUrls.clear();
            }

            // getting error monkey icons
            boolean first = true;
            int counter = 0;
            while (errorMonkeys.size() < 5 || first) {

                System.out.println("Entering search for error monkeys, count: " + counter + ", number of error monkeys: " + errorMonkeys.size());
                first = false;
                if (counter++ > 60) { // just in case we do this forever...

                    throw new QuestionProviderException("Could not find enough error monkeys! :(");
                }
                int page = 1; //(int) (Math.random() * 100); // maybe use a random page number for a more random set of users

                photoList = photosInterface.getRecent(10, page);


                for (int i = 0; i < photoList.size(); i++) {
                    Photo currentPhoto = (Photo) photoList.get(i);
                    User currentOwner = currentPhoto.getOwner();
                    User currentInfo = null;
                    currentInfo = peopleInterface.getInfo(currentOwner.getId());

                    String errorMonkeyIconUrl = currentInfo.getBuddyIconUrl();
                    String errorMonkeyUsername = currentInfo.getId();

                    boolean hasIcon = currentInfo.getIconServer() != 0;
                    boolean isRealOwner = errorMonkeyIconUrl.equals(ownerIconUrl);

                    System.out.println("\tUSER: " + errorMonkeyUsername + (hasIcon ? "\tADDING BUDDYICON" : "") + (isRealOwner ? "\tREAL OWNER - NOT ADDING!" : ""));

                    if (hasIcon && !isRealOwner) {

                        if (!errorMonkeyUrls.contains(errorMonkeyIconUrl)) { // add it if it isn't already there

                            errorMonkeys.add(new Monkey(new URL(errorMonkeyIconUrl), new URL(profilePagePrefix + URLEncoder.encode(errorMonkeyUsername, "UTF-8"))));
                            errorMonkeyUrls.add(errorMonkeyIconUrl);
                        }

                    }
                }
            }

            Collections.shuffle(errorMonkeys);
            int correct = (int) (Math.random() * numberOfMonkeys);
            Monkey[] monkeys = new Monkey[numberOfMonkeys];

            monkeys[correct] = new Monkey(new URL(ownerIconUrl), new URL(profilePagePrefix + URLEncoder.encode(ownerUsername, "UTF-8")));

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
            qi = new QuestionImage(new URL(photoUrl), new URL(photoPageUrl));


            return new Question(Arrays.asList(monkeys), correct, qi);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new QuestionProviderException(ex.getMessage());
        }
    }
}
