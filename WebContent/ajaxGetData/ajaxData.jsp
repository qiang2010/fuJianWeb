<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*,app.prepareData.*,app.entity.*" %>
<%!
	PrepareData preData = PrepareData.getInstance();
%>

<%
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
		
		
		// 开始的时候是在这里 将 List<Line> 编码成json格式的，后来发现总是找不到包，于是放到了ParepareData中
		// 实际上， 由于是java web项目，依赖的包必须在WEB-INF 下的lib中放一个。
		String ans = preData.getAllLinePointJSON(time, roadName, direction); 		


	    out.print(ans);
	    // 直接输出的数据，就会发给index.jsp
	    out.print("ajax data： "+time +":" + roadName+" "+ direction);
	    
%>