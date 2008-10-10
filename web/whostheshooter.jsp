<%-- 
    Document   : whostheshooter
    Created on : Oct 10, 2008, 3:08:04 PM
    Author     : p62287946
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 

<%@ page import="model.*" %>
<%
            Object data = session.getAttribute("question");
            Question question = null;
            if (data != null) {
                question = (Question) data;
            } else {
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
            <input type="hidden" name="monkey"/>
        </form>
        <h1>Click on the user you think captured this photograph</h1>
        <div class="image"><img src="image.jsp" alt="who captured this?"/></div>
        <div class="monkeys">
            <%
            int i = 0;
            for (Monkey m : question.getMonkeys()) {
                out.println("<img src=\"image.jsp?i="+ i +"\" alt=\"this one?\" onClick=\"document.guess.monkey.value = '" + i + "'; document.guess.submit();\" />");
                i++;
            }
            %>
        </div>
    </body>
</html>
