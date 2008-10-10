/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Gussoh
 */
public class Question {

    private List<Monkey> monkeys;
    private int correctMonkeyIndex;
    private QuestionImage questionImage;

    protected Question(List<Monkey> monkeys, int correctMonkeyIndex, QuestionImage questionImage) {
        this.monkeys = monkeys;
        this.correctMonkeyIndex = correctMonkeyIndex;
        this.questionImage = questionImage;
    }

    public int getCorrectMonkeyIndex() {
        return correctMonkeyIndex;
    }

    public List<Monkey> getMonkeys() {
        return monkeys;
    }

    public QuestionImage getQuestionImage() {
        return questionImage;
    }
}
