package controller;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.FlickrQuestionProvider;
import model.GameState;
import model.QuestionProvider;
import model.QuestionProviderException;

/**
 *
 * @author p62287946
 */
public class WhoIsTheShooter extends HttpServlet {

    private QuestionProvider questionProvider = new FlickrQuestionProvider();
    public static final String ATTRIBUTE_GAME_STATE = "GameState";
    public static final String ATTRIBUTE_KEY = "key";
    public static final String PARAMETER_ANSWER = "a";
    public static final String PARAMETER_INVALIDATE = "invalidate";
    public static final String PARAMETER_QUESTION = "q";
    public static final String PARAMETER_XML = "xml";

    public WhoIsTheShooter() {
        questionProvider.startNewFetchingThread();
    }
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();


        boolean correctKey;
        {
            String key = request.getParameter(ATTRIBUTE_KEY);
            correctKey = key != null && key.equals(session.getAttribute(ATTRIBUTE_KEY));
        }

        if (Boolean.parseBoolean(request.getParameter(PARAMETER_INVALIDATE)) && correctKey) {
            session.removeAttribute(ATTRIBUTE_GAME_STATE);
        }

        Object gameStateAttribute = session.getAttribute(ATTRIBUTE_GAME_STATE);
        GameState gameState;

        // First time user request XML data
        if (request.getParameter(PARAMETER_XML) != null) {
            if (gameStateAttribute == null) {
                try {
                    gameState = new GameState(questionProvider, 5);
                    session.setAttribute(ATTRIBUTE_KEY, Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE)));
                } catch (QuestionProviderException ex) {
                    showErrorPage(ex, request, response);
                    return;
                }
                session.setAttribute(ATTRIBUTE_GAME_STATE, gameState);
            } else {
                gameState = (GameState) gameStateAttribute;
            }
        } else {
            // First time user visits the page, show empty page and let ajax fetch first question
            showGamePage(request, response);
            return;
        }

        String answer = request.getParameter(PARAMETER_ANSWER);

        // If user tries to answer and the checkId is correct 
        if (answer != null && correctKey) {

            try {
                int answerIndex = Integer.parseInt(answer);

                try {
                    gameState.answerQuestionAndCreateNext(answerIndex);
                    session.setAttribute(ATTRIBUTE_KEY, Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE)));
                } catch (QuestionProviderException ex) {
                    showErrorPage(ex, request, response);
                    return;
                }
            } catch (NumberFormatException numberFormatException) {
                request.getSession().invalidate();
                showErrorPage(numberFormatException, request, response);
                return;
            }
        }

        if (gameState.isGameFinished()) {
            if (request.getParameter(PARAMETER_XML) != null) {
                showScorePageXML(request, response);
            } else {
                showScorePage(request, response);
            }
        } else {
            if (request.getParameter(PARAMETER_XML) != null) {
                showGamePageXML(request, response);
            } else {
                showGamePage(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void showErrorPage(Exception exception, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        exception.printStackTrace();
        RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("errorPage");
        dispatcher.forward(request, response);
    }

    private void showGamePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("gamePage");
        dispatcher.forward(request, response);
    }

    private void showScorePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("scorePage");
        dispatcher.forward(request, response);
    }

    private void showScorePageXML(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("scorePageXML");
        dispatcher.forward(request, response);
    }

    private void showGamePageXML(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("gamePageXML");
        dispatcher.forward(request, response);
    }
}
