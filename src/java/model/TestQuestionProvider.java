/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author P23225291
 */
public class TestQuestionProvider extends QuestionProvider {

    @Override
    public Question createQuestion() {
        List<Monkey> monkeys = new LinkedList<Monkey>();
        for (int i = 0; i < 4; i++) {
            try {
                monkeys.add(new Monkey(new URL("http://imgs.xkcd.com/comics/barrel_cropped_(1).jpg")));
            } catch (IOException ex) {
            }
        }
        QuestionImage qi = null;
        try {
            qi = new QuestionImage(new URL("http://imgs.xkcd.com/comics/barrel_cropped_(1).jpg"));
        } catch (IOException ex) {
        }
        return new Question(monkeys, 3, qi);
    }
}
