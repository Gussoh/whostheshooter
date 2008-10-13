/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author P23225291
 */
public class TestQuestionProvider implements QuestionProvider {

    public Question createQuestion() {
        List<Monkey> monkeys = new LinkedList<Monkey>();
        for (int i = 0; i < 4; i++) {
            try {
                monkeys.add(new Monkey(new URL("http://imgs.xkcd.com/comics/barrel_cropped_(1).jpg"), 200, 200));
            } catch (IOException ex) {
                Logger.getLogger(TestQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        QuestionImage qi = null;
        try {
            qi = new QuestionImage(new URL("http://imgs.xkcd.com/comics/barrel_cropped_(1).jpg"), 200, 200);
        } catch (IOException ex) {
            Logger.getLogger(TestQuestionProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Question(monkeys, 3, qi);
    }
}
