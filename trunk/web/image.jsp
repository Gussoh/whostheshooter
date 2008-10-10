<%-- 
    Document   : image
    Created on : Oct 10, 2008, 3:35:36 PM
    Author     : p62287946
--%>

<%@page contentType="image/jpeg"%>
<%@page import="model.*, java.nio.*, java.io.*"%>
<%
            try {
                String param = request.getParameter(controller.WhoIsTheShooter.PARAMETER_ANSWER);
                GameState gameState = (GameState) session.getAttribute(controller.WhoIsTheShooter.ATTRIBUTE_GAME_STATE);
                if (param == null || param.isEmpty()) {
                    ByteBuffer buffer = gameState.getCurrentQuestion().getQuestionImage().getImageData();
                    response.getOutputStream().write(buffer.array());
                } else {
                    int monkeyIndex = Integer.parseInt(param);
                    ByteBuffer buffer = gameState.getCurrentQuestion().getMonkeys().get(monkeyIndex).getImageData();
                    response.getOutputStream().write(buffer.array());
                }

// If error we should write to log file. Cannot write msg since it is an image.
            } catch (NumberFormatException ex) {
            } catch (NullPointerException ex) {
            } catch (ClassCastException ex) {
            } catch (IndexOutOfBoundsException ex) {
            }

%>


