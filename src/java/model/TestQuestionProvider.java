/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author P23225291
 */
public class TestQuestionProvider implements QuestionProvider {

    public Question createQuestion() {
        List<Monkey> monkeys = new LinkedList<Monkey>();
        for (int i = 0; i < 4; i++) {
            monkeys.add(new Monkey(ByteBuffer.allocate(0), 200, 200));
        }
        QuestionImage qi = new QuestionImage(ByteBuffer.allocate(0), 400, 300);
        return new Question(monkeys, 3, qi);
    }
}
