<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*,app.prepareData.*,app.entity.*" %>
<%!
	PrepareData preData = PrepareData.getInstance();
%>

<%
 
	    //out.print("<h1> "+ lines.size()+ "</h1>");
	    
	    // 直接输出的数据，就会发给index.jsp
	    out.print("ajax data");
	    
%>