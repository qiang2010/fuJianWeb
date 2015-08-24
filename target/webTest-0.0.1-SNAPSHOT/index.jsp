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
	</style>
</head>
<body>
	<div id="mapContainer"></div>
	<div id="tip">  
		<input type="button" value="添加线覆盖物" onClick="javascript:addLine()"/>  
	</div>
	<%
	app.prepareData.PrepareData pp = new app.prepareData.PrepareData();
	app.prepareData.PrepareData.getRoadInfo(); // 首先加载所有道路的信息
		// 根据用户提交表单设置查询条件
	    String time = "";
	    String roadName = "";
	    int direction =1;
	    List<Line> lines = pp.getAllLinePoint(pp.changeToTimeStamp(time), roadName, direction);
		//out.print("<h1> "+ lines.size()+ "</h1>");
	%>
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=ef5828a2b1f622d83e5044efc83c730b"></script>
	<script type="text/javascript">
		var polyline;
		//初始化地图对象，加载地图
		var map = new AMap.Map("mapContainer",{
				resizeEnable: true,
			  	view: new AMap.View2D({
		      	center:new AMap.LngLat(117.628264,25.797241),//地图中心点
		      	zoom:8 //地图显示的缩放级别
		     })
		});	
		// 直接画图。
		 var allLines = new Array();
		<%
		Line tempLine;
		for( int i = 0 ;i < lines.size() ;i++) {
			  tempLine = lines.get(i);
		%>
		   lineArr = new Array();//创建线覆盖物节点坐标数组
		   lineArr[0] = "<%=tempLine.startLong%>" ;
		   lineArr[1] = "<%=tempLine.startLat%>" ;
		   lineArr[2] = "<%=tempLine.endLong%>" ;
		   lineArr[3] = "<%=tempLine.endLat%>" ;
		   lineArr[4] = "<%=tempLine.color%>";
		   
		   allLines[<%=i%>] = lineArr;

	    <% }	%>
		//添加线覆盖物
		
	
		function addLine() {
			
		   var lineArr = new Array();//创建线覆盖物节点坐标数组
		  // lineArr.push(new AMap.LngLat("116.368904","39.913423")); 
		  // lineArr.push(new AMap.LngLat("116.382122","39.901176")); 
		  // lineArr.push(new AMap.LngLat("116.387271","39.912501")); 
		  // lineArr.push(new AMap.LngLat("116.398258","39.904600")); 
		   polyline = new AMap.Polyline({ 
			   path:lineArr, //设置线覆盖物路径
			   strokeColor:"#3366FF", //线颜色
			   strokeOpacity:1, //线透明度 
			   strokeWeight:5, //线宽
			   strokeStyle:"solid", //线样式
			   strokeDasharray:[10,5] //补充线样式 
		   }); 
		   polyline.setMap(map);
		 }
	</script>
</body>
</html>