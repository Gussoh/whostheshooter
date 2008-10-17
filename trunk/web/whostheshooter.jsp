<%-- 
    Document   : whostheshooter
    Created on : Oct 10, 2008, 3:08:04 PM
    Author     : p62287946
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
response.setHeader("Cache-Control", "no-cache, must-revalidate");
response.setHeader("Expires", "Sat, 26 Jul 1997 05:00:00 GMT"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@ page import="model.*" %>
<%
            Object data = session.getAttribute(controller.WhoIsTheShooter.ATTRIBUTE_GAME_STATE);
            GameState gameState = null;
            if (data != null) {
                gameState = (GameState) data;
            } else {
                // FAIL - SHOW ERROR MESSAGE AND DIE
                out.print("<h1>EPIC FAIL</h1>");
                return;
            }
            if(gameState.getCurrentQuestion() == null) {
                // FAIL - SHOW ERROR MESSAGE AND DIE
                out.print("<h1>EPIC FAIL</h1>");
                return;
            }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Who's the monkey?</title>
    </head>
    <body>
        <form action="" method="post" name="guess">
            <input type="hidden" name="<%=controller.WhoIsTheShooter.PARAMETER_ANSWER %>"/>
            <input type="hidden" name="check" value="<%= session.getAttribute(controller.WhoIsTheShooter.ATTRIBUTE_CHECK) %>"/>
        </form>
        <h1>Click on the user you think captured this photograph</h1>
        <div class="image"><img src="image" alt="who captured this?"/></div>
        <div class="monkeys">
            <%
            int i = 0;
            for (Monkey m : gameState.getCurrentQuestion().getMonkeys()) {
                out.println("<img src=\"image?" + controller.WhoIsTheShooter.PARAMETER_ANSWER + "="+ i +"\" alt=\"this one?\" onClick=\"document.guess." + controller.WhoIsTheShooter.PARAMETER_ANSWER + ".value = '" + i + "'; document.guess.submit();\" />");
                i++;
            }
            %>
        </div>
    </body>
</html>
