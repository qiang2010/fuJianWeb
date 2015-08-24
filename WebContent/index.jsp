<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
    %>
<%@ page import="java.util.*,app.prepareData.*,app.entity.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>webTest</title>
	<style type="text/css">
		body{
			margin:0;
			height:100%;
			width:100%;
			position:absolute;
		}
		#mapContainer{
			position: absolute;
			top:0;
			left: 0;
			right:0;
			bottom:0;
		}
		
		#tip{
			position:absolute;
			bottom:20px;
			right:0;
			height:80px;
			font-size:12px;
		}
		
		#tip input[type='button']{
			margin-top:10px;
			height:28px;
			line-height:28px;
			outline:none;
			text-align:center;
			padding-left:5px;
			padding-right:5px;
			color:#FFF;
			background-color:#0D9BF2;
			border:0;
			border-radius: 3px;
			margin-top:5px;
			margin-left:5px;
			cursor:pointer;
			margin-right:10px;
		}
		#tip input[type='submit']{
			margin-top:10px;
			height:28px;
			line-height:28px;
			outline:none;
			text-align:center;
			padding-left:5px;
			padding-right:5px;
			color:#FFF;
			background-color:#0D9BF2;
			border:0;
			border-radius: 3px;
			margin-top:5px;
			margin-left:5px;
			cursor:pointer;
			margin-right:10px;
		}
	</style>
	<script type="text/javascript">
		function setFunc(){
			var road = new Array("G15","G70","G72","G76","G1501","G1514","S35","G3","G25","G319","G324");
			var select = document.getElementById("roadSelect");
			select.length = 1;
			select.options[0].selected=true;
			for(var i =0 ; i < road.length;i++){
				var option = document.createElement("option");
				option.setAttribute("value",i+1);
				option.appendChild(document.createTextNode(road[i]));
				select.appendChild(option);
				if(i+1 == <%=request.getParameter("roads")%>)
					option.selected=true;
			}
			
			// 设置方向为之前选中的方向
			var directionOP = document.getElementById("directionSelect");
			if(0 == <%=request.getParameter("direction") %>){
				directionOP.options[0].selected = true;
			}else{
				directionOP.options[1].selected = true;
			}
		}
	</script>
	    
	    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script> 
    <script src="http://malsup.github.com/jquery.form.js"></script> 
	
	<script>
		//<!-- 这里写ajax更新的代码,用于实时更新-->
		$(document).ready(function(){
				var options = {
					beforeSubmit: showRequest,
					success: showResponse
				};
				
				$('#dataSetForm').submit(function(){
					$(this).ajaxSubmit(options);
					
					return false;  
				});
			 });
		// 当点击提交按钮首先之行这里的函数。
		function showRequest(formData, jqForm, options){
		    // formData is an array; here we use $.param to convert it to a string to display it 
		    // but the form plugin does this for you automatically when it submits the data 
			var queryString = $.param(formData);
		    alert('submit'+queryString);
		    
		    // here we could return false to prevent the form from being submitted; 
		    // returning anything other than false will allow the form submit to continue 
		    return true;
		    
		}
		// 提交后的回调函数
		function showResponse(responseText,statusText){
			// for normal html responses, the first argument to the success callback 
		    // is the XMLHttpRequest object's responseText property 
		 
		    // if the ajaxForm method was passed an Options Object with the dataType 
		    // property set to 'xml' then the first argument to the success callback 
		    // is the XMLHttpRequest object's responseXML property 
		 
		    // if the ajaxForm method was passed an Options Object with the dataType 
		    // property set to 'json' then the first argument to the success callback 
		    // is the json data object returned by the server 
		    if(statusText == "success"){
		    	// 	
		    	 alert("jq");
		    	 polyline.setMap(null);	
		    }
		    
		}
		
	</script>
	
</head>

<body onload="setFunc()">
	<div id="mapContainer"></div>
		<%! 
		// 声明的部分，只会初始化一次
	    PrepareData pp = PrepareData.getInstance();
	    // 首先加载所有道路的信息，会在PrepareData的构造器中加载一次
		// 根据用户提交表单设置查询条件
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
	    
	    
	    
	    List<Line> lines = pp.getAllLinePoint(time, roadName, direction);
	    //out.print("<h1> "+ lines.size()+ "</h1>");
	    out.print(lines.size());
		%>
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=ef5828a2b1f622d83e5044efc83c730b"></script>
	<script type="text/javascript">

		 var allLines = new Array();
		 var lineArr;
		
			<% int i=0;
			Line tempLine=null;
			for(i=0; i < lines.size();i++){ 
			   tempLine = lines.get(i);
			  %>
			   lineArr = new Array();//创建线覆盖物节点坐标数组
			   lineArr[0] = <% out.print(tempLine.getStartLong()); %>;
			   lineArr[1] = <%=tempLine.getStartLat() %> ;
			   lineArr[2] = <%=tempLine.getEndLong() %> ;
			   lineArr[3] = <%=tempLine.getEndLat() %>; 
			   lineArr[4] = '<%=tempLine.getColor() %>';
			   allLines[<%=i%>]=lineArr;
		    <% } %>
			//添加线覆盖物
		
		//初始化地图对象，加载地图
		var map = new AMap.Map("mapContainer",{
				resizeEnable: true,
			  	view: new AMap.View2D({
		      	center:new AMap.LngLat(117.628264,25.797241),//地图中心点
		      	zoom:8 //地图显示的缩放级别
		     })
		});
 
	</script>
	<script>		

		var oneLine;
		var twoPoints;
		var polyline;
		for(var j=0;j < allLines.length; j++ ){
			oneLine = allLines[j];
			twoPoints = new Array();
			twoPoints.push(new AMap.LngLat(oneLine[0],oneLine[1]));
			twoPoints.push(new AMap.LngLat(oneLine[2],oneLine[3]));
			 polyline = new AMap.Polyline({ 
				   path:twoPoints, //设置线覆盖物路径
				   strokeColor:oneLine[4], //线颜色
				   strokeOpacity:1, //线透明度 
				   strokeWeight:3, //线宽
				   strokeStyle:"solid", //线样式
			   	   strokeDasharray:[10,5] //补充线样式 
			   }); 
			 polyline.setMap(map);
		}
		
	</script>
	
  <div id="tip">  
	<form id="dataSetForm"  method="post" action="./ajaxGetData/ajaxData.jsp" >
	
		<input type="button" value="实时更新" onClick=""/>
		
		 <select id="roadSelect" name ="roads">
		 	<option value=0> All</option>
		 </select>
		
		 <input type="button" value="选择道路"/> 
		 
		 <select id="directionSelect" name="direction">
		 	<option value="0"  > 0  方向</option>
			<option value="1" > 1  方向</option>
		 </select>
		 
		 <input type="button" value="设置方向"/> 
		 <input type="text" name="time" value="<%=time %>"/>
		 <input type="button" value="设置时间"/>
		 <input type="submit" value="提交设置"/>
	</form>
  </div>
</body>
</html>