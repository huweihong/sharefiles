<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
   <head>
      <title>Cluster App Test</title>
   </head>
   <body>
      Server Info:
      <%
         out.println(request.getLocalName() + " : " + request.getLocalPort()
           + "<br>");
      %>
      <%
         out.println("<br> ID " + session.getId() + "<br>");
         String dataName = request.getParameter("dataName");
         if (dataName != null && dataName.length() > 0) {
            String dataValue = request.getParameter("dataValue");
            session.setAttribute(dataName, dataValue);
         }
         out.print("<b>Session ??</b>");
         Enumeration e = session.getAttributeNames();
         while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = session.getAttribute(name).toString();
            out.println(name + " = " + value + "<br>");
            System.out.println(name + " = " + value);
         }
      %>
      <form action="index.jsp" method="POST">Name:
         <input type=text size=20 name="dataName">
         <br>Value:
         <input type=text size=20 name="dataValue">
         <br>
         <input type=submit>
      </form>
   </body>
</html>