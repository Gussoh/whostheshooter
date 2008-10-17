/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.print.attribute.ResolutionSyntax;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.GameState;

/**
 *
 * @author Gussoh
 */
public class ViewImage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            response.setHeader("Expires", "Sat, 26 Jul 1997 05:00:00 GMT");
            HttpSession session = request.getSession();
            
            
            String param = request.getParameter(controller.WhoIsTheShooter.PARAMETER_ANSWER);
            GameState gameState = (GameState) session.getAttribute(controller.WhoIsTheShooter.ATTRIBUTE_GAME_STATE);
            
            if(gameState == null) {
                return;
            }
            
            if (param == null || param.isEmpty()) {
                ByteBuffer buffer = gameState.getCurrentQuestion().getQuestionImage().getImageData();
                response.setContentLength(gameState.getCurrentQuestion().getQuestionImage().getSize());
                response.setContentType(gameState.getCurrentQuestion().getQuestionImage().getContentType());
                response.getOutputStream().write(buffer.array());
            } else {
                int monkeyIndex = Integer.parseInt(param);
                if(gameState.getCurrentQuestion().getMonkeys().get(monkeyIndex) == null) {
                    return;
                }
                    
                ByteBuffer buffer = gameState.getCurrentQuestion().getMonkeys().get(monkeyIndex).getImageData();
                response.setContentLength(gameState.getCurrentQuestion().getMonkeys().get(monkeyIndex).getSize());
                response.setContentType(gameState.getCurrentQuestion().getMonkeys().get(monkeyIndex).getContentType());
                response.getOutputStream().write(buffer.array());
            }

// If error we should write to log file. Cannot write msg since it is an image.
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }
}
