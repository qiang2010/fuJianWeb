<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*,app.prepareData.*,app.entity.*" %>
<%!
	PrepareData preData = PrepareData.getInstance();
%>

<%
 	// 准备需要返回的数据
    	request.setCharacterEncoding("gb2312"); // 简体中文
	    String time = request.getParameter("time");
		if(time == null){
			out.println("time null");
			time = "2015-06-15 12:30:00";
		}
	    String  roadName = request.getParameter("roads");
	    if(roadName == null){
	    	roadName = "0";
	    }
	    String dir = request.getParameter("direction");
	    int direction = 1;//
	    if(dir == null){
	    	direction = 1;
	    }else{
	    	direction = Integer.parseInt(dir);
	    }
	    out.println("time： " +time);
	    out.println("road:"+roadName);
	    out.println("dir:"+dir);

	    List<Line> lines = preData.getAllLinePoint(time, roadName, direction);
	    //out.print("<h1> "+ lines.size()+ "</h1>");
	    
	    // 直接输出的数据，就会发给index.jsp
	    out.print("ajax data");
	    
%>