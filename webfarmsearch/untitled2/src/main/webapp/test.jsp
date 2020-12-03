<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/8/9
  Time: 12:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="db.MySQL" %>


<!DOCTYPE html>
<html>
<head>
    <title>Test</title>
</head>
<body>
<form action="/test.jsp">
    city:<input type="text" name="city"/><br/>
    State:<input type="text" name="State"/><br/>
    ID:<input type="text" name="ID"/><br/>
    zipcode:<input type="text" name="zipcode"/><br/>
    limitation:<input type="text" name="limitation"/><br/>
    zip(for searching):<input type="text" name="central_zip"/><br/>
    city(for searching):<input type="text" name="central_city"/><br/>
    state(for searching):<input type="text" name="central_state"/><br/>
    <input type="submit" value="submit"/>
</form>
<%
    int Page = 1;
    String city = request.getParameter("city");
    String state = request.getParameter("State");
    String ID = request.getParameter("ID");
    String zipcode = request.getParameter("zipcode");
    String central_state = request.getParameter("central_state");
    String central_city = request.getParameter("central_city");
    String central_zip = request.getParameter("central_zip");
    if(request.getParameter("Page") != null && !request.getParameter("Page").isEmpty()){
        Page =Integer.valueOf(request.getParameter("Page"));
    }
    Double lim = Double.valueOf(0);
    if (request.getParameter("limitation")!="") {
        lim = Double.valueOf(request.getParameter("limitation"));
    }
    boolean hasnextpage = MySQL.search(city,state,ID,zipcode,lim,Page,central_state,central_city,central_zip);
    out.println("Hello");
    for (int i = 0;i<MySQL.displaylist.size()&&i<13;i++){
        //response.getWriter().println(i);
        //response.getWriter().println("<br>");
        out.println("<li>"+MySQL.displaylist.get(i)+"</li>");
        out.println("<br>");
    }



%>
<%if(Page>1){ %>
<a style="margin-left: 10px" href="/test.jsp?Page=<%=Page-1%>&city=<%=city%>&State=<%=state%>&ID=<%=ID%>&zipcode=<%=zipcode%>
&limitation=<%=lim%>&central_zip=<%=central_zip%>&central_city=<%=central_city%>&central_state=<%=central_state%>">Previous Page</a>
<%}%>
<a>Page:<%=Page%></a>
<%if(hasnextpage){ %>
<a style="margin-left: 10px" href="/test.jsp?Page=<%=Page+1%>&city=<%=city%>&State=<%=state%>&ID=<%=ID%>&zipcode=<%=zipcode%>
&limitation=<%=lim%>&central_zip=<%=central_zip%>&central_city=<%=central_city%>&central_state=<%=central_state%>">Next Page</a>
<%}%>

</body>
</html>
