<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 2015/3/2
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>   <title>应用市场管理系统</title>

  <%@ include file="../common/common.jsp" %>
  <jsp:include page="../common/commonFrame.jsp">
    <jsp:param name="needPage" value="Y"/>
    <jsp:param name="needDate" value="Y"/>
    <jsp:param name="needValidate" value="Y"/>
  </jsp:include>

  <!--引用百度地图API-->
  <style type="text/css">
    html,body{margin:0;padding:0;}
    .iw_poi_title {color:#CC5522;font-size:14px;font-weight:bold;overflow:hidden;padding-right:13px;white-space:nowrap}
    .iw_poi_content {font:12px arial,sans-serif;overflow:visible;padding-top:4px;white-space:-moz-pre-wrap;word-wrap:break-word}
  </style>
  <script type="text/javascript" src="http://api.map.baidu.com/api?ak=BUdI4VNGcf4xtKp4sbByU5OMtK0IReoW&v=2.0&services=true"></script>



</head>
<body class="${webSkin}">

<%@ include file="../include/navbar.jsp" %>
<div class="wrapper row-offcanvas row-offcanvas-left">
  <!-- Left side column. contains the logo and sidebar -->
  <jsp:include page="../include/left.jsp">
    <jsp:param name="enName" value="terminalPosition" />
  </jsp:include>

  <!-- Right side column. Contains the navbar and content of the page -->
  <aside class="right-side">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>位置查询</h1>
      <ucfPay:ContentHeaderTag enName="terminalPosition" />
    </section>

    <!-- Main content -->
    <section class="content">
      <form id="frmQry" name="frmQry" role="form" action="" METHOD="post" class="form-horizontal">
        <input type="hidden" name="page" value="0"/>
        <input type="hidden" name="size" value="20"/>
        <input type="hidden" name="sort" value="create_time,desc"/>
      <div class="box box-primary">
        <div class="box-body">
            <div class="row form-group">
              <label class="col-sm-1 control-label">IMEI号：</label>
              <div class="col-sm-2"><input type="text"  id="imeiInput" name="nameLike" class="form-control" /></div>

              <label class="col-sm-1 control-label">机身号：</label>
              <div class="col-sm-2"><input type="text"  id="snInput" name="sn" class="form-control" /></div>

            <label class="col-sm-1 control-label">机构：</label>
            <div class="col-sm-2">
              <select name="uidName" class="form-control" id="uidName">
                <option value="">--请选择--</option>
                <c:forEach items="${users}" var="user">
                  <option value="${user.id}">${user.id}-${user.account}</option>
                </c:forEach>
              </select>
            </div>
            <label class="col-sm-1 control-label">区域：</label>
            <div class="col-sm-2">
              <select name="areaName" class="form-control" id="areaName">
                <option value="">--请选择--</option>
                <c:forEach items="${areas}" var="area">
                  <option value="${area.name}"> ${area.name}</option>
                </c:forEach>
              </select>
            </div>

          </div>
        </div>

       <%-- <div class="box-body">
          <div class="row form-group">
            <label class="col-sm-2 control-label">地址：</label>
            <div class="col-xs-3"><input type="text" name="address" class="form-control" /></div>
          </div>
        </div>--%>

        <div class="box-footer">
          <button id="btnQry" type="button" onclick="queryPosition()" class="btn"><i class="fa fa-search"></i>查询</button>
         <%-- <shiro:hasPermission name="terminalManage:terminalControl:CREATE">
             &lt;%&ndash; <button id="btnExport" type="button" class="btn">导出终端</button>
            <a href="terminal/add" type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus"></i>添加终端</a>&ndash;%&gt;
              <a href="terminal/preImport" type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">
                  <i class="fa fa-plus"></i> 终端入库
              </a>
          </shiro:hasPermission>--%>
        </div>
        </div>
      </form>
      <div class="box">
        <!--百度地图容器-->
        <div style="width:100%;height:600px;border:#ccc solid 1px;" id="dituContent"></div>
      </div>

    </section><!-- /.content -->
  </aside><!-- /.right-side -->
</div><!-- ./wrapper -->
<%@ include file="../include/footer.jsp" %>
</body>
</html>

<script type="text/javascript">
    //创建和初始化地图函数：
    function initMap(){
        createMap();//创建地图

    }

    //创建地图函数：
    function createMap(){

        var _url = "/terminal/list";
        $.post(_url,{size:100},
            function(result){
                doResult(result);
            },
            "json"
        );

    }

    function doResult(result){
        if (result.status == 'OK' && result.message.content.length >0) {
            totalElements = result.message.totalElements;
            // 百度地图API功能
            var map = new BMap.Map("dituContent");
            window.map = map;//将map变量存储在全局
            map.centerAndZoom(new BMap.Point(result.message.content[0].shopLongitude,result.message.content[0].shopLatitude), 13);
            map.enableScrollWheelZoom(true);
            var index = 0;
            var myGeo = new BMap.Geocoder();
            var adds = [];
            if (typeof result.message.content != "undefined")
                $.each(result.message.content, function (index, obj) {
                    adds.push(new BMap.Point(obj.shopLongitude,obj.shopLatitude));
                    var marker = new BMap.Marker(new BMap.Point(obj.shopLongitude,obj.shopLatitude));
                    map.addOverlay(marker);
                    var labelgg = new BMap.Label(obj.shopName,{offset:new BMap.Size(20,-10)});
                    labelgg.setStyle({
                        color : "#000",
                        fontSize : "14px",
                        border :"0",
                        fontWeight :"bold"
                    });
                    marker.setLabel(labelgg);
                });

            function bdGEO(){
                var pt = adds[index];
                geocodeSearch(pt);
                index++;
            }
            function geocodeSearch(pt){
                if(index < adds.length-1){
                    setTimeout(window.bdGEO,400);
                }
                myGeo.getLocation(pt, function(rs){
                    var addComp = rs.addressComponents;
                    document.getElementById("result").innerHTML += index + ". " +adds[index-1].lng + "," + adds[index-1].lat + "："  + "商圈(" + rs.business + ")  结构化数据(" + addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber + ")<br/><br/>";
                });
            }

            setMapEvent();//设置地图事件
            addMapControl();//向地图添加控件


        } else {
            // 百度地图API功能
            var map = new BMap.Map("dituContent");
            window.map = map;//将map变量存储在全局
            var point = new BMap.Point(114.023459,22.54791);
            map.centerAndZoom(point,12);

            var geolocation = new BMap.Geolocation();
            geolocation.getCurrentPosition(function(r){
                if(this.getStatus() == BMAP_STATUS_SUCCESS){
                    var mk = new BMap.Marker(r.point);
                    map.addOverlay(mk);
                    map.panTo(r.point);
                }
                else {

                }
            },{enableHighAccuracy: true})

            setMapEvent();//设置地图事件
            addMapControl();//向地图添加控件


        }
    }

    //地图事件设置函数：
    function setMapEvent(){
        map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
        map.enableScrollWheelZoom();//启用地图滚轮放大缩小
        map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
        map.enableKeyboard();//启用键盘上下左右键移动地图
    }

    //地图控件添加函数：
    function addMapControl(){
        //向地图中添加缩放控件
        var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE});
        map.addControl(ctrl_nav);
        //向地图中添加缩略图控件
        var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,isOpen:1});
        map.addControl(ctrl_ove);
        //向地图中添加比例尺控件
        var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
        map.addControl(ctrl_sca);
    }




  function queryPosition() {
      var imei = $("#imeiInput")[0].value;
      var sn = $("#snInput")[0].value;
      var uidName = $("#uidName").val();
      var areaName = $("#areaName").val();
      var _url = "/terminal/list";
      $.post(_url,{size:100,nameLike:imei,sn:sn, uidName:uidName, areaName:areaName},
          function(result){
              doResult(result);
          },
          "json"
      );

  }

    initMap();//创建和初始化地图

</script>