package controller;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.GameState;
import model.Question;
import model.QuestionProvider;
import model.TestQuestionProvider;
import org.apache.catalina.Session;

/**
 *
 * @author p62287946
 */
public class WhoIsTheShooter extends HttpServlet {

    private QuestionProvider questionProvider = new TestQuestionProvider();
    public static final String ATTRIBUTE_GAME_STATE = "GameState";
    public static final String PARAMETER_ANSWER = "a";

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object data = session.getAttribute(ATTRIBUTE_GAME_STATE);
        GameState gameState;
        if( data == null) {
            gameState = new GameState(questionProvider, 10);
            session.setAttribute(ATTRIBUTE_GAME_STATE, gameState);
        } else {
            gameState = (GameState) data;
        }
        
        String answer = request.getParameter(PARAMETER_ANSWER);
        if(answer != null) {
            try{ 
                int answerIndex = Integer.parseInt(answer);
                gameState.answerQuestionAndCreateNext(answerIndex);
                showGamePage(request, response);
            } catch (NumberFormatException numberFormatException) {
                request.getSession().invalidate();
                forwardToErrorPage(numberFormatException, request, response);
            }
        } else {
            showGamePage(request, response);
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

    private void forwardToErrorPage(Exception exception, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        exception.printStackTrace();
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }

    private void showGamePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/whostheshooter.jsp");
        dispatcher.forward(request, response);
    }
}
