/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author P23225291
 */
public class GameState {

    private List<Question> questions = new LinkedList<Question>();
    private Map<Question, Integer> questionToAnswerMap = new HashMap<Question, Integer>();
    private int numQuestions,  currentQuestion;
    private QuestionProvider questionProvider;

    public GameState(QuestionProvider questionProvider, int numQuestions) {
        this.questionProvider = questionProvider;
        this.numQuestions = numQuestions;

        questions.add(questionProvider.createQuestion());
        currentQuestion = 0;
    }

    /**
     * Answers current questions and creates the next Question. 
     * After this method returns, getCurrentQuestion will return a new question.
     * @param answer
     */
    public void answerQuestionAndCreateNext(int answer) {
        if(isGameFinished()) {
            throw new IllegalStateException("Cannot answer question. Game already finished.");
        }
        questionToAnswerMap.put(getCurrentQuestion(), answer);
        questions.add(questionProvider.createQuestion());
        currentQuestion++;
    }
    /**
     * Returns current Question or null if game is finished.
     * @return
     */
    public Question getCurrentQuestion() {
        if(isGameFinished()) {
            return null;
        }
        return questions.get(currentQuestion);
    }
    
    /**
     * Returns current question index. starts at 0.
     * @return the index.
     */
    public int getCurrentQuestionIndex() {
        return currentQuestion;
    }

    /**
     * 
     * @return total number of questions to be answered before the game is finished.
     */
    public int getNumQuestions() {
        return numQuestions;
    }
    
    /**
     * 
     * @return true if game is finished, otherwise false.
     */
    public boolean isGameFinished() {
        return currentQuestion >= numQuestions;
    }
}
